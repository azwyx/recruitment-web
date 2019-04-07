package fr.d2factory.libraryapp.book;

import fr.d2factory.libraryapp.member.Member;
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
    private Map<Member, Book> membersBorrowingBooks = new HashMap<>();

    public boolean addBooks(final List<Book> books) {
        if (!CollectionUtils.isEmpty(books)) {
            availableBooks.putAll(books.stream()
                    .collect(Collectors.toMap(Book::getIsbn, book -> book)));
            return true;
        }

        return false;
    }

    public Book findBook(long isbnCode) {

        for(ISBN isbn : availableBooks.keySet()){
            if (isbn.getIsbnCode() == isbnCode){
                return availableBooks.get(isbn);
            }
        }

        for(Book book : borrowedBooks.keySet()){
            if (book.getIsbn() != null && book.getIsbn().getIsbnCode() == isbnCode){
                return book;
            }
        }

        return null;
    }

    public Book findAvailbaleBook(long isbnCode) {

        for(ISBN isbn : availableBooks.keySet()){
            if (isbn.getIsbnCode() == isbnCode){
                return availableBooks.get(isbn);
            }
        }
        return null;
    }

    public void saveBookBorrow(Book book, LocalDate borrowedAt) {
        availableBooks.remove(book.getIsbn());
        borrowedBooks.put(book, borrowedAt);
    }

    public Book saveBookReturning(Book book) {
        borrowedBooks.remove(book);
        return availableBooks.put(book.getIsbn(), book);
    }

    public LocalDate findBorrowedBookDate(Book book) {
        return borrowedBooks.get(book);
    }

    public List<Book> getAvailableBookList(){
        return availableBooks.values().stream().collect(Collectors.toList());
    }

    public List<Book> getBorrowedBookList(){
        return borrowedBooks.keySet().stream().collect(Collectors.toList());
    }

    public List<Book> getBooksBorrowedByMember(final Member member){
        Map<Member, Book> membersBooks = membersBorrowingBooks.entrySet().stream()
                .filter(e -> e.getKey() == member)
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

        return membersBooks.values().stream().collect(Collectors.toList());
    }
    public void saveMemberBorrowingTheBook(Member member, Book book, LocalDate date) {
        saveBookBorrow(book, date);
        membersBorrowingBooks.put(member, book);
    }

    public void saveMemberReturningTheBook(Member member, Book book) {
        saveBookReturning(book);
        membersBorrowingBooks.put(member, book);
    }
}
