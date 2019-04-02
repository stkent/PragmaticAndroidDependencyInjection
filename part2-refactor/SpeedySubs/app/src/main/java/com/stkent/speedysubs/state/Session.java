package com.stkent.speedysubs.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stkent.speedysubs.networking.ordering.models.Customer;
import com.stkent.speedysubs.networking.ordering.models.Order;

public final class Session implements ISession {

    @Nullable
    private static Session sharedInstance;

    private static final Object SYNC_LOCK = new Object();

    @NonNull
    public static Session getSharedInstance() {
        synchronized (SYNC_LOCK) {
            if (sharedInstance == null) {
                sharedInstance = new Session();
            }

            return sharedInstance;
        }
    }

    @Nullable
    private Customer customer;

    @Nullable
    private Order order;

    private Session() {
        // This constructor intentionally left blank.
    }

    @Override
    @Nullable
    public Customer getCustomer() {
        return customer;
    }

    @Override
    @Nullable
    public Order getOrder() {
        return order;
    }

    @Override
    public void setCustomer(@NonNull final Customer customer) {
        this.customer = customer;
    }

    @Override
    public void clearCustomer() {
        customer = null;
    }

    @Override
    public void setOrder(@NonNull final Order order) {
        this.order = order;
    }

    @Override
    public void clearOrder() {
        order = null;
    }

}
