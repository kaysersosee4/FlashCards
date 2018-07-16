package com.example.kaysersose.fiszki.utils;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Kayser Sose on 2018-06-29.
 */

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
