package com.stkent.speedysubs.networking.ordering.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

public final class Order {

    @Nullable
    private Sandwich sandwich;

    @Nullable
    private CreditCard creditCard;

    public Order() {
        // This constructor intentionally left blank.
    }

    @VisibleForTesting
    public Order(@Nullable final Sandwich sandwich, @Nullable final CreditCard creditCard) {
        this.sandwich = sandwich;
        this.creditCard = creditCard;
    }

    @Nullable
    public Sandwich getSandwich() {
        return sandwich;
    }

    @Nullable
    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setSandwich(@NonNull final Sandwich sandwich) {
        this.sandwich = sandwich;
    }

    public void setCreditCard(@NonNull final CreditCard creditCard) {
        this.creditCard = creditCard;
    }

}
