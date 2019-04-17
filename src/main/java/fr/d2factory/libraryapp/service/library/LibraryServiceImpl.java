package fr.d2factory.libraryapp.service.library;

import fr.d2factory.libraryapp.entity.book.Book;
import fr.d2factory.libraryapp.repository.BookRepository;
import fr.d2factory.libraryapp.entity.member.Member;
import fr.d2factory.libraryapp.service.library.exceptions.BookNotAvailableException;
import fr.d2factory.libraryapp.service.library.exceptions.BookNotFoundException;
import fr.d2factory.libraryapp.service.library.exceptions.HasLateBooksException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LibraryServiceImpl implements Library {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book borrowBook(long isbnCode, Member member, LocalDate borrowAt) throws HasLateBooksException, BookNotFoundException {
        // check if the book exist
        if (bookRepository.findBook(isbnCode) == null) throw new BookNotFoundException();

        // check if the book is available
        Book book = bookRepository.findAvailbaleBook(isbnCode);
        if (book == null) throw new BookNotAvailableException();

        // check if member is late
        bookRepository.getBooksBorrowedByMember(member).forEach(borrowedBook -> {
            if (ChronoUnit.DAYS.between(bookRepository.findBorrowedBookDate(borrowedBook), LocalDate.now()) > member.getLimitDate())
                throw new HasLateBooksException();
        });

        return bookRepository.saveMemberBorrowingTheBook(member, book, borrowAt);
    }

    @Override
    public void returnBook(Book book, Member member) {
        if (book != null && book.getIsbn() != null) {
            Book borrowedBook = bookRepository.findBook(book.getIsbn().getIsbnCode());
            LocalDate borrowedAt = bookRepository.findBorrowedBookDate(borrowedBook);
            member.payBook((int) ChronoUnit.DAYS.between(borrowedAt, LocalDate.now()));
            bookRepository.saveMemberReturningTheBook(member, borrowedBook);
        }
    }

    public boolean checkBookIsAvailibale(long isbn) {
        return bookRepository.findAvailbaleBook(isbn) != null ? true : false;
    }

    public List<Book> getBooksBorrowedByMember(Member member) {
        return bookRepository.getBooksBorrowedByMember(member);
    }
}
