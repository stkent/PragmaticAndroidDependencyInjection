package com.stkent.speedysubs.choosesandwich;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.stkent.speedysubs.choosesandwich.ISandwichView.DisplaySandwich;
import com.stkent.speedysubs.networking.Callback;
import com.stkent.speedysubs.networking.ordering.IOrderingApi;
import com.stkent.speedysubs.networking.ordering.OrderingApi;
import com.stkent.speedysubs.networking.ordering.models.Order;
import com.stkent.speedysubs.networking.ordering.models.Sandwich;
import com.stkent.speedysubs.persistence.IFaveStorage;
import com.stkent.speedysubs.state.ISession;
import com.stkent.speedysubs.state.Session;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

final class SandwichPresenter {

    @NonNull
    private final ISandwichView view;

    @NonNull
    private final IOrderingApi orderingApi;

    @NonNull
    private final ISession session;

    @NonNull
    private final IFaveStorage faveStorage;

    SandwichPresenter(
            @NonNull final ISandwichView view,
            @NonNull final IOrderingApi orderingApi,
            @NonNull final ISession session,
            @NonNull final IFaveStorage faveStorage) {

        this.view = view;
        this.orderingApi = orderingApi;
        this.session = session;
        this.faveStorage = faveStorage;
    }

    void onViewCreated() {
        view.setScreenTitle("Choose Sandwich");
    }

    void onStart() {
        view.showProgressViews();

        orderingApi.getSandwiches(new Callback<List<Sandwich>>() {
            @Override
            public void onSuccess(@NonNull final List<Sandwich> sandwiches) {
                view.hideProgressViews();

                final List<DisplaySandwich> displaySandwiches = processSandwiches(sandwiches);
                view.displaySandwiches(displaySandwiches);
            }

            @Override
            public void onError(@NonNull final String errorMessage) {
                view.hideProgressViews();
                view.showError(errorMessage);
            }
        });
    }

    void onSandwichSelected(@NonNull final Sandwich sandwich) {
        faveStorage.setFavoriteSandwichId(sandwich.getId());

        final Order newOrder = new Order();
        newOrder.setSandwich(sandwich);
        session.setOrder(newOrder);

        view.goToChooseCreditCardScreen();
    }

    @NonNull
    private List<DisplaySandwich> processSandwiches(@NonNull final List<Sandwich> sandwiches) {
        final int favoriteId = faveStorage.getFavoriteSandwichId();

        final List<DisplaySandwich> result = new ArrayList<>();

        for (final Sandwich sandwich : sandwiches) {
            if (sandwich.getId() == favoriteId) {
                // Insert favorite at the front of the list.
                result.add(0, new DisplaySandwich(sandwich, true));
            } else {
                // Insert non-favorite at the end of the list.
                result.add(new DisplaySandwich(sandwich, false));
            }
        }

        return result;
    }

}
