package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.dto.MessageDTO;
import com.fyaora.profilemanagement.profileservice.dto.ResponseDTO;
import com.fyaora.profilemanagement.profileservice.dto.ServiceOfferedDTO;
import com.fyaora.profilemanagement.profileservice.service.ServicesOfferedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/waitlist/services")
public class ServiceOfferedController {

    private final ServicesOfferedService servicesOfferedService;
    private final MessageSource messageSource;

    @Autowired
    public ServiceOfferedController(
            ServicesOfferedService servicesOfferedService,
            MessageSource messageSource) {
        this.servicesOfferedService = servicesOfferedService;
        this.messageSource = messageSource;
    }

    @Operation(
            summary = "Add service",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully added service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
            }
    )
    @PostMapping
    public ResponseEntity<?> addService(@RequestBody @Valid ServiceOfferedDTO serviceOfferedDTO) {
        servicesOfferedService.addService(serviceOfferedDTO);
        String message = messageSource.getMessage("service.add.success", null, LocaleContextHolder.getLocale());
        ResponseDTO response = new ResponseDTO(message);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Search services",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Search service is success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceOfferedDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
            }
    )
    @GetMapping
    public ResponseEntity<?> getServices() {
        List<ServiceOfferedDTO> services = servicesOfferedService.getServices();
        return ResponseEntity.ok(services);
    }
}
