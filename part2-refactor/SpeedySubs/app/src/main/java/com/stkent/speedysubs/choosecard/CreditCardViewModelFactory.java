package com.stkent.speedysubs.choosecard;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.stkent.speedysubs.networking.ordering.OrderingApi;
import com.stkent.speedysubs.state.Session;
import com.stkent.speedysubs.time.SystemCalendar;

final class CreditCardViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
        //noinspection unchecked
        return (T) new CreditCardViewModel(
                new OrderingApi(),
                Session.getSharedInstance(),
                new SystemCalendar());
    }

}
