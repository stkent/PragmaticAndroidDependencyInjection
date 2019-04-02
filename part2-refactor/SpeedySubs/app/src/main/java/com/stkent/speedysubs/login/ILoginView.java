package com.stkent.speedysubs.login;

import android.support.annotation.NonNull;

public interface ILoginView {

    void showProgressIndicators(boolean show);
    void goToChooseSandwichScreen();
    void displayError(@NonNull String message);

}
