package com.stkent.speedysubs.login;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.stkent.speedysubs.R;
import com.stkent.speedysubs.choosesandwich.SandwichFragment;
import com.stkent.speedysubs.networking.Callback;
import com.stkent.speedysubs.networking.ordering.IOrderingApi;
import com.stkent.speedysubs.networking.ordering.OrderingApi;
import com.stkent.speedysubs.networking.ordering.models.Customer;
import com.stkent.speedysubs.state.ISession;
import com.stkent.speedysubs.state.Session;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;

final class LoginPresenter {

    @NonNull
    private final ILoginView view;

    @NonNull
    private final IOrderingApi orderingApi;

    @NonNull
    private final ISession session;

    LoginPresenter(
            @NonNull final ILoginView view,
            @NonNull final IOrderingApi orderingApi,
            @NonNull final ISession session) {

        this.view = view;
        this.orderingApi = orderingApi;
        this.session = session;
    }

    void onSubmitTapped(
            @NonNull final String username,
            @NonNull final String password) {

        if (username.isEmpty()) {
            view.displayError("Username cannot be blank");
            return;
        }

        if (password.isEmpty()) {
            view.displayError("Password cannot be blank");
            return;
        }

        view.showProgressIndicators(true);

        orderingApi.logIn(
                username,
                password,
                new Callback<Customer>() {
                    @Override
                    public void onSuccess(@NonNull final Customer customer) {
                        session.setCustomer(customer);

                        view.showProgressIndicators(false);
                        view.goToChooseSandwichScreen();
                    }

                    @Override
                    public void onError(@NonNull final String errorMessage) {
                        session.clearCustomer();

                        view.showProgressIndicators(false);
                        view.displayError(errorMessage);
                    }
                });
    }

}
