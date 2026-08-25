package com.dev.passwordgenerator;

import java.security.SecureRandom;

/**
 * Kelas Java untuk logika pembuatan password acak.
 * Dipanggil dari MainActivity.kt (interoperabilitas Kotlin-Java).
 */
public class PasswordUtils {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    public static String generate(int length, boolean useUpper, boolean useLower,
                                   boolean useNumbers, boolean useSymbols) {
        StringBuilder charPool = new StringBuilder();
        if (useUpper) charPool.append(UPPERCASE);
        if (useLower) charPool.append(LOWERCASE);
        if (useNumbers) charPool.append(NUMBERS);
        if (useSymbols) charPool.append(SYMBOLS);

        if (charPool.length() == 0) {
            // Default fallback agar tidak menghasilkan string kosong
            charPool.append(LOWERCASE).append(NUMBERS);
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charPool.length());
            password.append(charPool.charAt(index));
        }
        return password.toString();
    }

    /** Estimasi kekuatan password sederhana: 0 = lemah, 1 = sedang, 2 = kuat */
    public static int estimateStrength(int length, boolean useUpper, boolean useLower,
                                        boolean useNumbers, boolean useSymbols) {
        int variety = 0;
        if (useUpper) variety++;
        if (useLower) variety++;
        if (useNumbers) variety++;
        if (useSymbols) variety++;

        if (length >= 12 && variety >= 3) return 2;
        if (length >= 8 && variety >= 2) return 1;
        return 0;
    }
}
