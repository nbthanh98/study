package com.thanhnb.jwtauth.utils;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CurrencyUtils {

        public static String toVNCurrency(BigDecimal money) {
                Locale locale = new Locale("vi", "VN");
                Currency currency = Currency.getInstance("VND");

                DecimalFormatSymbols df = DecimalFormatSymbols.getInstance(locale);
                df.setCurrency(currency);
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                numberFormat.setCurrency(currency);
                return numberFormat.format(money);
        }
}
