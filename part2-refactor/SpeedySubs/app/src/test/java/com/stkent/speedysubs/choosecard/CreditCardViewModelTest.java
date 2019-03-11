package com.stkent.speedysubs.choosecard;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.annotation.NonNull;

import com.stkent.speedysubs.networking.Callback;
import com.stkent.speedysubs.networking.ordering.IOrderingApi;
import com.stkent.speedysubs.networking.ordering.models.CreditCard;
import com.stkent.speedysubs.networking.ordering.models.Customer;
import com.stkent.speedysubs.networking.ordering.models.Order;
import com.stkent.speedysubs.networking.ordering.models.Sandwich;
import com.stkent.speedysubs.state.ISession;
import com.stkent.speedysubs.time.ICalendar;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreditCardViewModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    private IOrderingApi mockOrderingApi;

    @Mock
    private ISession mockSession;

    @Mock
    private ICalendar mockCalendar;

    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Test
    public void test_initialCreditCardsAreEmitted_whenViewModelIsCreated() {
        // Given
        when(mockCalendar.today()).thenReturn(LocalDate.of(2018, 3, 31));

        List<CreditCard> initialCreditCards = Arrays.asList(
                new CreditCard(1, "Visa 1111", LocalDate.of(2020, 10, 31)),
                new CreditCard(2, "Visa 2222", LocalDate.of(2018, 7, 31)),
                new CreditCard(3, "Visa 3333", LocalDate.of(2025, 4, 30)));

        when(mockSession.getCustomer())
                .thenReturn(new Customer("test customer", initialCreditCards));

        CreditCardViewModel viewModel = getInitializedViewModel();

        // Then
        assertEquals(initialCreditCards, viewModel.creditCards().getValue());
    }

    @Test
    public void test_expiredCreditCardsAreNotEmitted_whenViewModelIsCreated() {
        // Given
        when(mockCalendar.today()).thenReturn(LocalDate.of(2019, 3, 31));

        List<CreditCard> initialCreditCards = Arrays.asList(
                new CreditCard(1, "Visa 1111", LocalDate.of(2020, 10, 31)),
                new CreditCard(2, "Visa 2222", LocalDate.of(2018, 7, 31)),
                new CreditCard(3, "Visa 3333", LocalDate.of(2025, 4, 30)));

        when(mockSession.getCustomer())
                .thenReturn(new Customer("test customer", initialCreditCards));

        CreditCardViewModel viewModel = getInitializedViewModel();

        // Then
        List<CreditCard> nonExpiredCreditCards = Arrays.asList(
                new CreditCard(1, "Visa 1111", LocalDate.of(2020, 10, 31)),
                new CreditCard(3, "Visa 3333", LocalDate.of(2025, 4, 30)));

        assertEquals(nonExpiredCreditCards, viewModel.creditCards().getValue());
    }

    @Test
    public void test_updatedCreditCardsAreEmitted_afterRefresh() {
        // Given
        when(mockCalendar.today()).thenReturn(LocalDate.of(2018, 3, 31));

        when(mockSession.getCustomer())
                .thenReturn(new Customer("test customer", new ArrayList<CreditCard>()));

        CreditCardViewModel viewModel = getInitializedViewModel();

        List<CreditCard> updatedCreditCards = Arrays.asList(
                new CreditCard(1, "Visa 1111", LocalDate.of(2020, 10, 31)),
                new CreditCard(2, "Visa 2222", LocalDate.of(2018, 7, 31)),
                new CreditCard(3, "Visa 3333", LocalDate.of(2025, 4, 30)),
                new CreditCard(4, "Visa 4444", LocalDate.of(2019, 5, 31)));

        configureGetCreditCardsSuccess(updatedCreditCards);

        // When
        viewModel.onCreditCardsRefreshed();

        // Then
        assertEquals(updatedCreditCards, viewModel.creditCards().getValue());
    }

    @Test
    public void test_expiredCreditCardsAreNotEmitted_afterRefresh() {
        // Given
        when(mockCalendar.today()).thenReturn(LocalDate.of(2020, 3, 31));

        when(mockSession.getCustomer())
                .thenReturn(new Customer("test customer", new ArrayList<CreditCard>()));

        CreditCardViewModel viewModel = getInitializedViewModel();

        List<CreditCard> updatedCreditCards = Arrays.asList(
                new CreditCard(1, "Visa 1111", LocalDate.of(2020, 10, 31)),
                new CreditCard(2, "Visa 2222", LocalDate.of(2018, 7, 31)),
                new CreditCard(3, "Visa 3333", LocalDate.of(2025, 4, 30)),
                new CreditCard(4, "Visa 4444", LocalDate.of(2019, 5, 31)));

        configureGetCreditCardsSuccess(updatedCreditCards);

        // When
        viewModel.onCreditCardsRefreshed();

        // Then
        List<CreditCard> nonExpiredCreditCards = Arrays.asList(
                new CreditCard(1, "Visa 1111", LocalDate.of(2020, 10, 31)),
                new CreditCard(3, "Visa 3333", LocalDate.of(2025, 4, 30)));

        assertEquals(nonExpiredCreditCards, viewModel.creditCards().getValue());
    }

    @Test
    public void test_orderPlacedCorrectly_whenCreditCardSelected() {
        // Given
        when(mockCalendar.today()).thenReturn(LocalDate.of(2018, 3, 31));

        CreditCard creditCardToSelect =
                new CreditCard(2, "Visa 2222", LocalDate.of(2018, 7, 31));

        List<CreditCard> initialCreditCards = Arrays.asList(
                new CreditCard(1, "Visa 1111", LocalDate.of(2020, 10, 31)),
                creditCardToSelect,
                new CreditCard(3, "Visa 3333", LocalDate.of(2025, 4, 30)));

        Customer customer = new Customer("test-customer", initialCreditCards);
        when(mockSession.getCustomer()).thenReturn(customer);

        Sandwich orderSandwich = new Sandwich(123, "Sandwich 123");
        when(mockSession.getOrder()).thenReturn(new Order(orderSandwich, null));

        CreditCardViewModel viewModel = getInitializedViewModel();

        // When
        viewModel.onCreditCardSelected(creditCardToSelect);

        // Then
        verify(mockOrderingApi, times(1)).placeOrder(
                customerCaptor.capture(),
                orderCaptor.capture(),
                ArgumentMatchers.<Callback<Integer>>any());

        Customer actualCustomer = customerCaptor.getValue();
        assertEquals(customer, actualCustomer);

        Order actualOrder = orderCaptor.getValue();

        assertEquals(orderSandwich, actualOrder.getSandwich());
        assertEquals(creditCardToSelect, actualOrder.getCreditCard());
    }

    // Helper methods

    @NonNull
    private CreditCardViewModel getInitializedViewModel() {
        return new CreditCardViewModel(
                mockOrderingApi,
                mockSession,
                mockCalendar);
    }

    private void configureGetCreditCardsSuccess(@NonNull final List<CreditCard> creditCards) {
        doAnswer(new Answer<Callback<List<CreditCard>>>() {
            @Override
            public Callback<List<CreditCard>> answer(final InvocationOnMock invocation) {
                Callback<List<CreditCard>> callback = invocation.getArgument(0);
                callback.onSuccess(creditCards);
                return null;
            }
        }).when(mockOrderingApi).getCustomerCreditCards(
                ArgumentMatchers.<Callback<List<CreditCard>>>any());
    }

}
