package com.assignment.inventoryservice.specfication;

import com.assignment.inventoryservice.entity.InventoryEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class InventorySpecificationsBuilder {
    private final List<SpecSearchCriteria> params;

    public InventorySpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public final InventorySpecificationsBuilder with(String key, String operation, Object value,
                                                     String prefix, String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public final InventorySpecificationsBuilder with(String orPredicate, String key, String operation,
                                                     Object value, String prefix, String suffix) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) { // the operation may be complex operation
                boolean startWithAsterisk = prefix != null &&
                prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                boolean endWithAsterisk = suffix != null &&
                        suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(orPredicate, key, op, value));
        }
        return this;
    }

    public Specification<InventoryEntity> build() {
        if (params.size() == 0)
            return null;

        Specification result = new InventorySpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new InventorySpecification(params.get(i)))
                    : Specification.where(result).and(new InventorySpecification(params.get(i)));
        }

        return result;
    }
}
