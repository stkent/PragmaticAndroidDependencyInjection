package com.stkent.speedysubs.networking.ordering.models;

import android.support.annotation.NonNull;

import java.util.List;

public final class Customer {

    @NonNull
    private String id;

    @NonNull
    private List<CreditCard> creditCards;

    public Customer(@NonNull final String id, @NonNull final List<CreditCard> creditCards) {
        this.id = id;
        this.creditCards = creditCards;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(@NonNull final List<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Customer customer = (Customer) o;

        return id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
