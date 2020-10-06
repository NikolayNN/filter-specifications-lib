package by.nhorushko.filterspecification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.util.function.Function;

public abstract class FilterSpecificationAbstract<ENTITY> {

    /**
     * Converter Functions
     */
    @Autowired
    protected Converters converters;

    /**
     * Generic method to return {@link Specification} for Entity {@link ENTITY}
     *
     * @param fieldName
     * @param filterValue
     * @param converter
     * @param specifications
     * @return
     */
    protected <T extends Comparable<T>> Specification<ENTITY> getSpecification(String fieldName,
                                                                               String filterValue,
                                                                               Function<String, T> converter,
                                                                               FilterSpecifications<ENTITY, T> specifications) {
        if (StringUtils.isNotBlank(filterValue)) {
            //Form the filter Criteria
            FilterCriteria<T> criteria = new FilterCriteria<>(fieldName, filterValue, converter);
            return specifications.getSpecification(criteria.getOperation()).apply(criteria);
        }
        return null;
    }
}
