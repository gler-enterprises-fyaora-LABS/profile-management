package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.dto.WaitlistInvestorRequestDTO;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistProcess;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistSearchDTO;
import com.fyaora.profilemanagement.profileservice.service.WaitlistService;
import com.fyaora.profilemanagement.profileservice.service.WaitlistServiceFactory;
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
@RequestMapping("/api/v1/waitlist/investor")
public class InvestorWaitlistController {

    private final MessageSource messageSource;
    private final WaitlistServiceFactory waitlistServiceFactory;

    @Autowired
    public InvestorWaitlistController(MessageSource messageSource,
                                      WaitlistServiceFactory factory) {
        this.messageSource = messageSource;
        this.waitlistServiceFactory = factory;
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinWaitlist(@Valid @RequestBody WaitlistInvestorRequestDTO investorRequestDTO) {
        WaitlistService service = waitlistServiceFactory.getService(WaitlistProcess.INVESTOR);
        service.joinWaitlist(investorRequestDTO);
        return ResponseEntity.ok(
                messageSource.getMessage("investor.join.waitlist.success", null, LocaleContextHolder.getLocale()));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchWaitlist(@RequestBody WaitlistSearchDTO searchDTO) {
        WaitlistService service = waitlistServiceFactory.getService(WaitlistProcess.INVESTOR);
        List<WaitlistInvestorRequestDTO> list = service.searchWaitlist(searchDTO);
        return ResponseEntity.ok(list);
    }
}
