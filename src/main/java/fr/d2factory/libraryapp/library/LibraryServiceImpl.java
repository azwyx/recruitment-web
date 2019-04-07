package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LibraryServiceImpl implements Library {

    @Autowired
    private BookRepository bookRepository;

    /*public LibraryImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }*/

    @Override
    public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
        return null;
    }

    @Override
    public void returnBook(Book book, Member member) {

    }
}
