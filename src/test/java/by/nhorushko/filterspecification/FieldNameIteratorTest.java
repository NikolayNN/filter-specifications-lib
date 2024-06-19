package by.nhorushko.filterspecification;


import org.junit.Test;


import static org.junit.Assert.*;

public class FieldNameIteratorTest {

    @Test
    public void name() {
        String given = "name";
        FieldNameIterator iterator = new FieldNameIterator(given);

        assertTrue(iterator.hasNext());
        FieldNameIterator.Item actual = iterator.next();
        assertEquals("name", actual.getValue());
        assertTrue(actual.isProperty());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void name2() {
        String given = "obj.name";
        FieldNameIterator iterator = new FieldNameIterator(given);

        assertTrue(iterator.hasNext());
        FieldNameIterator.Item actual = iterator.next();
        assertEquals("obj", actual.getValue());
        assertTrue(actual.isProperty());

        assertTrue(iterator.hasNext());
        actual = iterator.next();
        assertEquals("name", actual.getValue());
        assertTrue(actual.isProperty());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void name3() {
        String given = "$items";
        FieldNameIterator fieldNameIterator = new FieldNameIterator(given);

        FieldNameIterator.Item actual = fieldNameIterator.next();
        assertEquals("items", actual.getValue());
        assertTrue(actual.isCollection());
    }

    @Test
    public void name4() {
        String given = "$items.name";
        FieldNameIterator fieldNameIterator = new FieldNameIterator(given);

        FieldNameIterator.Item actual = fieldNameIterator.next();
        assertEquals("items", actual.getValue());
        assertTrue(actual.isCollection());

        actual = fieldNameIterator.next();
        assertEquals("name", actual.getValue());
        assertTrue(actual.isProperty());
    }

    @Test
    public void name5() {
        String given = "item.t:com.example.ClassName.name";
        FieldNameIterator fieldNameIterator = new FieldNameIterator(given);

        FieldNameIterator.Item actual = fieldNameIterator.next();
        assertEquals("item", actual.getValue());
        assertTrue(actual.isProperty());

        actual = fieldNameIterator.next();
        assertEquals("com.example.ClassName", actual.getValue());
        assertTrue(actual.isClass());

        actual = fieldNameIterator.next();
        assertEquals("name", actual.getValue());
        assertTrue(actual.isProperty());
    }
}
