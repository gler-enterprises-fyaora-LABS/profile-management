package com.fyaora.profilemanagement.profileservice.service.impl;

import com.fyaora.profilemanagement.profileservice.advice.ResourceNotFoundException;
import com.fyaora.profilemanagement.profileservice.model.enums.WaitlistProcess;
import com.fyaora.profilemanagement.profileservice.model.response.CustomerWaitlist;
import com.fyaora.profilemanagement.profileservice.model.request.WaitlistRequest;
import com.fyaora.profilemanagement.profileservice.model.request.WaitlistSearch;
import com.fyaora.profilemanagement.profileservice.model.enums.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Waitlist;
import com.fyaora.profilemanagement.profileservice.model.db.repository.WaitlistRepository;
import com.fyaora.profilemanagement.profileservice.model.db.mapper.CustomerWaitlistMapper;
import com.fyaora.profilemanagement.profileservice.service.WaitlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerWaitlistServiceImpl implements WaitlistService {

    private final WaitlistRepository waitlistRepository;
    private final CustomerWaitlistMapper customerWaitlistMapper;
    private final MessageSource messageSource;
    private final WaitlistSpecificationBuilder specificationBuilder;

    @Override
    public WaitlistProcess getProcess() {
        return WaitlistProcess.CUSTOMER;
    }

    @Override
    public void joinWaitlist(WaitlistRequest waitlistRequest) {
        if (waitlistRequest instanceof CustomerWaitlist customerRequestDTO) {
            Waitlist waitlist = customerWaitlistMapper.toEntity(customerRequestDTO);
            waitlist.setUserType(UserTypeEnum.CUSTOMER);
            waitlist.setEnabled(Boolean.TRUE);
            waitlist.setCreatedDatetime(LocalDateTime.now());
            waitlistRepository.save(waitlist);
        }
    }

    @Override
    public <T extends WaitlistRequest> List<T> searchWaitlist(WaitlistSearch waitlistSearch) {
        Specification<Waitlist> spec = specificationBuilder.build(waitlistSearch, UserTypeEnum.CUSTOMER);

        int pageNum = waitlistSearch.pageNum();
        int pageSize = waitlistSearch.pageSize();
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("id").ascending());

        Page<Waitlist> page = waitlistRepository.findAll(spec, pageable);
        List<CustomerWaitlist> list = customerWaitlistMapper.toDtoList(page.getContent());
        if (list.isEmpty()) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("customer.waitlist.requests.not.found", null, LocaleContextHolder.getLocale()));
        }
        return (List<T>) list;
    }
}
