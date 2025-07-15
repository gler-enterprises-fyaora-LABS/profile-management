package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.dto.ServiceOfferedDTO;
import com.fyaora.profilemanagement.profileservice.service.ServicesOfferedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/waitlist/services")
public class ServiceOfferedController {

    private final ServicesOfferedService servicesOfferedService;

    @Autowired
    public ServiceOfferedController(ServicesOfferedService servicesOfferedService) {
        this.servicesOfferedService = servicesOfferedService;
    }

    @GetMapping
    public ResponseEntity<?> getServices() {
        List<ServiceOfferedDTO> services = servicesOfferedService.getServices();
        return ResponseEntity.ok(services);
    }
}
