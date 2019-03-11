package com.stkent.speedysubs.choosesandwich;

import android.support.annotation.NonNull;

import com.stkent.speedysubs.choosesandwich.ISandwichView.DisplaySandwich;
import com.stkent.speedysubs.networking.Callback;
import com.stkent.speedysubs.networking.ordering.IOrderingApi;
import com.stkent.speedysubs.networking.ordering.models.Order;
import com.stkent.speedysubs.networking.ordering.models.Sandwich;
import com.stkent.speedysubs.persistence.IFaveStorage;
import com.stkent.speedysubs.state.ISession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SandwichPresenterTest {

    @Mock
    private ISandwichView mockSandwichView;

    @Mock
    private IOrderingApi mockOrderingApi;

    @Mock
    private ISession mockSession;

    @Mock
    private IFaveStorage mockFaveStorage;

    @Captor
    ArgumentCaptor<Order> orderCaptor;

    @Captor
    ArgumentCaptor<List<DisplaySandwich>> sandwichesCaptor;

    private SandwichPresenter presenter;

    @Before
    public void setUp() {
        presenter = new SandwichPresenter(
                mockSandwichView,
                mockOrderingApi,
                mockSession,
                mockFaveStorage);
    }

    @Test
    public void test_sandwichLoadCallIsMade_onScreenStart() {
        // When
        presenter.onStart();

        // Then
        verify(mockOrderingApi, times(1)).getSandwiches(
                ArgumentMatchers.<Callback<List<Sandwich>>>any());
    }

    @Test
    public void test_storedFaveIsUpdated_whenUserSelectsASandwich() {
        // Given
        Sandwich sandwichToSelect = new Sandwich(456, "Test sandwich 456");

        configureGetSandwichesSuccess(
                Arrays.asList(
                        new Sandwich(123, "Test sandwich 123"),
                        sandwichToSelect,
                        new Sandwich(789, "Test sandwich 789")));

        presenter.onStart();

        // When
        presenter.onSandwichSelected(sandwichToSelect);

        // Then
        verify(mockFaveStorage, times(1)).setFavoriteSandwichId(456);
    }

    @Test
    public void test_orderIsUpdatedCorrectly_whenUserSelectsASandwich() {
        // Given
        Sandwich sandwichToSelect = new Sandwich(456, "Test sandwich 456");

        configureGetSandwichesSuccess(
                Arrays.asList(
                        new Sandwich(123, "Test sandwich 123"),
                        sandwichToSelect,
                        new Sandwich(789, "Test sandwich 789")));

        presenter.onStart();

        // When
        presenter.onSandwichSelected(sandwichToSelect);

        // Then
        verify(mockSession, times(1)).setOrder(orderCaptor.capture());
        Order order = orderCaptor.getValue();
        assertEquals(order.getSandwich(), sandwichToSelect);
    }

    @Test
    public void test_navigationOccurs_whenUserSelectsASandwich() {
        // Given
        Sandwich sandwichToSelect = new Sandwich(456, "Test sandwich 456");

        configureGetSandwichesSuccess(
                Arrays.asList(
                        new Sandwich(123, "Test sandwich 123"),
                        sandwichToSelect,
                        new Sandwich(789, "Test sandwich 789")));

        presenter.onStart();

        // When
        presenter.onSandwichSelected(sandwichToSelect);

        // Then
        verify(mockSandwichView, times(1)).goToChooseCreditCardScreen();
    }

    @Test
    public void test_sandwichesAreShownInCorrectOrder_ifNoFaveExists() {
        // Given
        configureGetSandwichesSuccess(
                Arrays.asList(
                        new Sandwich(123, "Test sandwich 123"),
                        new Sandwich(456, "Test sandwich 456"),
                        new Sandwich(789, "Test sandwich 789")));

        when(mockFaveStorage.getFavoriteSandwichId()).thenReturn(-1);

        // When
        presenter.onStart();

        // Then
        verify(mockSandwichView, times(1)).displaySandwiches(sandwichesCaptor.capture());

        List<Integer> expectedIdOrder = Arrays.asList(123, 456, 789);
        List<Integer> actualIdOrder = extractIds(sandwichesCaptor.getValue());

        assertEquals(expectedIdOrder, actualIdOrder);
    }

    @Test
    public void test_sandwichesAreShownInCorrectOrder_ifFaveExists() {
        // Given
        configureGetSandwichesSuccess(
                Arrays.asList(
                        new Sandwich(123, "Test sandwich 123"),
                        new Sandwich(456, "Test sandwich 456"),
                        new Sandwich(789, "Test sandwich 789")));

        when(mockFaveStorage.getFavoriteSandwichId()).thenReturn(456);

        // When
        presenter.onStart();

        // Then
        verify(mockSandwichView, times(1)).displaySandwiches(sandwichesCaptor.capture());

        List<Integer> expectedIdOrder = Arrays.asList(456, 123, 789);
        List<Integer> actualIdOrder = extractIds(sandwichesCaptor.getValue());

        assertEquals(expectedIdOrder, actualIdOrder);
    }

    // Helper methods

    private void configureGetSandwichesSuccess(@NonNull final List<Sandwich> sandwiches) {
        doAnswer(new Answer<Callback<List<Sandwich>>>() {
            @Override
            public Callback<List<Sandwich>> answer(final InvocationOnMock invocation) {
                Callback<List<Sandwich>> callback = invocation.getArgument(0);
                callback.onSuccess(sandwiches);
                return null;
            }
        }).when(mockOrderingApi).getSandwiches(
                ArgumentMatchers.<Callback<List<Sandwich>>>any());
    }

    @NonNull
    private List<Integer> extractIds(
            @NonNull final List<DisplaySandwich> sandwiches) {

        List<Integer> result = new ArrayList<>();

        for (DisplaySandwich displaySandwich : sandwiches) {
            result.add(displaySandwich.getId());
        }

        return result;
    }

}
