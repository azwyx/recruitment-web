package fr.d2factory.libraryapp.member;

import lombok.Data;

@Data
public class Resident extends Member {

    public static float RESIDENT_TAX = 0.10f;
    public static float RESIDENT_TAX_SUPP_60 = 0.20f;
    public static int NB_DAY_WITH_NORMAL_TAX = 60;

    @Override
    public void payBook(int numberOfDays) {
        if (numberOfDays > NB_DAY_WITH_NORMAL_TAX) {
            wallet = wallet - (RESIDENT_TAX * NB_DAY_WITH_NORMAL_TAX) - RESIDENT_TAX_SUPP_60 * (numberOfDays - NB_DAY_WITH_NORMAL_TAX);
        } else {
            wallet = wallet - RESIDENT_TAX * numberOfDays;
        }
    }

    @Override
    public int getLimitDate() {
        return NB_DAY_WITH_NORMAL_TAX;
    }
}
