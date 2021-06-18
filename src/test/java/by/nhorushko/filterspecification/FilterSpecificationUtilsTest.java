package by.nhorushko.filterspecification;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Set;

import static org.junit.Assert.*;

public class FilterSpecificationUtilsTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void getValue() {
        assertEquals("123", FilterSpecificationUtils.getValue("eq#123"));
        assertEquals("1,2,3", FilterSpecificationUtils.getValue("in#1,2,3"));
    }

    @Test
    public void getOperation() {
        assertEquals(FilterOperation.EQUAL, FilterSpecificationUtils.getOperation("eq#123"));
        assertEquals(FilterOperation.IN, FilterSpecificationUtils.getOperation("in#1,2,3"));
    }

    @Test
    public void checkFilterOperation() {
        assertTrue(FilterSpecificationUtils.checkFilterOperation("eq#123", Set.of(FilterOperation.IN, FilterOperation.EQUAL)));
        assertFalse(FilterSpecificationUtils.checkFilterOperation("eq#123", Set.of(FilterOperation.IN)));
    }

    @Test
    public void checkFilterOperation2() {
        assertTrue(FilterSpecificationUtils.checkFilterOperation("eq#123", FilterOperation.EQUAL));
        assertFalse(FilterSpecificationUtils.checkFilterOperation("eq#123", FilterOperation.IN));
    }

    @Test
    public void buildFilter() {
        String actual = FilterSpecificationUtils.buildFilter(FilterOperation.IN, "1", "2", "3");
        assertEquals("in#1,2,3", actual);

        actual = FilterSpecificationUtils.buildFilter(FilterOperation.EQUAL, 1);
        assertEquals("eq#1", actual);

        actual = FilterSpecificationUtils.buildFilter(FilterOperation.BETWEEN, 1, 2);
        assertEquals("btn#1,2", actual);
    }

    @Test
    public void buildFilter_empty() {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("can't be build filter params length is 0");

        FilterSpecificationUtils.buildFilter(FilterOperation.IN);
    }

    @Test
    public void buildFilter_expectTwoValues() {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("can't build filter for operation: btn expect 2 params, but: 3");

        FilterSpecificationUtils.buildFilter(FilterOperation.BETWEEN, 1, 2, 3);
    }

    @Test
    public void buildFilter_expectOneValue() {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("can't build filter for operation: eq expect 1 params, but: 2");

        FilterSpecificationUtils.buildFilter(FilterOperation.EQUAL, 1, 2);
    }
}
