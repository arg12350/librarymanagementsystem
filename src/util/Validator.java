package util;

public final class Validator {
    private Validator() {}

    public static String required(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " is required.");
        }
        return value.trim();
    }

    public static int positiveInt(String value, String field) {
        try {
            int n = Integer.parseInt(value.trim());
            if (n <= 0) throw new NumberFormatException();
            return n;
        } catch (Exception e) {
            throw new IllegalArgumentException(field + " must be a positive integer.");
        }
    }

    public static void phone(String value) {
        if (!value.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone number must contain exactly 10 digits.");
        }
    }

    public static void email(String value) {
        if (!value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Enter a valid email address.");
        }
    }
}
