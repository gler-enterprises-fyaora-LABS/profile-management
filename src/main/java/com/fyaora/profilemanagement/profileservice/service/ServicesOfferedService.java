package com.fyaora.profilemanagement.profileservice.service;

import com.fyaora.profilemanagement.profileservice.dto.ServiceOfferedDTO;

import java.util.List;

public interface ServicesOfferedService {
    void addService(ServiceOfferedDTO serviceOfferedDTO);
    List<ServiceOfferedDTO> getServices();
}
