package com.fyaora.profilemanagement.profileservice.service.impl;

import com.fyaora.profilemanagement.profileservice.dto.ServiceOfferedDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.ServiceOffered;
import com.fyaora.profilemanagement.profileservice.model.db.repository.ServicesOfferedRepository;
import com.fyaora.profilemanagement.profileservice.model.mapping.ServiceOfferedMapper;
import com.fyaora.profilemanagement.profileservice.service.ServicesOfferedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicesOfferedServiceImpl implements ServicesOfferedService {

    private final ServicesOfferedRepository servicesOfferedRepository;
    private final ServiceOfferedMapper serviceOfferedMapper;

    @Autowired
    public ServicesOfferedServiceImpl(ServicesOfferedRepository servicesOfferedRepository, ServiceOfferedMapper serviceOfferedMapper) {
        this.servicesOfferedRepository = servicesOfferedRepository;
        this.serviceOfferedMapper = serviceOfferedMapper;
    }

    @Override
    public List<ServiceOfferedDTO> getServices() {
        List<ServiceOffered> services = servicesOfferedRepository.findAll();
        return serviceOfferedMapper.toDtoList(services);
    }
}
