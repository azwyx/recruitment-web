package fr.d2factory.libraryapp.repository;

import fr.d2factory.libraryapp.entity.book.Book;
import fr.d2factory.libraryapp.entity.book.ISBN;
import fr.d2factory.libraryapp.entity.member.Member;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
    private Map<ISBN, Book> availableBooks = new HashMap<>();
    private Map<Book, LocalDate> borrowedBooks = new HashMap<>();
    private Map<Member, Map<Book, LocalDate>> membersBorrowingBooks = new HashMap<>();

    public boolean addBooks(final List<Book> books) {
        if (!CollectionUtils.isEmpty(books)) {
            availableBooks.putAll(books.stream()
                    .collect(Collectors.toMap(Book::getIsbn, book -> book)));
            return true;
        }

        return false;
    }

    public Book findBook(long isbnCode) {

        for (ISBN isbn : availableBooks.keySet()) {
            if (isbn.getIsbnCode() == isbnCode) {
                return availableBooks.get(isbn);
            }
        }

        for (Book book : borrowedBooks.keySet()) {
            if (book.getIsbn() != null && book.getIsbn().getIsbnCode() == isbnCode) {
                return book;
            }
        }

        return null;
    }

    public Book findAvailbaleBook(long isbnCode) {

        for (ISBN isbn : availableBooks.keySet()) {
            if (isbn.getIsbnCode() == isbnCode) {
                return availableBooks.get(isbn);
            }
        }
        return null;
    }

    public Book saveBookBorrow(Book borrowedBook, LocalDate borrowedAt) {
        availableBooks.remove(borrowedBook.getIsbn());
        borrowedBooks.put(borrowedBook, borrowedAt);
        return borrowedBook;
    }

    public Book saveBookReturning(Book book) {
        borrowedBooks.remove(book);
        return availableBooks.put(book.getIsbn(), book);
    }

    public LocalDate findBorrowedBookDate(Book book) {
        return borrowedBooks.get(book);
    }

    public List<Book> getAvailableBookList() {
        return availableBooks.values().stream().collect(Collectors.toList());
    }

    public List<Book> getBorrowedBookList() {
        return borrowedBooks.keySet().stream().collect(Collectors.toList());
    }

    public List<Book> getBooksBorrowedByMember(final Member member) {
        Map<Book, LocalDate> books = new HashMap<>();
        membersBorrowingBooks.entrySet().stream()
                .filter(e -> {
                    if (e.getKey() == member) {
                        books.putAll(e.getValue());
                        return true;
                    }
                    return false;
                })
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

        return books.keySet().stream().collect(Collectors.toList());
    }

    public Book saveMemberBorrowingTheBook(Member member, Book borrowedBook, LocalDate borrowAt) {
        Map<Book, LocalDate> bookBorrowedAt = new HashMap<>();
        bookBorrowedAt.put(borrowedBook, borrowAt);
        if (membersBorrowingBooks.get(member) != null) {
            membersBorrowingBooks.get(member).put(borrowedBook, borrowAt);
        } else {
            membersBorrowingBooks.put(member, bookBorrowedAt);
        }
        return saveBookBorrow(borrowedBook, borrowAt);
    }

    public Book saveMemberReturningTheBook(Member member, Book bookReturned) {
        Map<Book, LocalDate> bookBorrowedAt = membersBorrowingBooks.get(member);
        if (bookBorrowedAt != null) bookBorrowedAt.remove(bookReturned);
        return saveBookReturning(bookReturned);
    }
}
