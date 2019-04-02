package com.stkent.speedysubs.time;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;

public final class SystemCalendar implements ICalendar {

    @NonNull
    @Override
    public LocalDate today() {
        return LocalDate.now();
    }

}
