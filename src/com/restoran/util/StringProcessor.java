package com.restoran.util;

/**
 * String işlemleri için utility sınıfı
 * String methodları ve StringBuilder kullanımı
 */
public class StringProcessor {
    public static String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String formatPrice(double price) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%.2f", price));
        sb.append(" TL");
        return sb.toString();
    }

    public static String truncate(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    public static boolean containsIgnoreCase(String source, String target) {
        if (source == null || target == null) {
            return false;
        }
        return source.toLowerCase().contains(target.toLowerCase());
    }
}

