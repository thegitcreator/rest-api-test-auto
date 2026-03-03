package com.example.utils;

import com.github.javafaker.Faker;
import java.util.Locale;
import java.util.UUID;

public class TestDataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));

    public static String generateLogin() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String generatePassword() {
        return "pass_" + UUID.randomUUID().toString().substring(0, 8) + "A1!"; // Добавляем требования к паролю
    }

    public static String generateUniqueGameTitle() {
        return "Test Game " + UUID.randomUUID().toString().substring(0, 8);
    }
}