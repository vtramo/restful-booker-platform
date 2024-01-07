package com.rbp.service;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * This class is used to generate random strings.
 */
public class RandomString {

    /**
     * Generate a random string.
     *
     * @return a newly generated random string
     */
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    // The string of all upper case letters
    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // The string of all lower case letters
    public static final String lower = upper.toLowerCase(Locale.ENGLISH);

    // The string of all numeric digits
    public static final String digits = "0123456789";

    // A string of all alpha-numeric characters
    public static final String alphanum = upper + lower + digits;

    private final Random random;

    // The symbols used for creating random strings
    private final char[] symbols;

    // The buffer used for creating random strings
    private final char[] buf;

    /**
     * Creates a new random string generator.
     *
     * @param length the length of the generated strings
     * @param random the source of randomness
     * @param symbols the set of symbols to choose from
     * @throws IllegalArgumentException if length is less than 1 or symbols has less than 2 characters
     */
    public RandomString(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Creates a new random alphanumeric string generator.
     *
     * @param length the length of the generated strings
     * @param random the source of randomness
     */
    public RandomString(int length, Random random) {
        this(length, random, alphanum);
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     *
     * @param length the length of the generated strings
     */
    public RandomString(int length) {
        this(length, new SecureRandom());
    }

}