package fr.d2factory.libraryapp.member;

import lombok.Data;

@Data
public class Student extends Member {

    public static float STUDENT_TAX = 0.10f;
    public static float STUDENT_TAX_SUPP_30 = 0.15f;
    public static int NB_DAY_WITH_NORMAL_TAX = 30;
    public static int FREE_PERIOD = 15;

    private boolean firstYearStudent;

    @Override
    public void payBook(int numberOfDays) {

        if (numberOfDays > NB_DAY_WITH_NORMAL_TAX) {
            wallet = wallet - (STUDENT_TAX * NB_DAY_WITH_NORMAL_TAX) - STUDENT_TAX_SUPP_30 * (numberOfDays - NB_DAY_WITH_NORMAL_TAX);
        } else {
            wallet = wallet - (STUDENT_TAX * numberOfDays);
        }

        // 15 days of free period for each book for first year student
        if (firstYearStudent) {
            if (numberOfDays > FREE_PERIOD) wallet = wallet + STUDENT_TAX * FREE_PERIOD;
            else wallet = wallet + STUDENT_TAX * numberOfDays;
        }
    }

    @Override
    public int getLimitDate() {
        return NB_DAY_WITH_NORMAL_TAX;
    }
}
