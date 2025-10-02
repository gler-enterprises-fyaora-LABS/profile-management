package com.fyaora.profilemanagement.profileservice.service.impl;

import com.fyaora.profilemanagement.profileservice.advice.ResourceNotFoundException;
import com.fyaora.profilemanagement.profileservice.model.request.InquiryRequest;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Inquiry;
import com.fyaora.profilemanagement.profileservice.model.db.repository.InquiryRepository;
import com.fyaora.profilemanagement.profileservice.model.db.mapper.InquiryMapper;
import com.fyaora.profilemanagement.profileservice.service.InquiryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InquiryServiceImpl implements InquiryService {

    private final InquiryMapper inquiryMapper;
    private final InquiryRepository inquiryRepository;
    private final MessageSource messageSource;

    @Autowired
    public InquiryServiceImpl(final InquiryMapper inquiryMapper, final InquiryRepository inquiryRepository,
                              final MessageSource messageSource) {
        this.inquiryMapper = inquiryMapper;
        this.inquiryRepository = inquiryRepository;
        this.messageSource = messageSource;
    }

    @Override
    public void addInquiry(final InquiryRequest inquiryRequest) {
        Inquiry inquiry = inquiryMapper.toEntity(inquiryRequest);
        inquiry.setEnabled(Boolean.TRUE);
        inquiry.setCreatedDatetime(LocalDateTime.now());
        inquiryRepository.save(inquiry);
    }

    @Override
    public List<InquiryRequest> viewInquiries(
            final LocalDate from, final LocalDate to, String email,
            final int pageNum, final int pageSize) {

        Specification<Inquiry> spec = ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if (null != from) {
            LocalDateTime startDate = from.atStartOfDay();
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("createdDatetime"), startDate));
        }

        if (null != to) {
            LocalDateTime endDate = to.atTime(23, 59, 59);
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("createdDatetime"), endDate));
        }

        if (StringUtils.isNotBlank(email)) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email));
        }

        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("id").ascending());

        Page<Inquiry> inquiries = inquiryRepository.findAll(spec, pageable);
        List<InquiryRequest> results = inquiryMapper.toDtoList(inquiries.getContent());
        if (results.isEmpty()) {
            String message = messageSource.getMessage("inquiries.not.found", null, LocaleContextHolder.getLocale());
            throw new ResourceNotFoundException(message);
        }
        return results;
    }

    @Override
    public String readInquiry(final Long id) {
        Inquiry inquiry = inquiryRepository.findById(id).orElse(null);
        if (null != inquiry) {
            return  inquiry.getMessage();
        } else {
            throw new ResourceNotFoundException(String.format("Inquiry not found with id: %s", id));
        }
    }

    private Specification<Inquiry> createdAfter(LocalDateTime from) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("createdDatetime"), from);
    }

    private Specification<Inquiry> createdBefore(LocalDateTime to) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("createdDatetime"), to);
    }
}
