package fr.d2factory.libraryapp.tools;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.d2factory.libraryapp.book.Book;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class JsonToBooksConverter {

    public static List<Book> convertJsonFileToBookList() throws FileNotFoundException {
        Gson googleJson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader("src/test/resources/books.json"));
        Type type = new TypeToken<List<Book>>(){}.getType();
        List<Book> books = googleJson.fromJson(br, type);
        return books;
    }
}
