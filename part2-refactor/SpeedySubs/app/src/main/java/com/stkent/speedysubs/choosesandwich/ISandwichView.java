package com.stkent.speedysubs.choosesandwich;

import android.support.annotation.NonNull;

import com.stkent.speedysubs.networking.ordering.models.Sandwich;

import java.util.List;

public interface ISandwichView {

    class DisplaySandwich {

        @NonNull
        private final Sandwich sandwich;

        private boolean isFavorite;

        DisplaySandwich(@NonNull final Sandwich sandwich, final boolean isFavorite) {
            this.sandwich = sandwich;
            this.isFavorite = isFavorite;
        }

        int getId() {
            return sandwich.getId();
        }

        @NonNull
        String getName() {
            return sandwich.getName();
        }

        boolean isFavorite() {
            return isFavorite;
        }

        @NonNull
        Sandwich getSandwich() {
            return sandwich;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final DisplaySandwich that = (DisplaySandwich) o;

            return sandwich.equals(that.sandwich);
        }

        @Override
        public int hashCode() {
            return sandwich.hashCode();
        }

    }

    void displaySandwiches(@NonNull List<DisplaySandwich> sandwiches);
    void goToChooseCreditCardScreen();
    void hideProgressViews();
    void setScreenTitle(@NonNull String title);
    void showError(@NonNull String errorMessage);
    void showProgressViews();
}
