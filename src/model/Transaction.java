package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status { ACTIVE, RETURNED }
    public enum PaymentStatus { NOT_REQUIRED, UNPAID, PAID }

    private final String id;
    private final String bookId;
    private final String bookTitle;
    private final String memberId;
    private final String memberName;
    private final LocalDate issueDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private Status status;
    private double fineAmount;
    private PaymentStatus paymentStatus;

    public Transaction(String id, String bookId, String bookTitle, String memberId,
                       String memberName, LocalDate issueDate, LocalDate dueDate) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.memberId = memberId;
        this.memberName = memberName;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = Status.ACTIVE;
        this.fineAmount = 0.0;
        this.paymentStatus = PaymentStatus.NOT_REQUIRED;
    }

    public String getId() { return id; }
    public String getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public String getMemberId() { return memberId; }
    public String getMemberName() { return memberName; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public Status getStatus() { return status; }
    public double getFineAmount() { return fineAmount; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }

    public void completeReturn(LocalDate returnDate, double fineAmount) {
        this.returnDate = returnDate;
        this.status = Status.RETURNED;
        this.fineAmount = fineAmount;
        this.paymentStatus = fineAmount > 0 ? PaymentStatus.UNPAID : PaymentStatus.NOT_REQUIRED;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
