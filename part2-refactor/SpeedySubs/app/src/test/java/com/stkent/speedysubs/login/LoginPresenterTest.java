package com.stkent.speedysubs.login;

import com.stkent.speedysubs.networking.Callback;
import com.stkent.speedysubs.networking.ordering.IOrderingApi;
import com.stkent.speedysubs.networking.ordering.models.CreditCard;
import com.stkent.speedysubs.networking.ordering.models.Customer;
import com.stkent.speedysubs.state.ISession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    @Mock
    private ILoginView mockLoginView;

    @Mock
    private IOrderingApi mockOrderingApi;

    @Mock
    private ISession mockSession;

    @Captor
    ArgumentCaptor<Callback<Customer>> logInCaptor;

    private LoginPresenter presenter;

    @Before
    public void setUp() {
        presenter = new LoginPresenter(
                mockLoginView,
                mockOrderingApi,
                mockSession);
    }

    @Test
    public void test_errorIsShown_ifUsernameIsEmpty() {
        // Given
        String username = "";
        String password = "password";

        // When
        presenter.onSubmitTapped(username, password);

        // Then
        verify(mockLoginView, times(1)).displayError("Username cannot be blank");
    }

    @Test
    public void test_noLoginCallIsMade_ifUsernameIsEmpty() {
        // Given
        String username = "";
        String password = "password";

        // When
        presenter.onSubmitTapped(username, password);

        // Then
        verify(mockOrderingApi, never()).logIn(
                anyString(),
                anyString(),
                ArgumentMatchers.<Callback<Customer>>any());
    }

    @Test
    public void test_errorIsShown_ifPasswordIsEmpty() {
        // Given
        String username = "username";
        String password = "";

        // When
        presenter.onSubmitTapped(username, password);

        // Then
        verify(mockLoginView, times(1)).displayError("Password cannot be blank");
    }

    @Test
    public void test_noLoginCallIsMade_ifPasswordIsEmpty() {
        // Given
        String username = "username";
        String password = "";

        // When
        presenter.onSubmitTapped(username, password);

        // Then
        verify(mockOrderingApi, never()).logIn(
                anyString(),
                anyString(),
                ArgumentMatchers.<Callback<Customer>>any());
    }

    @Test
    public void test_loginCallIsMade_ifUsernameAndPasswordAreNonEmpty() {
        // Given
        String username = "username";
        String password = "password";

        // When
        presenter.onSubmitTapped(username, password);

        // Then
        verify(mockOrderingApi, times(1)).logIn(
                eq(username),
                eq(password),
                ArgumentMatchers.<Callback<Customer>>any());
    }

    @Test
    public void test_navigationOccurs_ifLoginIsSuccessful() {
        // Given
        String username = "username";
        String password = "password";

        // When
        presenter.onSubmitTapped(username, password);

        // Then
        verify(mockOrderingApi, times(1))
                .logIn(eq(username), eq(password), logInCaptor.capture());

        Customer dummyCustomer = new Customer("", new ArrayList<CreditCard>());
        logInCaptor.getValue().onSuccess(dummyCustomer);

        verify(mockLoginView, times(1)).goToChooseSandwichScreen();
    }

    @Test
    public void test_navigationDoesNotOccur_ifLoginIsUnsuccessful() {
        // Given
        String username = "username";
        String password = "password";

        // When
        presenter.onSubmitTapped(username, password);

        // Then
        verify(mockOrderingApi, times(1))
                .logIn(eq(username), eq(password), logInCaptor.capture());

        logInCaptor.getValue().onError("User not found");

        verify(mockLoginView, never()).goToChooseSandwichScreen();
    }

    @Test
    public void test_sessionIsUpdated_ifLoginIsSuccessful() {
        // Given
        String username = "username";
        String password = "password";

        // When
        presenter.onSubmitTapped(username, password);

        // Then
        verify(mockOrderingApi, times(1))
                .logIn(eq(username), eq(password), logInCaptor.capture());

        String customerId = "1234567890";
        Customer customer = new Customer(customerId, new ArrayList<CreditCard>());
        logInCaptor.getValue().onSuccess(customer);

        verify(mockSession, times(1)).setCustomer(customer);
    }

}
