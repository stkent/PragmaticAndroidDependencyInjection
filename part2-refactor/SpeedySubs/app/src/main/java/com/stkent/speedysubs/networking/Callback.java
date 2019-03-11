package com.stkent.speedysubs.networking;

import android.support.annotation.NonNull;

public interface Callback<T> {

    void onSuccess(@NonNull T value);

    void onError(@NonNull String errorMessage);

}
