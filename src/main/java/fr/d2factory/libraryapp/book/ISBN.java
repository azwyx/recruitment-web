package fr.d2factory.libraryapp.book;

import lombok.Data;

@Data
public class ISBN {
    private long isbnCode;

    public ISBN(long isbnCode) {
        this.isbnCode = isbnCode;
    }
}
