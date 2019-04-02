package com.stkent.speedysubs.networking.ordering;

import android.support.annotation.NonNull;

import com.stkent.speedysubs.networking.Callback;
import com.stkent.speedysubs.networking.ordering.models.CreditCard;
import com.stkent.speedysubs.networking.ordering.models.Customer;
import com.stkent.speedysubs.networking.ordering.models.Order;
import com.stkent.speedysubs.networking.ordering.models.Sandwich;

import java.util.List;

public interface IOrderingApi {

    void logIn(
            @NonNull String username,
            @NonNull String password,
            @NonNull Callback<Customer> callback);

    void getCustomerCreditCards(
            @NonNull Callback<List<CreditCard>> callback);

    void getSandwiches(
            @NonNull Callback<List<Sandwich>> callback);

    void placeOrder(
            @NonNull Customer customer,
            @NonNull Order order,
            @NonNull Callback<Integer> callback);

}
