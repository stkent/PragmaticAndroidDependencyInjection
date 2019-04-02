package com.stkent.speedysubs.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stkent.speedysubs.networking.ordering.models.Customer;
import com.stkent.speedysubs.networking.ordering.models.Order;

public interface ISession {

    @Nullable
    Customer getCustomer();

    @Nullable
    Order getOrder();

    void setCustomer(@NonNull Customer customer);
    void clearCustomer();
    void setOrder(@NonNull Order order);
    void clearOrder();

}
