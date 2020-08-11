package com.zukron.note.util;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/7/2020
 */
public class Tools {
    public static String formatDate(LocalDate localDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd LLL");
        return localDate.format(dateTimeFormatter);
    }
}
