package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
/*import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;*/
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Student;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class LibraryTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LibraryServiceImpl libraryService ;


    @Before
    public void setup() {}

    @Test
    public void member_can_borrow_a_book_if_book_is_available(){
        final ISBN isbn = new ISBN(968787565445L);
        final Book bookToBorrow = new Book("Catch 22", "Joseph Heller", isbn);
        Student student = new Student();
        
        Mockito.when(bookRepository.findBook(ArgumentMatchers.anyLong())).thenReturn(bookToBorrow);
        assertThat(libraryService.borrowBook(0, null, null)).isNull();
    }

/*    @Test
    public void borrowed_book_is_no_longer_available(){
        Assertions.fail("Implement me");
    }

    @Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book(){
        Assertions.fail("Implement me");
    }

    @Test
    public void students_pay_10_cents_the_first_30days(){
        Assertions.fail("Implement me");
    }

    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days(){
        Assertions.fail("Implement me");
    }

    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days(){
        Assertions.fail("Implement me");
    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){
        Assertions.fail("Implement me");
    }

    @Test
    public void members_cannot_borrow_book_if_they_have_late_books(){
        Assertions.fail("Implement me");
    }*/
}
