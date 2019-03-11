package com.stkent.speedysubs.choosesandwich;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.stkent.speedysubs.choosesandwich.ISandwichView.DisplaySandwich;
import com.stkent.speedysubs.networking.Callback;
import com.stkent.speedysubs.networking.ordering.OrderingApi;
import com.stkent.speedysubs.networking.ordering.models.Order;
import com.stkent.speedysubs.networking.ordering.models.Sandwich;
import com.stkent.speedysubs.state.Session;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

final class SandwichPresenter {

    private static final String PREFS_NAME = "FAVE_STORAGE";
    private static final String FAVE_ID_KEY = "FAVE_ID";

    @NonNull
    private final ISandwichView view;

    @NonNull
    private final SharedPreferences sharedPreferences;

    SandwichPresenter(@NonNull final ISandwichView view, @NonNull final Context context) {
        this.view = view;
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    void onViewCreated() {
        view.setScreenTitle("Choose Sandwich");
    }

    void onStart() {
        view.showProgressViews();

        new OrderingApi().getSandwiches(new Callback<List<Sandwich>>() {
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
        sharedPreferences
                .edit()
                .putInt(FAVE_ID_KEY, sandwich.getId())
                .apply();

        final Order newOrder = new Order();
        newOrder.setSandwich(sandwich);
        Session.getSharedInstance().setOrder(newOrder);

        view.goToChooseCreditCardScreen();
    }

    @NonNull
    private List<DisplaySandwich> processSandwiches(@NonNull final List<Sandwich> sandwiches) {
        final int favoriteId = sharedPreferences.getInt(FAVE_ID_KEY, -1);

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
