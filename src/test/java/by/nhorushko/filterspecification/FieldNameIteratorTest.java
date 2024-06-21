package by.nhorushko.filterspecification;

import org.junit.Test;

import static org.junit.Assert.*;

public class FieldNameIteratorTest {

    @Test
    public void name() {
        FieldNameIterator iterator = new FieldNameIterator("name");

        FieldNameIterator.Item actual = iterator.next();
        assertEquals("name", actual.getValue());
        assertFalse(actual.isCollection());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void name2() {
        FieldNameIterator iterator = new FieldNameIterator("order.name");

        FieldNameIterator.Item actual = iterator.next();
        assertEquals("order", actual.getValue());
        assertFalse(actual.isCollection());
        assertTrue(iterator.hasNext());

        actual = iterator.next();
        assertEquals("name", actual.getValue());
        assertFalse(actual.isCollection());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void name3() {
        FieldNameIterator iterator = new FieldNameIterator("order.$skills.id");

        FieldNameIterator.Item actual = iterator.next();
        assertEquals("order", actual.getValue());
        assertFalse(actual.isCollection());
        assertTrue(iterator.hasNext());


        actual = iterator.next();
        assertEquals("skills", actual.getValue());
        assertTrue(actual.isCollection());
        assertTrue(iterator.hasNext());

        actual = iterator.next();
        assertEquals("id", actual.getValue());
        assertFalse(actual.isCollection());
        assertFalse(iterator.hasNext());
    }
}
