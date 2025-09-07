package com.fyaora.profilemanagement.profileservice.service.impl;

import com.fyaora.profilemanagement.profileservice.model.db.entity.Waitlist;
import com.fyaora.profilemanagement.profileservice.model.enums.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.model.request.WaitlistSearch;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class WaitlistSpecificationBuilder {

    public Specification<Waitlist> build(WaitlistSearch search, UserTypeEnum userType) {
        Specification<Waitlist> spec =
                (root, query, cb) -> cb.equal(root.get("userType"), userType);

        if (search.from() != null) {
            LocalDateTime fromDate = search.from().atStartOfDay();
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdDatetime"), fromDate));
        }

        if (search.to() != null) {
            LocalDateTime toDate = search.to().atTime(23, 59, 59);
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdDatetime"), toDate));
        }

        if (search.email() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("email"), search.email()));
        }

        if (search.telnum() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("telnum"), search.telnum()));
        }

        return spec;
    }
}
