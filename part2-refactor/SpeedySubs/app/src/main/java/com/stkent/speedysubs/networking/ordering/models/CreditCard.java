package com.stkent.speedysubs.networking.ordering.models;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;

public final class CreditCard {

    private int id;
    private String displayName;
    private LocalDate expirationDate;

    public CreditCard(
            final int id,
            @NonNull final String displayName,
            @NonNull final LocalDate expirationDate) {

        this.id = id;
        this.displayName = displayName;
        this.expirationDate = expirationDate;
    }

    @NonNull
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    @Override
    @NonNull
    public String toString() {
        return displayName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final CreditCard that = (CreditCard) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
