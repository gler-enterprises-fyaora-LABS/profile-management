package com.fyaora.profilemanagement.profileservice.service.impl;

import com.fyaora.profilemanagement.profileservice.advice.ResourceNotFoundException;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistProcess;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistRequestDTO;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistSearchDTO;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistServiceProviderRequestDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.ServiceOffered;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Waitlist;
import com.fyaora.profilemanagement.profileservice.model.db.entity.WaitlistServiceOffered;
import com.fyaora.profilemanagement.profileservice.model.db.repository.ServicesOfferedRepository;
import com.fyaora.profilemanagement.profileservice.model.db.repository.WaitlistRepository;
import com.fyaora.profilemanagement.profileservice.model.db.repository.WaitlistServiceRepository;
import com.fyaora.profilemanagement.profileservice.model.mapping.ServiceProviderWaitlistMapper;
import com.fyaora.profilemanagement.profileservice.service.WaitlistService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServiceProviderWaitlistServiceImpl implements WaitlistService {

    @Value("${pagination.page.size}")
    private int pageSize;

    private final WaitlistRepository waitlistRepository;
    private final ServicesOfferedRepository servicesOfferedRepository;
    private final WaitlistServiceRepository waitlistServiceRepository;
    private final ServiceProviderWaitlistMapper serviceProviderWaitlistMapper;
    private final MessageSource messageSource;

    @Autowired
    public ServiceProviderWaitlistServiceImpl(WaitlistRepository repository,
                                              ServicesOfferedRepository servicesOfferedRepository,
                                              WaitlistServiceRepository waitlistServiceRepository,
                                              ServiceProviderWaitlistMapper mapper,
                                              MessageSource messageSource) {
        this.waitlistRepository = repository;
        this.servicesOfferedRepository = servicesOfferedRepository;
        this.waitlistServiceRepository = waitlistServiceRepository;
        this.serviceProviderWaitlistMapper = mapper;
        this.messageSource = messageSource;
    }

    @Override
    public WaitlistProcess getProcess() {
        return WaitlistProcess.SERVICE_PROVIDER;
    }

    @Override
    @Transactional
    public void joinWaitlist(WaitlistRequestDTO requestDTO) {
        if (requestDTO instanceof WaitlistServiceProviderRequestDTO serviceProviderRequestDTO) {
            Waitlist waitlist = serviceProviderWaitlistMapper.toEntity(serviceProviderRequestDTO);
            waitlist.setUserType(UserTypeEnum.SERVICE_PROVIDER);
            waitlist.setEnabled(Boolean.TRUE);

            List<ServiceOffered> serviceOffereds = servicesOfferedRepository.findAllById(serviceProviderRequestDTO.servicesOffered());
            Set<Integer> foundServices = serviceOffereds.stream().map(ServiceOffered::getId).collect(Collectors.toSet());

            List<Integer> filteredNonServices =
                    serviceProviderRequestDTO.servicesOffered().stream()
                            .filter(id -> !foundServices.contains(id))
                            .collect(Collectors.toList());

            if (!filteredNonServices.isEmpty()) {
                throw new ResourceNotFoundException(
                        messageSource.getMessage("service.offered.not.found", new Object[]{filteredNonServices}, LocaleContextHolder.getLocale()));
            }

            List<WaitlistServiceOffered> waitlistServiceOffereds = new ArrayList<>();

            serviceOffereds.stream().forEach(s -> {
                WaitlistServiceOffered wService =
                        WaitlistServiceOffered.builder()
                                .service(s)
                                .waitlist(waitlist)
                                .build();

                waitlistServiceOffereds.add(wService);
            });

            waitlistRepository.save(waitlist);
            waitlistServiceRepository.saveAll(waitlistServiceOffereds);
        }
    }

    @Override
    public <T extends WaitlistRequestDTO> List<T> searchWaitlist(WaitlistSearchDTO searchDTO) {
        int page = searchDTO.page() == null ? 0 : searchDTO.page();
        int size = pageSize;
        Pageable pageable = PageRequest.of(page, size);

        Page<Waitlist> list;
        if (StringUtils.isBlank(searchDTO.email()) && StringUtils.isBlank(searchDTO.telnum())) {
            list = waitlistRepository.findByUserType(UserTypeEnum.SERVICE_PROVIDER, pageable);
        } else {
            list = waitlistRepository.
                    findByUserTypeAndEmailOrTelnum(UserTypeEnum.SERVICE_PROVIDER, searchDTO.email(), searchDTO.telnum(), pageable);
        }

        if (list.isEmpty()) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("service.provider.waitlist.requests.not.found", null, LocaleContextHolder.getLocale()));
        }

        List<WaitlistServiceProviderRequestDTO> dtoList = serviceProviderWaitlistMapper.toDtoList(list.getContent());
        return (List<T>) dtoList;
    }
}
