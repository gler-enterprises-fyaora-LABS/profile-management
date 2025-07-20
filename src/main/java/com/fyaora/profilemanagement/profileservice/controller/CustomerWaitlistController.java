package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.dto.MessageDTO;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistCustomerRequestDTO;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistProcess;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistSearchDTO;
import com.fyaora.profilemanagement.profileservice.service.WaitlistService;
import com.fyaora.profilemanagement.profileservice.service.WaitlistServiceFactory;
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
@RequestMapping("/api/v1/waitlist/customer")
public class CustomerWaitlistController {

    private final MessageSource messageSource;
    private final WaitlistServiceFactory waitlistServiceFactory;

    @Autowired
    public CustomerWaitlistController(MessageSource messageSource,
                                      WaitlistServiceFactory factory) {
        this.messageSource = messageSource;
        this.waitlistServiceFactory = factory;
    }

    @Operation(
            summary = "Add customer waitlist request",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully added customer waitlist request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = WaitlistCustomerRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
            }
    )
    @PostMapping("/join")
    public ResponseEntity<?> joinWaitList(@Valid @RequestBody WaitlistCustomerRequestDTO waitListRequestDTO) {
        WaitlistService service = waitlistServiceFactory.getService(WaitlistProcess.CUSTOMER);
        service.joinWaitlist(waitListRequestDTO);
        return ResponseEntity.ok(
                messageSource.getMessage("customer.join.waitlist.success", null, LocaleContextHolder.getLocale()));
    }

    @Operation(
            summary = "Search customer waitlist requests",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Search customer waitlist requests is success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = WaitlistCustomerRequestDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
            }
    )
    @GetMapping("/search")
    public ResponseEntity<?> searchWaitlist(@RequestBody WaitlistSearchDTO searchDTO) {
        WaitlistService service = waitlistServiceFactory.getService(WaitlistProcess.CUSTOMER);
        List<WaitlistCustomerRequestDTO> list = service.searchWaitlist(searchDTO);
        return ResponseEntity.ok(list);
    }
}
