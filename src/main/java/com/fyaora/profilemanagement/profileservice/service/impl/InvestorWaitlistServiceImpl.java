package com.fyaora.profilemanagement.profileservice.service.impl;

import com.fyaora.profilemanagement.profileservice.advice.ResourceNotFoundException;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistInvestorRequestDTO;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistProcess;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistRequestDTO;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistSearchDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Waitlist;
import com.fyaora.profilemanagement.profileservice.model.db.repository.WaitlistRepository;
import com.fyaora.profilemanagement.profileservice.model.mapping.InvestorWaitlistMapper;
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
public class InvestorWaitlistServiceImpl implements WaitlistService {

    @Value("${pagination.page.size}")
    private int pageSize;

    private final WaitlistRepository waitlistRepository;
    private final InvestorWaitlistMapper investorWaitlistMapper;
    private final MessageSource messageSource;

    @Autowired
    public InvestorWaitlistServiceImpl(WaitlistRepository repository,
                                       InvestorWaitlistMapper mapper,
                                       MessageSource messageSource) {
        this.waitlistRepository = repository;
        this.investorWaitlistMapper = mapper;
        this.messageSource = messageSource;
    }

    public WaitlistProcess getProcess() {
        return WaitlistProcess.INVESTOR;
    }

    @Override
    public void joinWaitlist(WaitlistRequestDTO requestDTO) {
        if (requestDTO instanceof WaitlistInvestorRequestDTO investorRequestDTO) {
            Waitlist waitlist = investorWaitlistMapper.toEntity(investorRequestDTO);
            waitlist.setUserType(UserTypeEnum.INVESTOR);
            waitlist.setEnabled(Boolean.TRUE);
            waitlistRepository.save(waitlist);
        }
    }

    @Override
    public <T extends WaitlistRequestDTO> List<T> searchWaitlist(WaitlistSearchDTO searchDTO) {
        int page = searchDTO.page() == null ? 0 : searchDTO.page();
        int size = pageSize;
        Pageable pageable = PageRequest.of(page, size);

        Page<Waitlist> list;

        if (StringUtils.isBlank(searchDTO.email()) && StringUtils.isBlank(searchDTO.telnum())) {
            list = waitlistRepository.findByUserType(UserTypeEnum.INVESTOR, pageable);
        } else {
            list = waitlistRepository.findByUserTypeAndEmailOrTelnum(UserTypeEnum.INVESTOR, searchDTO.email(), searchDTO.telnum(), pageable);
        }

        if (list.isEmpty()) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("investor.waitlist.requests.not.found", null, LocaleContextHolder.getLocale()));
        }

        List<WaitlistInvestorRequestDTO> dtoList = investorWaitlistMapper.toDtoList(list.getContent());
        return (List<T>) dtoList;
    }
}
