package service;

import exception.*;
import model.Book;
import model.Member;
import model.Transaction;
import util.Config;
import util.DataStore;
import util.DateUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class LibraryService {
    private final DataStore.LibraryData data;

    public LibraryService() {
        data = DataStore.load();
    }

    public ArrayList<Book> getBooks() { return data.books; }
    public ArrayList<Member> getMembers() { return data.members; }
    public ArrayList<Transaction> getTransactions() { return data.transactions; }

    private void save() {
        try {
            DataStore.save(data);
        } catch (IOException e) {
            throw new IllegalStateException("Could not save library data: " + e.getMessage(), e);
        }
    }

    public void addBook(Book book) throws DuplicateBookException {
        if (findBook(book.getId()) != null)
            throw new DuplicateBookException("Book ID already exists: " + book.getId());
        data.books.add(book);
        save();
    }

    public void updateBook(Book book) throws BookNotFoundException {
        Book old = findBook(book.getId());
        if (old == null) throw new BookNotFoundException("Book not found: " + book.getId());

        old.setTitle(book.getTitle());
        old.setAuthor(book.getAuthor());
        old.setCategory(book.getCategory());
        old.setPublisher(book.getPublisher());
        old.setQuantity(book.getQuantity());
        save();
    }

    public void deleteBook(String id) throws BookNotFoundException, InvalidTransactionException {
        Book book = findBookOrThrow(id);
        for (Transaction t : data.transactions) {
            if (t.getBookId().equals(id) && t.getStatus() == Transaction.Status.ACTIVE) {
                throw new InvalidTransactionException("Cannot delete a book with an active issue.");
            }
        }
        data.books.remove(book);
        save();
    }

    public Book findBook(String id) {
        for (Book b : data.books) if (b.getId().equalsIgnoreCase(id)) return b;
        return null;
    }

    public Book findBookOrThrow(String id) throws BookNotFoundException {
        Book b = findBook(id);
        if (b == null) throw new BookNotFoundException("Book not found: " + id);
        return b;
    }

    public void addMember(Member member) throws DuplicateMemberException {
        if (findMember(member.getId()) != null)
            throw new DuplicateMemberException("Member ID already exists: " + member.getId());
        data.members.add(member);
        save();
    }

    public void updateMember(Member member) throws MemberNotFoundException {
        Member old = findMember(member.getId());
        if (old == null) throw new MemberNotFoundException("Member not found: " + member.getId());

        old.setName(member.getName());
        old.setDepartment(member.getDepartment());
        old.setPhone(member.getPhone());
        old.setEmail(member.getEmail());
        save();
    }

    public void deleteMember(String id) throws MemberNotFoundException, InvalidTransactionException {
        Member member = findMemberOrThrow(id);
        if (activeCountForMember(id) > 0)
            throw new InvalidTransactionException("Cannot delete a member with active issued books.");
        data.members.remove(member);
        save();
    }

    public Member findMember(String id) {
        for (Member m : data.members) if (m.getId().equalsIgnoreCase(id)) return m;
        return null;
    }

    public Member findMemberOrThrow(String id) throws MemberNotFoundException {
        Member m = findMember(id);
        if (m == null) throw new MemberNotFoundException("Member not found: " + id);
        return m;
    }

    public Transaction issueBook(String memberId, String bookId)
            throws MemberNotFoundException, BookNotFoundException,
            BookUnavailableException, BorrowingLimitExceededException {
        Member member = findMemberOrThrow(memberId);
        Book book = findBookOrThrow(bookId);

        if (!book.isAvailable())
            throw new BookUnavailableException("No available copy of this book.");
        if (activeCountForMember(memberId) >= Config.MAX_ACTIVE_BOOKS)
            throw new BorrowingLimitExceededException("A member can have at most 3 active books.");

        LocalDate issueDate = LocalDate.now();
        Transaction transaction = new Transaction(
                "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                book.getId(), book.getTitle(), member.getId(), member.getName(),
                issueDate, DateUtil.dueDate(issueDate)
        );

        book.issueCopy();
        data.transactions.add(transaction);
        save();
        return transaction;
    }

    public Transaction returnBook(String transactionId) throws InvalidTransactionException, BookNotFoundException {
        Transaction t = findTransaction(transactionId);
        if (t == null) throw new InvalidTransactionException("Transaction not found: " + transactionId);
        if (t.getStatus() == Transaction.Status.RETURNED)
            throw new InvalidTransactionException("This transaction has already been returned.");

        Book book = findBookOrThrow(t.getBookId());
        LocalDate returnDate = LocalDate.now();
        double fine = DateUtil.fine(t, returnDate);

        book.returnCopy();
        t.completeReturn(returnDate, fine);
        save();
        return t;
    }

    public Transaction findTransaction(String id) {
        for (Transaction t : data.transactions) if (t.getId().equalsIgnoreCase(id)) return t;
        return null;
    }

    public int activeCountForMember(String memberId) {
        int count = 0;
        for (Transaction t : data.transactions) {
            if (t.getMemberId().equalsIgnoreCase(memberId) &&
                    t.getStatus() == Transaction.Status.ACTIVE) count++;
        }
        return count;
    }

    public long overdueCount() {
        long count = 0;
        LocalDate today = LocalDate.now();
        for (Transaction t : data.transactions) {
            if (t.getStatus() == Transaction.Status.ACTIVE && today.isAfter(t.getDueDate())) count++;
        }
        return count;
    }

    public double outstandingFines() {
        double total = 0;
        for (Transaction t : data.transactions) {
            if (t.getFineAmount() > 0 && t.getPaymentStatus() == Transaction.PaymentStatus.UNPAID)
                total += t.getFineAmount();
        }
        return total;
    }

    public void setFinePaid(String transactionId, boolean paid) throws InvalidTransactionException {
        Transaction t = findTransaction(transactionId);
        if (t == null) throw new InvalidTransactionException("Transaction not found.");
        if (t.getFineAmount() <= 0) throw new InvalidTransactionException("This transaction has no fine.");
        t.setPaymentStatus(paid ? Transaction.PaymentStatus.PAID : Transaction.PaymentStatus.UNPAID);
        save();
    }

    public List<Transaction> recentTransactions(int limit) {
        ArrayList<Transaction> copy = new ArrayList<>(data.transactions);
        copy.sort(Comparator.comparing(Transaction::getIssueDate).reversed());
        return copy.subList(0, Math.min(limit, copy.size()));
    }

    public void loadSampleData() throws DuplicateBookException, DuplicateMemberException,
            MemberNotFoundException, BookNotFoundException, BookUnavailableException,
            BorrowingLimitExceededException, InvalidTransactionException {
        if (!data.books.isEmpty() || !data.members.isEmpty() || !data.transactions.isEmpty())
            throw new InvalidTransactionException("Sample data is only available when the library is empty.");

        data.books.add(new Book("B101", "Clean Code", "Robert C. Martin", "Programming", "Prentice Hall", 4));
        data.books.add(new Book("B102", "Computer Networks", "Andrew S. Tanenbaum", "Networking", "Pearson", 3));
        data.books.add(new Book("B103", "Operating System Concepts", "Silberschatz", "Operating Systems", "Wiley", 5));
        data.books.add(new Book("B104", "Data Structures", "Seymour Lipschutz", "DSA", "McGraw Hill", 2));

        data.members.add(new Member("M101", "Aarav Sen", "CSE", "9876543210", "aarav@example.com"));
        data.members.add(new Member("M102", "Riya Das", "CSE", "9876543211", "riya@example.com"));
        data.members.add(new Member("M103", "Kabir Roy", "ECE", "9876543212", "kabir@example.com"));

        LocalDate issue = LocalDate.now().minusDays(18);
        Transaction overdue = new Transaction("TXN-SAMPLE1", "B101", "Clean Code", "M101", "Aarav Sen",
                issue, issue.plusDays(Config.BORROWING_DAYS));
        data.books.get(0).issueCopy();
        overdue.completeReturn(LocalDate.now(), 20);
        overdue.setPaymentStatus(Transaction.PaymentStatus.UNPAID);
        data.transactions.add(overdue);

        issueBook("M102", "B102");
        save();
    }
}
