package fr.d2factory.libraryapp.member;

import lombok.Data;

@Data
public class Student extends Member {

    private boolean firstYearStudent;

    @Override
    public void payBook(int numberOfDays) {

    }
}
