package by.nhorushko.filterspecification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Generic filter specification which holds {@link EnumMap} for
 * {@link FilterOperation} with Lambda functions <br>
 * <p>
 * Refer <br>
 * <table border="1">
 * <tr>
 * <td>Symbol</td>
 * <td>Operation</td>
 * <td>Example filter query param</td>
 * <tr>
 * <td>eq</td>
 * <td>Equals</td>
 * <td>city=eq#Sydney</td>
 * <tr>
 * <td>neq</td>
 * <td>Not Equals</td>
 * <td>country=neq#uk</td>
 * <tr>
 * <td>gt</td>
 * <td>Greater Than</td>
 * <td>amount=gt#10000</td>
 * <tr>
 * <td>gte</td>
 * <td>Greater Than or equals to</td>
 * <td>amount=gte#10000</td>
 * <tr>
 * <td>lt</td>
 * <td>Less Than</td>
 * <td>amount=lt#10000</td>
 * <tr>
 * <td>lte</td>
 * <td>Less Than or equals to</td>
 * <td>amount=lte#10000</td>
 * <tr>
 * <td>in</td>
 * <td>IN</td>
 * <td>country=in#uk, usa, au</td>
 * <tr>
 * <td>nin</td>
 * <td>Not IN</td>
 * <td>country=nin#fr, de, nz</td>
 * <tr>
 * <td>btn</td>
 * <td>Between</td>
 * <td>joiningDate=btn#2018-01-01, 2016-01-01</td>
 * <tr>
 * <td>like</td>
 * <td>Like</td>
 * <td>firstName=like#John</td>
 * </tr>
 * </table>
 * *
 *
 * @param <E>
 * @param <T>
 */
@Service
public class FilterSpecifications<E, T extends Comparable<T>> {

    private EnumMap<FilterOperation, Function<FilterCriteria<T>, Specification<E>>> map;

    public FilterSpecifications() {
        initSpecifications();
    }

    public Function<FilterCriteria<T>, Specification<E>> getSpecification(FilterOperation operation) {
        return map.get(operation);
    }

    /**
     * Forms the generic filter specifications for the operations
     * {@link FilterOperation}
     *
     * @return
     */
    private Map<FilterOperation, Function<FilterCriteria<T>, Specification<E>>> initSpecifications() {

        map = new EnumMap<>(FilterOperation.class);

        // Equal
        map.put(FilterOperation.EQUAL, filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .equal(getPath(root, filterCriteria.getFieldName(), criteriaBuilder), filterCriteria.getConvertedSingleValue()));

        map.put(FilterOperation.NOT_EQUAL, filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .notEqual(getPath(root, filterCriteria.getFieldName(), criteriaBuilder), filterCriteria.getConvertedSingleValue()));

        map.put(FilterOperation.GREATER_THAN,
                filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(
                        getPath(root, filterCriteria.getFieldName(),criteriaBuilder), filterCriteria.getConvertedSingleValue()));

        map.put(FilterOperation.GREATER_THAN_OR_EQUAL_TO,
                filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                        getPath(root, filterCriteria.getFieldName(), criteriaBuilder), filterCriteria.getConvertedSingleValue()));

        map.put(FilterOperation.LESS_THAN, filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .lessThan(getPath(root, filterCriteria.getFieldName(), criteriaBuilder), filterCriteria.getConvertedSingleValue()));

