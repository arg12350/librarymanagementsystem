package util;

public final class Config {
    private Config() {}

    public static final String APP_NAME = "BBIT Library Management System";
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "admin123";
    public static final int BORROWING_DAYS = 14;
    public static final int MAX_ACTIVE_BOOKS = 3;
    public static final double FINE_PER_DAY = 5.0;
    public static final String DATA_FILE = "library-data.ser";
}
