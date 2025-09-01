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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CustomerWaitlistServiceImpl implements WaitlistService {

    @Value("${pagination.page.size}")
    private int pageSize;

    private final WaitlistRepository waitlistRepository;
    private final CustomerWaitlistMapper customerWaitlistMapper;
    private final MessageSource messageSource;

    @Autowired
    public CustomerWaitlistServiceImpl(WaitlistRepository repository,
                                       CustomerWaitlistMapper mapper,
                                       MessageSource messageSource) {
        this.waitlistRepository = repository;
        this.customerWaitlistMapper = mapper;
        this.messageSource = messageSource;
    }

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
            waitlistRepository.save(waitlist);
        }
    }

    @Override
    public <T extends WaitlistRequest> List<T> searchWaitlist(WaitlistSearch waitlistSearch) {
        int page = waitlistSearch.page() == null ? 0 : waitlistSearch.page();
        int size = pageSize;
        Pageable pageable = PageRequest.of(page, size);

        Page<Waitlist> list;

        if (StringUtils.isBlank(waitlistSearch.email()) && StringUtils.isBlank(waitlistSearch.telnum())) {
            list = waitlistRepository.findByUserType(UserTypeEnum.CUSTOMER, pageable);
        } else {
            list = waitlistRepository.findByUserTypeAndEmailOrTelnum(UserTypeEnum.CUSTOMER, waitlistSearch.email(), waitlistSearch.telnum(), pageable);
        }

        if (list.isEmpty()) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("customer.waitlist.requests.not.found", null, LocaleContextHolder.getLocale()));
        }

        List<CustomerWaitlist> requests = customerWaitlistMapper.toDtoList(list.getContent());
        return (List<T>) requests;
    }
}
