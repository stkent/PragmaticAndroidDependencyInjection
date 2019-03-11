package com.stkent.speedysubs.networking.ordering.models;

import android.support.annotation.NonNull;

public final class Sandwich {

    private int id;
    private String name;

    public Sandwich(final int id, @NonNull final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Sandwich sandwich = (Sandwich) o;

        return id == sandwich.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
