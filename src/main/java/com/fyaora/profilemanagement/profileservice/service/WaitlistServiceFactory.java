package com.fyaora.profilemanagement.profileservice.service;

import com.fyaora.profilemanagement.profileservice.model.enums.WaitlistProcess;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WaitlistServiceFactory {
    private final List<WaitlistService> services;

    public WaitlistServiceFactory(List<WaitlistService> services) {
        this.services = services;
    }

    public WaitlistService getService(WaitlistProcess process) {
        return services.stream().filter(service -> service.getProcess().equals(process)).findFirst().get();
    }
}
