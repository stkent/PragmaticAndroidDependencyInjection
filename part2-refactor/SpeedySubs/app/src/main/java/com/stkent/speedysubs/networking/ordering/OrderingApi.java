package com.stkent.speedysubs.networking.ordering;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.stkent.speedysubs.networking.Callback;
import com.stkent.speedysubs.networking.ordering.models.CreditCard;
import com.stkent.speedysubs.networking.ordering.models.Customer;
import com.stkent.speedysubs.networking.ordering.models.Order;
import com.stkent.speedysubs.networking.ordering.models.Sandwich;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;

public final class OrderingApi {

    public void logIn(
            @NonNull final String username,
            @NonNull final String password,
            @NonNull final Callback<Customer> callback) {

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                final List<CreditCard> creditCards = new ArrayList<CreditCard>() {{
                    add(new CreditCard(1, "Visa 1111", LocalDate.of(2020, 10, 31)));
                    add(new CreditCard(2, "Visa 2222", LocalDate.of(2018, 7, 31)));
                    add(new CreditCard(3, "Visa 3333", LocalDate.of(2025, 4, 30)));
                }};

                callback.onSuccess(new Customer(UUID.randomUUID().toString(), creditCards));
            }
        }, SECONDS.toMillis(2));
    }

    public void getCustomerCreditCards(@NonNull final Callback<List<CreditCard>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                final List<CreditCard> creditCards = new ArrayList<CreditCard>() {{
                    add(new CreditCard(1, "Visa 1111", LocalDate.of(2020, 10, 31)));
                    add(new CreditCard(2, "Visa 2222", LocalDate.of(2018, 7, 31)));
                    add(new CreditCard(3, "Visa 3333", LocalDate.of(2025, 4, 30)));
                    add(new CreditCard(4, "Visa 4444", LocalDate.of(2019, 5, 31)));
                }};

                callback.onSuccess(creditCards);
            }
        }, SECONDS.toMillis(2));
    }

    public void getSandwiches(@NonNull final Callback<List<Sandwich>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                final List<Sandwich> sandwiches = new ArrayList<Sandwich>() {{
                    add(new Sandwich(10, "BLT"));
                    add(new Sandwich(20, "Italian"));
                    add(new Sandwich(30, "Veggie"));
                    add(new Sandwich(40, "Philly Cheesesteak"));
                    add(new Sandwich(50, "Everything"));
                }};

                callback.onSuccess(sandwiches);
            }
        }, SECONDS.toMillis(2));
    }

    public void placeOrder(
            @NonNull final Customer customer,
            @NonNull final Order order,
            @NonNull final Callback<Integer> callback) {

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(new Random().nextInt() & Integer.MAX_VALUE);
            }
        }, SECONDS.toMillis(2));
    }

}
