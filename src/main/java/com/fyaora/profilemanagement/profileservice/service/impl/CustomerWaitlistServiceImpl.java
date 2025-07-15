package com.fyaora.profilemanagement.profileservice.service.impl;

import com.fyaora.profilemanagement.profileservice.dto.WaitlistProcess;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistCustomerRequestDTO;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistRequestDTO;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistSearchDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Waitlist;
import com.fyaora.profilemanagement.profileservice.model.db.repository.WaitlistRepository;
import com.fyaora.profilemanagement.profileservice.model.mapping.CustomerWaitlistMapper;
import com.fyaora.profilemanagement.profileservice.service.WaitlistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CustomerWaitlistServiceImpl implements WaitlistService {

    private final WaitlistRepository waitlistRepository;
    private final CustomerWaitlistMapper customerWaitlistMapper;

    @Autowired
    public CustomerWaitlistServiceImpl(WaitlistRepository repository, CustomerWaitlistMapper mapper) {
        this.waitlistRepository = repository;
        this.customerWaitlistMapper = mapper;
    }

    @Override
    public WaitlistProcess getProcess() {
        return WaitlistProcess.CUSTOMER;
    }

    @Override
    public void joinWaitlist(WaitlistRequestDTO requestDTO) {
        if (requestDTO instanceof WaitlistCustomerRequestDTO customerRequestDTO) {
            Waitlist waitlist = customerWaitlistMapper.toEntity(customerRequestDTO);
            waitlist.setUserType(UserTypeEnum.CUSTOMER);
            waitlist.setEnabled(Boolean.TRUE);
            waitlistRepository.save(waitlist);
        }
    }

    @Override
    public <T extends WaitlistRequestDTO> List<T> searchWaitlist(WaitlistSearchDTO searchDTO) {
        List<Waitlist> list = waitlistRepository
                .findByUserTypeAndEmailOrTelnum(UserTypeEnum.CUSTOMER, searchDTO.email(), searchDTO.telnum());
        List<WaitlistCustomerRequestDTO> requests = customerWaitlistMapper.toDtoList(list);
        return (List<T>) requests;
    }
}