        map.put(FilterOperation.LESSTHAN_OR_EQUAL_TO,
                filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                        getPath(root, filterCriteria.getFieldName(), criteriaBuilder), filterCriteria.getConvertedSingleValue()));

        map.put(FilterOperation.IN, filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> getPath(root, filterCriteria.getFieldName(), criteriaBuilder).in(filterCriteria.getConvertedValues()));

        map.put(FilterOperation.NOT_IN, filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .not(getPath(root, filterCriteria.getFieldName(), criteriaBuilder).in(filterCriteria.getConvertedValues())));

        map.put(FilterOperation.BETWEEN,
                filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(
                        getPath(root, filterCriteria.getFieldName(), criteriaBuilder), filterCriteria.getMinValue(),
                        filterCriteria.getMaxValue()));

        map.put(FilterOperation.CONTAINS, filterCriteria -> (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder
                        .like(criteriaBuilder.lower(getPath(root, filterCriteria.getFieldName(), criteriaBuilder)), ("%" + filterCriteria.getConvertedSingleValue() + "%").toLowerCase()));

        map.put(FilterOperation.IS_NULL, filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .isNull(getPath(root, filterCriteria.getFieldName(), criteriaBuilder)));

        map.put(FilterOperation.NOT_NULL, filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .isNotNull(getPath(root, filterCriteria.getFieldName(), criteriaBuilder)));

        map.put(FilterOperation.ALWAYS_TRUE, filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction());

        return map;
    }

    /**
     * Resolves the path to a field of an entity based on a dot-separated string. This method
     * handles simple fields, nested fields, collection joins, and type-safe casting using the JPA Criteria API.
     *
     * <p>Examples of path strings:</p>
     * <ul>
     *   <li><b>Simple attribute:</b> "username"
     *       <p>Direct attribute of the root entity.</p>
     *       <p><code>root.get("username")</code></p>
     *   </li>
     *   <li><b>Nested attribute:</b> "user.address.street"
     *       <p>Navigates through nested entities: from 'user' to 'address', then to 'street'.</p>
     *       <p><code>root.get("user").get("address").get("street")</code></p>
     *   </li>
     *   <li><b>Collection elements:</b> "$employees.name"
     *       <p>Accesses elements of a collection 'employees' and retrieves the 'name' attribute.</p>
     *       <p><code>root.join("employees").get("name")</code></p>
     *   </li>
     *   <li><b>Polymorphic type casting:</b> "user.t:com.example.SpecialUser.specialAttribute"
     *       <p>Casts the 'user' path to 'SpecialUser' to access a specific attribute 'specialAttribute' available only in the subclass.</p>
     *       <p><code>criteriaBuilder.treat(root.get("user"), SpecialUser.class).get("specialAttribute")</code></p>
     *   </li>
     *   <li><b>Polymorphic type casting for collection elements:</b> "$employees.t:com.example.SpecialEmployee.specialSkill"
     *  *       <p>Accesses a collection 'employees', and casts each element to 'SpecialEmployee' to retrieve a specific attribute 'specialSkill' available only in the subclass.</p>
     *  *       <p><code>criteriaBuilder.treat(root.join("employees"), SpecialEmployee.class).get("specialSkill")</code></p>
     *  *   </li>
     * </ul>
     *
     * @param <Y> The type of the attribute at the end of the path.
     * @param root the root entity from which to start the path.
     * @param fieldName the dot-separated path string defining the field or nested fields to be accessed.
     * @param criteriaBuilder the CriteriaBuilder instance used for creating type-safe queries.
     * @return a Path object representing the specified entity attribute.
     * @throws IllegalArgumentException if a class specified for type casting cannot be found.
     */
    private <Y> Path<Y> getPath(Root<E> root, String fieldName, CriteriaBuilder criteriaBuilder) {
        String[] split = fieldName.split("\\.");
        Path<Y> p;
        if (isCollectionColumn(split[0])) {
            p = root.join(split[0].substring(1));
        } else {
            p = root.get(split[0]);
        }
        for (int i = 1; i < split.length; i++) {
            if (split[i].startsWith("t:")) {
                String className = split[i].substring(2);
                try {
                    Class<Y> type = (Class<Y>) Class.forName(className);
                    p = criteriaBuilder.treat(p, type);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Class not found for treat operation: " + className);
                }
            } else {
                p = p.get(split[i]);
            }
        }
        return p;
    }


    private boolean isCollectionColumn(String columnName) {
        return columnName.charAt(0) == FilterSpecificationConstants.COLLECTION_COLUMN_PREFIX;
    }
}
