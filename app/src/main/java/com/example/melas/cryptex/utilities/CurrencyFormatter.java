package com.example.melas.cryptex.utilities;

import android.icu.text.NumberFormat;

import java.util.Locale;


public class CurrencyFormatter {
    public static String formatCurrency (double amount, String code) {
        NumberFormat format;
        String formatted = String.format(Locale.getDefault(),"%.2f", amount);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            format.setCurrency(android.icu.util.Currency.getInstance(code));
            formatted = format.format(amount);
        }

        return formatted;
    }
}
