package com.fyaora.profilemanagement.profileservice.service.impl;

import com.fyaora.profilemanagement.profileservice.advice.ResourceNotFoundException;
import com.fyaora.profilemanagement.profileservice.dto.ServiceOfferedDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.ServiceOffered;
import com.fyaora.profilemanagement.profileservice.model.db.repository.ServicesOfferedRepository;
import com.fyaora.profilemanagement.profileservice.model.mapping.ServiceOfferedMapper;
import com.fyaora.profilemanagement.profileservice.service.ServicesOfferedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicesOfferedServiceImpl implements ServicesOfferedService {

    private final ServicesOfferedRepository servicesOfferedRepository;
    private final ServiceOfferedMapper serviceOfferedMapper;
    private final MessageSource messageSource;

    @Autowired
    public ServicesOfferedServiceImpl(ServicesOfferedRepository servicesOfferedRepository,
                                      ServiceOfferedMapper serviceOfferedMapper,
                                      MessageSource messageSource) {
        this.servicesOfferedRepository = servicesOfferedRepository;
        this.serviceOfferedMapper = serviceOfferedMapper;
        this.messageSource = messageSource;
    }

    @Override
    public void addService(ServiceOfferedDTO serviceOfferedDTO) {
        ServiceOffered serviceOffered = serviceOfferedMapper.toEntity(serviceOfferedDTO);
        servicesOfferedRepository.save(serviceOffered);
    }

    @Override
    public List<ServiceOfferedDTO> getServices() {
        List<ServiceOffered> services = servicesOfferedRepository.findAll();
        if (services.isEmpty()) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("services.not.found", null, LocaleContextHolder.getLocale()));
        }
        return serviceOfferedMapper.toDtoList(services);
    }
}
