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

    public static String getValue(String filter) {
        return filter.split(FilterSpecificationConstants.FIELD_FILTER)[1];
    }

    public static FilterOperation getOperation(String filter) {
        return FilterOperation.fromValue(filter.split(FilterSpecificationConstants.FIELD_FILTER)[0]);
    }

    public static boolean checkFilterOperation(String filter, Set<FilterOperation> availableOperations) {
        return availableOperations.contains(getOperation(filter));
    }

    public static boolean checkFilterOperation(String filter, FilterOperation availableOperation) {
        return getOperation(filter).equals(availableOperation);
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
