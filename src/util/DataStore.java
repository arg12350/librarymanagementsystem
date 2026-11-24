package util;

import model.Book;
import model.Member;
import model.Transaction;

import java.io.*;
import java.util.ArrayList;

public final class DataStore {
    private DataStore() {}

    public static class LibraryData implements Serializable {
        private static final long serialVersionUID = 1L;
        public ArrayList<Book> books = new ArrayList<>();
        public ArrayList<Member> members = new ArrayList<>();
        public ArrayList<Transaction> transactions = new ArrayList<>();
    }

    public static LibraryData load() {
        File file = new File(Config.DATA_FILE);
        if (!file.exists()) return new LibraryData();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object value = in.readObject();
            if (value instanceof LibraryData) return (LibraryData) value;
        } catch (IOException | ClassNotFoundException e) {
            File backup = new File(Config.DATA_FILE + ".corrupt");
            if (!file.renameTo(backup)) {
                // Keep the original file if a backup cannot be made.
            }
        }
        return new LibraryData();
    }

    public static void save(LibraryData data) throws IOException {
        File target = new File(Config.DATA_FILE);
        File temp = new File(Config.DATA_FILE + ".tmp");

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(temp))) {
            out.writeObject(data);
        }

        if (target.exists() && !target.delete()) {
            throw new IOException("Unable to replace existing data file.");
        }
        if (!temp.renameTo(target)) {
            throw new IOException("Unable to finalize data file.");
        }
    }
}
