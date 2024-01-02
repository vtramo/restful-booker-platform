package com.rbp.unit;

import com.rbp.service.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("A RandomString generator")
class RandomStringUT {
    RandomString randomStringGenerator;

    @ParameterizedTest
    @ValueSource(ints = {-1, -3, -10, -30, -59, -99, -100, -101, -1000, -191919, -999999})
    @DisplayName("when length is negative, should throw IllegalArgumentException")
    void testConstructorLengthIsNegative(int length) {
        assertThrows(IllegalArgumentException.class, () -> new RandomString(length));
    }

    @Test
    @DisplayName("when random is null, should throw NullPointerException")
    void testConstructorRandomIsNull() {
        assertThrows(NullPointerException.class, () -> new RandomString(5, null));
    }

    @Test
    @DisplayName("when symbols is null, should throw NullPointerException")
    void testConstructorSymbolsIsNull() {
        assertThrows(NullPointerException.class, () -> new RandomString(5, new Random(), null));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 30, 40, 50, 60, 70, 80, 1000, 2000})
    @DisplayName("when length is n and nextString is called, it should return a string of length n")
    void testNextStringLength(int length) {
        randomStringGenerator = new RandomString(length);

        final String randomString = randomStringGenerator.nextString();

        assertThat(randomString.length(), is(equalTo(length)));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 30, 40, 50, 60, 70, 80, 1000, 2000})
    @DisplayName("when length is 20 and nextString is called n times, it should return every time a different string")
    void testNextStringRandomness(int n) {
        randomStringGenerator = new RandomString(20);

        final Set<String> randomStrings = new HashSet<>(n);
        for (int i = 0; i < n; i++) {
            final String randomString = randomStringGenerator.nextString();
            assertThat(randomStrings.add(randomString), is(equalTo(true)));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 30, 40, 50, 60, 70, 80, 1000, 2000})
    @DisplayName("when using alphanum as symbols, generated string should contain only characters from alphanum")
    void testAlphanumericSymbols(int n) {
        randomStringGenerator = new RandomString(20);

        final Set<String> randomStrings = new HashSet<>(n);
        for (int i = 0; i < n; i++) {
            final String randomString = randomStringGenerator.nextString();
            assertThat(randomStrings.add(randomString), is(equalTo(true)));
        }

        assertThat(randomStrings, everyItem(not(matchesPattern("[^A-Za-z0-9]"))));
    }
}
