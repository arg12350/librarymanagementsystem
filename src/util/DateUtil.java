package util;

import model.Transaction;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class DateUtil {
    private DateUtil() {}

    public static LocalDate dueDate(LocalDate issueDate) {
        return issueDate.plusDays(Config.BORROWING_DAYS);
    }

    public static long overdueDays(Transaction transaction, LocalDate date) {
        if (!date.isAfter(transaction.getDueDate())) return 0;
        return ChronoUnit.DAYS.between(transaction.getDueDate(), date);
    }

    public static double fine(Transaction transaction, LocalDate date) {
        return overdueDays(transaction, date) * Config.FINE_PER_DAY;
    }
}
