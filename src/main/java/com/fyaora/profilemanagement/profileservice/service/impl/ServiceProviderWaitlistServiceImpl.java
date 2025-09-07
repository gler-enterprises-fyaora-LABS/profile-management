package com.fyaora.profilemanagement.profileservice.service.impl;

import com.fyaora.profilemanagement.profileservice.advice.ResourceNotFoundException;
import com.fyaora.profilemanagement.profileservice.model.enums.WaitlistProcess;
import com.fyaora.profilemanagement.profileservice.model.request.WaitlistRequest;
import com.fyaora.profilemanagement.profileservice.model.request.WaitlistSearch;
import com.fyaora.profilemanagement.profileservice.model.response.ServiceProviderWaitlist;
import com.fyaora.profilemanagement.profileservice.model.db.entity.ServiceOffered;
import com.fyaora.profilemanagement.profileservice.model.enums.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Waitlist;
import com.fyaora.profilemanagement.profileservice.model.db.entity.WaitlistServiceOffered;
import com.fyaora.profilemanagement.profileservice.model.db.repository.ServicesOfferedRepository;
import com.fyaora.profilemanagement.profileservice.model.db.repository.WaitlistRepository;
import com.fyaora.profilemanagement.profileservice.model.db.repository.WaitlistServiceRepository;
import com.fyaora.profilemanagement.profileservice.model.db.mapper.ServiceProviderWaitlistMapper;
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
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceProviderWaitlistServiceImpl implements WaitlistService {

    private final WaitlistRepository waitlistRepository;
    private final ServicesOfferedRepository servicesOfferedRepository;
    private final WaitlistServiceRepository waitlistServiceRepository;
    private final ServiceProviderWaitlistMapper serviceProviderWaitlistMapper;
    private final MessageSource messageSource;
    private final WaitlistSpecificationBuilder specificationBuilder;

    @Override
    public WaitlistProcess getProcess() {
        return WaitlistProcess.SERVICE_PROVIDER;
    }

    @Override
    @Transactional
    public void joinWaitlist(WaitlistRequest waitlistRequest) {
        if (waitlistRequest instanceof ServiceProviderWaitlist serviceProviderRequestDTO) {
            Waitlist waitlist = serviceProviderWaitlistMapper.toEntity(serviceProviderRequestDTO);
            waitlist.setUserType(UserTypeEnum.SERVICE_PROVIDER);
            waitlist.setCreatedDatetime(LocalDateTime.now());
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
    public <T extends WaitlistRequest> List<T> searchWaitlist(WaitlistSearch waitlistSearch) {
        Specification<Waitlist> spec = specificationBuilder.build(waitlistSearch, UserTypeEnum.SERVICE_PROVIDER);

        int pageNum = waitlistSearch.pageNum();
        int pageSize = waitlistSearch.pageSize();
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("id").ascending());

        Page<Waitlist> page = waitlistRepository.findAll(spec, pageable);
        List<ServiceProviderWaitlist> list = serviceProviderWaitlistMapper.toDtoList(page.getContent());
        if (list.isEmpty()) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("service.provider.waitlist.requests.not.found", null, LocaleContextHolder.getLocale()));
        }
        return (List<T>) list;
    }
}
