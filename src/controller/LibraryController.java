package controller;

import exception.*;
import model.Book;
import model.Member;
import model.Transaction;
import service.LibraryService;

import java.util.List;

public class LibraryController {
    private final LibraryService service = new LibraryService();

    public LibraryService getService() { return service; }

    public void addBook(Book b) throws DuplicateBookException { service.addBook(b); }
    public void updateBook(Book b) throws BookNotFoundException { service.updateBook(b); }
    public void deleteBook(String id) throws BookNotFoundException, InvalidTransactionException { service.deleteBook(id); }
    public void addMember(Member m) throws DuplicateMemberException { service.addMember(m); }
    public void updateMember(Member m) throws MemberNotFoundException { service.updateMember(m); }
    public void deleteMember(String id) throws MemberNotFoundException, InvalidTransactionException { service.deleteMember(id); }

    public Transaction issue(String memberId, String bookId) throws LibraryException {
        return service.issueBook(memberId, bookId);
    }

    public Transaction returnBook(String transactionId) throws LibraryException {
        return service.returnBook(transactionId);
    }

    public void setFinePaid(String transactionId, boolean paid) throws InvalidTransactionException {
        service.setFinePaid(transactionId, paid);
    }

    public List<Book> books() { return service.getBooks(); }
    public List<Member> members() { return service.getMembers(); }
    public List<Transaction> transactions() { return service.getTransactions(); }
}
