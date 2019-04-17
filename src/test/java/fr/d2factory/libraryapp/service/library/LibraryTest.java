package fr.d2factory.libraryapp.service.library;

import fr.d2factory.libraryapp.entity.book.Book;
import fr.d2factory.libraryapp.repository.BookRepository;
import fr.d2factory.libraryapp.entity.book.ISBN;
import fr.d2factory.libraryapp.entity.member.Resident;
import fr.d2factory.libraryapp.entity.member.Student;
import fr.d2factory.libraryapp.service.library.exceptions.BookNotAvailableException;
import fr.d2factory.libraryapp.service.library.exceptions.BookNotFoundException;
import fr.d2factory.libraryapp.service.library.exceptions.HasLateBooksException;
import fr.d2factory.libraryapp.tools.JsonToBooksConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileNotFoundException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class LibraryTest {

    @Spy
    private BookRepository bookRepository;

    @InjectMocks
    private LibraryServiceImpl libraryService ;

    Student student;
    Resident resident;
    Book book;
    ISBN isbn;
    LocalDate borrowAt;

    @Before
    public void setup() throws FileNotFoundException {
        isbn = new ISBN(968787565445L);
        book = new Book("Catch 22", "Joseph Heller", isbn);
        // initialize student
        student = new Student();
        student.setName("Alex");
        student.setId(205);
        student.setWallet(62);

        // initialize resident
        resident = new Resident();
        resident.setName("katy");
        resident.setId(118);
        resident.setWallet(85);

        // initialize borrow date
        borrowAt = LocalDate.now();

        bookRepository.addBooks(JsonToBooksConverter.convertJsonFileToBookList());
    }

    @Test(expected = BookNotFoundException.class)
    public void test_borrowed_book_does_not_exist(){
        Mockito.when(bookRepository.findBook(isbn.getIsbnCode())).thenReturn(null);
        libraryService.borrowBook(isbn.getIsbnCode(), null, null);
    }

    @Test(expected = BookNotAvailableException.class)
    public void test_cant_borrow_book_that_is_not_available(){
        //Mockito.when(bookRepository.findBook(isbn.getIsbnCode())).thenReturn(book);
        Mockito.when(bookRepository.findAvailbaleBook(isbn.getIsbnCode())).thenReturn(null);
        libraryService.borrowBook(isbn.getIsbnCode(), null, null);
    }

    @Test
    public void test_member_can_borrow_a_book_if_book_available(){
        libraryService.borrowBook(isbn.getIsbnCode(), student, borrowAt);

        assertThat(libraryService.getBooksBorrowedByMember(student)).contains(book);
    }

    @Test
    public void test_borrowed_book_is_no_longer_available(){
        libraryService.borrowBook(isbn.getIsbnCode(), null, null);

        assertThat(libraryService.checkBookIsAvailibale(isbn.getIsbnCode())).isFalse();
    }

    @Test
    public void test_students_pay_10_cents_the_first_30days(){
        libraryService.borrowBook(isbn.getIsbnCode(), student, borrowAt.minusDays(30));
        libraryService.returnBook(book, student);

        assertThat(student.getWallet()).isEqualTo((62-30*0.10f));
    }

    @Test
    public void test_students_in_1st_year_are_not_taxed_for_the_first_15days(){
        student.setFirstYearStudent(true);
        libraryService.borrowBook(isbn.getIsbnCode(), student, borrowAt.minusDays(20));
        libraryService.returnBook(book, student);

        assertThat(student.getWallet()).isEqualTo((62-5*0.10f));
    }

    @Test
    public void test_students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days(){
        student.setFirstYearStudent(false);
        libraryService.borrowBook(isbn.getIsbnCode(), student, borrowAt.minusDays(34));
        libraryService.returnBook(book, student);

        assertThat(student.getWallet()).isEqualTo((62-30*0.10f-4*0.15f));
    }

    @Test
    public void test_students_in_1st_year_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days(){
        student.setFirstYearStudent(true);
        libraryService.borrowBook(isbn.getIsbnCode(), student, borrowAt.minusDays(40));
        libraryService.returnBook(book, student);

        assertThat(student.getWallet()).isEqualTo((62-15*0.10f-10*0.15f));
    }

    @Test
    public void test_residents_are_taxed_10cents_for_each_day_they_keep_a_book(){
        libraryService.borrowBook(isbn.getIsbnCode(), resident, borrowAt.minusDays(60));
        libraryService.returnBook(book, resident);

        assertThat(resident.getWallet()).isEqualTo((85-60*0.10f));
    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){
        libraryService.borrowBook(isbn.getIsbnCode(), resident, borrowAt.minusDays(75));
        libraryService.returnBook(book, resident);

        assertThat(resident.getWallet()).isEqualTo((85-60*0.10f-15*0.20f));
    }

    @Test(expected = HasLateBooksException.class)
    public void resident_cannot_borrow_book_if_they_have_late_books(){
        ISBN secondIsbn = new ISBN(46578964513L);
        Book secondBook = new Book("Harry Potter", "J.K. Rowling", secondIsbn);

        // borrow the fisrt book and keep it more then 60 days
        libraryService.borrowBook(isbn.getIsbnCode(), resident, borrowAt.minusDays(75));

        // try to borrow the second book
        libraryService.borrowBook(secondIsbn.getIsbnCode(), resident, LocalDate.now());
    }

    @Test(expected = HasLateBooksException.class)
    public void student_cannot_borrow_book_if_they_have_late_books(){
        ISBN secondIsbn = new ISBN(46578964513L);

        // borrow the fisrt book and keep it more then 30 days
        libraryService.borrowBook(isbn.getIsbnCode(), student, borrowAt.minusDays(46));

        // try to borrow the second book
        libraryService.borrowBook(secondIsbn.getIsbnCode(), student, LocalDate.now());
    }

    @Test
    public void members_can_borrow_book_if_they_dont_have_late_books(){
        ISBN secondIsbn = new ISBN(46578964513L);
        Book secondBook = new Book("Harry Potter", "J.K. Rowling", secondIsbn);

        libraryService.borrowBook(secondIsbn.getIsbnCode(), student, borrowAt.minusDays(12));
        // borrow the fisrt book and keep it 12 days
        libraryService.borrowBook(isbn.getIsbnCode(), student, LocalDate.now());
        // try to borrow the second book


        assertThat(libraryService.getBooksBorrowedByMember(student)).contains(book);
        assertThat(libraryService.getBooksBorrowedByMember(student)).contains(secondBook);
    }
}
