package com.stkent.speedysubs.choosecard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.stkent.speedysubs.networking.Callback;
import com.stkent.speedysubs.networking.ordering.IOrderingApi;
import com.stkent.speedysubs.networking.ordering.OrderingApi;
import com.stkent.speedysubs.networking.ordering.models.CreditCard;
import com.stkent.speedysubs.networking.ordering.models.Customer;
import com.stkent.speedysubs.networking.ordering.models.Order;
import com.stkent.speedysubs.state.ISession;
import com.stkent.speedysubs.state.Session;
import com.stkent.speedysubs.time.ICalendar;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess") // Public access needed to avoid runtime instantiation crash.
public final class CreditCardViewModel extends ViewModel {

    private final MutableLiveData<String> _title = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _showProgressViews = new MutableLiveData<>();
    private final MutableLiveData<Object> _endRefresh = new MutableLiveData<>();
    private final MutableLiveData<List<CreditCard>> _creditCards = new MutableLiveData<>();
    private final MutableLiveData<Integer> _orderConfirmationNumber = new MutableLiveData<>();
    private final MutableLiveData<String> _errors = new MutableLiveData<>();

    @NonNull
    private final IOrderingApi orderingApi;

    @NonNull
    private final ISession session;

    @NonNull
    private final ICalendar calendar;

    CreditCardViewModel(
            @NonNull final IOrderingApi orderingApi,
            @NonNull final ISession session,
            @NonNull final ICalendar calendar) {

        this.orderingApi = orderingApi;
        this.session = session;
        this.calendar = calendar;
        _title.setValue("Choose Credit Card");
        _showProgressViews.setValue(false);
        displayCreditCards();
    }

    @NonNull
    LiveData<String> title() {
        return _title;
    }

    @NonNull
    LiveData<Boolean> showProgressViews() {
        return _showProgressViews;
    }

    @NonNull
    LiveData<Object> endRefresh() {
        return _endRefresh;
    }

    @NonNull
    LiveData<List<CreditCard>> creditCards() {
        return _creditCards;
    }

    @NonNull
    LiveData<Integer> orderConfirmationNumber() {
        return _orderConfirmationNumber;
    }

    @NonNull
    LiveData<String> errors() {
        return _errors;
    }

    void onCreditCardSelected(@NonNull final CreditCard creditCard) {
        _showProgressViews.setValue(true);

        final Customer customer = session.getCustomer();

        final Order existingOrder = session.getOrder();
        existingOrder.setCreditCard(creditCard);

        orderingApi.placeOrder(
                customer,
                existingOrder,
                new Callback<Integer>() {
                    @Override
                    public void onSuccess(@NonNull final Integer orderId) {
                        session.clearOrder();

                        _showProgressViews.setValue(false);

                        _orderConfirmationNumber.setValue(orderId);
                    }

                    @Override
                    public void onError(@NonNull final String errorMessage) {
                        _showProgressViews.setValue(false);

                        _errors.setValue(errorMessage);
                    }
                });
    }

    void onCreditCardsRefreshed() {
        orderingApi.getCustomerCreditCards(
                new Callback<List<CreditCard>>() {
                    @Override
                    public void onSuccess(@NonNull final List<CreditCard> creditCards) {
                        _endRefresh.setValue(new Object());

                        session.getCustomer().setCreditCards(creditCards);
                        displayCreditCards();
                    }

                    @Override
                    public void onError(@NonNull final String errorMessage) {
                        _endRefresh.setValue(new Object());

                        _errors.setValue(errorMessage);
                    }
                });
    }

    private void displayCreditCards() {
        final List<CreditCard> allCreditCards =
                session.getCustomer().getCreditCards();

        final List<CreditCard> nonExpiredCreditCards = new ArrayList<>();

        for (final CreditCard creditCard : allCreditCards) {
            if (creditCard.getExpirationDate().isAfter(calendar.today())) {
                nonExpiredCreditCards.add(creditCard);
            }
        }

        _creditCards.setValue(nonExpiredCreditCards);
    }

}
