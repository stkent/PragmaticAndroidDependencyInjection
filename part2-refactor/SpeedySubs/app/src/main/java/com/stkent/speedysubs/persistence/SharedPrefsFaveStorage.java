package com.stkent.speedysubs.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public final class SharedPrefsFaveStorage implements IFaveStorage {

    private static final String PREFS_NAME = "FAVE_STORAGE";
    private static final String FAVE_ID_KEY = "FAVE_ID";

    @NonNull
    private final SharedPreferences sharedPreferences;

    public SharedPrefsFaveStorage(@NonNull final Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void setFavoriteSandwichId(final int id) {
        sharedPreferences
                .edit()
                .putInt(FAVE_ID_KEY, id)
                .apply();
    }

    @Override
    public int getFavoriteSandwichId() {
        return sharedPreferences.getInt(FAVE_ID_KEY, -1);
    }

}
