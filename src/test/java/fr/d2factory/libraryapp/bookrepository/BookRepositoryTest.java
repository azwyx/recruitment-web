package fr.d2factory.libraryapp.bookrepository;

import fr.d2factory.libraryapp.entity.book.Book;
import fr.d2factory.libraryapp.repository.BookRepository;
import fr.d2factory.libraryapp.entity.book.ISBN;
import fr.d2factory.libraryapp.entity.member.Student;
import fr.d2factory.libraryapp.tools.JsonToBooksConverter;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BookRepositoryTest {

    BookRepository bookRepository;

    List<Book> books;
    Book book;
    Student student;

    @Before
    public void setup() throws FileNotFoundException {
        bookRepository = new BookRepository();
        //initialize book list
        books = JsonToBooksConverter.convertJsonFileToBookList();
        //initialize a book
        ISBN isbn = new ISBN(3326456467846L);
        book = new Book("Around the world in 80 days", "Around the world in 80 days", isbn);
        book.setTitle("Around the world in 80 days");
        book.setAuthor("Jules Verne");
        book.setIsbn(new ISBN(3326456467846L));
        //initialize student
        student = new Student();
        student.setId(100);
        student.setName("Alex");
        student.setWallet(65);
    }

    @Test
    public void testAddBooksOK() {
        final boolean rslt = bookRepository.addBooks(books);
        assertThat(rslt).isTrue();
        assertThat(bookRepository.getAvailableBookList().size()).isEqualTo(4);
    }

    @Test
    public void testAddBooksKo() {
        final boolean rslt = bookRepository.addBooks(null);

        assertThat(rslt).isFalse();
        assertThat(bookRepository.getAvailableBookList()).isEmpty();
    }

    @Test
    public void testFindBookOK() {
        bookRepository.addBooks(books);
        Book book = bookRepository.findBook(968787565445L);

        assertThat(book.getTitle()).isEqualTo("Catch 22");
    }

    @Test
    public void testFindBookKO() {
        bookRepository.addBooks(books);
        Book book = bookRepository.findBook(12345L);

        assertThat(book).isNull();
    }

    @Test
    public void testFindAvailableBookOK() {
        bookRepository.addBooks(books);
        Book book = bookRepository.findBook(968787565445L);

        assertThat(book.getTitle()).isEqualTo("Catch 22");
    }

    @Test
    public void testSaveBookBorrowOK() {
        LocalDate borrowedAt = LocalDate.now();
        bookRepository.saveBookBorrow(book, borrowedAt);

        assertThat(bookRepository.getAvailableBookList()).doesNotContain(book);
        assertThat(bookRepository.getBorrowedBookList()).contains(book);
    }

    @Test
    public void testSaveBookReturningOK() {
        bookRepository.saveBookReturning(book);

        assertThat(bookRepository.getAvailableBookList()).contains(book);
        assertThat(bookRepository.getBorrowedBookList()).doesNotContain(book);
    }

    @Test
    public void testFindBorrowedBookDateOK() {
        bookRepository.addBooks(books);
        LocalDate borrowedAt = LocalDate.now();
        bookRepository.saveBookBorrow(book, borrowedAt);

        assertThat(bookRepository.findBorrowedBookDate(book)).isEqualTo(borrowedAt);
    }

    @Test
    public void testFindBorrowedBookDateKO() {
        bookRepository.addBooks(books);

        assertThat(bookRepository.findBorrowedBookDate(book)).isNull();
    }

    @Test
    public void testSaveBookBorrow() {
        LocalDate borrowAt = LocalDate.now();
        bookRepository.saveBookBorrow(book, borrowAt);

        assertThat(bookRepository.getAvailableBookList()).doesNotContain(book);
        assertThat(bookRepository.getBorrowedBookList()).contains(book);
    }

    @Test
    public void testSaveBookReturning() {
        bookRepository.saveBookReturning(book);

        assertThat(bookRepository.getAvailableBookList()).contains(book);
        assertThat(bookRepository.getBorrowedBookList()).doesNotContain(book);
    }

    @Test
    public void testMemberBorrowBookOK() {
        LocalDate borrowAt = LocalDate.now();
        bookRepository.saveMemberBorrowingTheBook(student, book, borrowAt);

        assertThat(bookRepository.getAvailableBookList()).doesNotContain(book);
        assertThat(bookRepository.getBorrowedBookList()).contains(book);
        assertThat(bookRepository.findBorrowedBookDate(book)).isEqualTo(borrowAt);
        assertThat(bookRepository.getBooksBorrowedByMember(student)).contains(book);

    }

    @Test
    public void testMemberReturningBookOK() {
        bookRepository.saveMemberReturningTheBook(student, book);

        assertThat(bookRepository.getAvailableBookList()).contains(book);
        assertThat(bookRepository.getBorrowedBookList()).doesNotContain(book);
        assertThat(bookRepository.findBorrowedBookDate(book)).isNull();
    }

    @Test
    public void testSaveMemberBorrowingTheBook() {
        LocalDate borrowAt = LocalDate.now();
        bookRepository.saveMemberBorrowingTheBook(student, book, borrowAt);

        assertThat(bookRepository.getAvailableBookList()).doesNotContain(book);
        assertThat(bookRepository.getBorrowedBookList()).contains(book);
        assertThat(bookRepository.getBooksBorrowedByMember(student)).contains(book);
    }

}
