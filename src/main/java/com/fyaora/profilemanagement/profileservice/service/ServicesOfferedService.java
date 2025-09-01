package com.fyaora.profilemanagement.profileservice.service;

import com.fyaora.profilemanagement.profileservice.model.request.ServiceOfferedRequest;

import java.util.List;

public interface ServicesOfferedService {
    void addService(ServiceOfferedRequest serviceOfferedRequest);
    List<ServiceOfferedRequest> getServices();
}
