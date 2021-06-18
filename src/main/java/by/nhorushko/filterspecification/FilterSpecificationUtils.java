package by.nhorushko.filterspecification;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class FilterSpecificationUtils {

    private static final Set<FilterOperation> multipleOperations = Set.of(
            FilterOperation.IN,
            FilterOperation.NOT_IN);

    private static final Set<FilterOperation> twoValueOperations = Set.of(
            FilterOperation.BETWEEN);

    public static boolean isBlank(String filter) {
        return filter == null || filter.trim().length() == 0 || filter.split(FilterSpecificationConstants.FIELD_FILTER).length != 2;

    }

    /**
     * @throws NullPointerException if filter null
     */
    public static String[] getAllValues(String filter) throws NullPointerException {
        return filter.split(FilterSpecificationConstants.FIELD_FILTER)[1].split(FilterSpecificationConstants.VALUES);
    }

    /**
     * @throws NullPointerException if filter null
     */
    public static String getFirstValue(String filter) throws NullPointerException {
        return filter.split(FilterSpecificationConstants.FIELD_FILTER)[1].split(FilterSpecificationConstants.VALUES)[0];
    }

    /**
     * @throws NullPointerException if filter null
     */
    public static long getFirstLongValue(String filter) throws NullPointerException {
        return Long.parseLong(filter.split(FilterSpecificationConstants.FIELD_FILTER)[1].split(FilterSpecificationConstants.VALUES)[0]);
    }


    public static FilterOperation getOperation(String filter) {
        return FilterOperation.fromValue(filter.split(FilterSpecificationConstants.FIELD_FILTER)[0]);
    }

    public static boolean checkFilterOperation(String filter, Set<FilterOperation> availableOperations) {
        return filter == null || availableOperations.contains(getOperation(filter));
    }

    public static String buildFilter(FilterOperation operation, Object... params) {
        if (params.length == 0) {
            throw new RuntimeException("can't be build filter params length is 0");
        }
        if (multipleOperations.contains(operation)) {
            return build(operation, params);
        }
        if (twoValueOperations.contains(operation)) {
            return build(operation, 2, params);
        }
        return build(operation, 1, params);
    }

    private static String build(FilterOperation operation, int expectSize, Object... params) {
        if (params.length != expectSize) {
            throwException(operation, expectSize, params.length);
        }
        return build(operation, params);
    }

    private static void throwException(FilterOperation operation, int expectSize, int actual) {
        throw new RuntimeException(
                String.format("can't build filter for operation: %s expect %s params, but: %s ", operation, expectSize, actual));
    }

    private static String build(FilterOperation operation, Object[] params) {
        return operation.toString() + FilterSpecificationConstants.FIELD_FILTER +
                Arrays.stream(params).map(p -> p.toString()).collect(Collectors.joining(FilterSpecificationConstants.VALUES));
    }
}
