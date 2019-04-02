package com.stkent.speedysubs.time;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;

public interface ICalendar {

    @NonNull
    LocalDate today();

}
