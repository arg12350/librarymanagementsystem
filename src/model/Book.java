package model;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String author;
    private String category;
    private String publisher;
    private int quantity;
    private int availableQuantity;

    public Book(String id, String title, String author, String category,
                String publisher, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.publisher = publisher;
        this.quantity = quantity;
        this.availableQuantity = quantity;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public int getQuantity() { return quantity; }
    public int getAvailableQuantity() { return availableQuantity; }

    public void setQuantity(int quantity) {
        int issued = this.quantity - this.availableQuantity;
        if (quantity < issued) {
            throw new IllegalArgumentException("Quantity cannot be less than currently issued copies.");
        }
        this.quantity = quantity;
        this.availableQuantity = quantity - issued;
    }

    public boolean isAvailable() { return availableQuantity > 0; }

    public void issueCopy() {
        if (!isAvailable()) throw new IllegalStateException("No available copy.");
        availableQuantity--;
    }

    public void returnCopy() {
        if (availableQuantity < quantity) availableQuantity++;
    }
}
