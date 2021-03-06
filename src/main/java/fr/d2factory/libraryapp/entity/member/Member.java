package fr.d2factory.libraryapp.entity.member;

import fr.d2factory.libraryapp.service.library.Library;
import lombok.Data;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */

@Data
public abstract class Member {

    protected Integer id;
    protected String name;
    /**
     * An initial sum of money the member has
     */
    protected float wallet;

    /**
     * The member should pay their books when they are returned to the library
     *
     * @param numberOfDays the number of days they kept the book
     */
    public abstract void payBook(int numberOfDays);

    public abstract int getLimitDate();


}
