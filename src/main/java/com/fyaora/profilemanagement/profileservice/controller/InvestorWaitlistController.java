package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.model.request.InvestorWaitlist;
import com.fyaora.profilemanagement.profileservice.model.response.InvestorWaitlistWrapper;
import com.fyaora.profilemanagement.profileservice.model.response.MessageDTO;
import com.fyaora.profilemanagement.profileservice.model.response.ResponseDTO;
import com.fyaora.profilemanagement.profileservice.model.enums.WaitlistProcess;
import com.fyaora.profilemanagement.profileservice.model.request.WaitlistSearch;
import com.fyaora.profilemanagement.profileservice.service.WaitlistService;
import com.fyaora.profilemanagement.profileservice.service.WaitlistServiceFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
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

    @Operation(
            summary = "Add investor waitlist request",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully added investor waitlist request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
            }
    )
    @PostMapping("/join")
    public ResponseEntity<ResponseDTO> joinWaitlist(@Valid @RequestBody InvestorWaitlist investorWaitlist) {
        WaitlistService service = waitlistServiceFactory.getService(WaitlistProcess.INVESTOR);
        service.joinWaitlist(investorWaitlist);
        String message = messageSource.getMessage("investor.join.waitlist.success", null, LocaleContextHolder.getLocale());
        ResponseDTO response = new ResponseDTO(message);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Search investor waitlist requests",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Search investor waitlist requests is success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvestorWaitlistWrapper.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
            }
    )
    @GetMapping("/search")
    public ResponseEntity<InvestorWaitlistWrapper> searchWaitlist(
            @Parameter(
                    description = "Start date (format: YYYY-MM-DD)"
            )
            @RequestParam(value = "from", required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(
                    description = "End date (format: YYYY-MM-DD)"
            )
            @RequestParam(value = "to", required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "telnum", required = false) String telnum,
            @RequestParam(value = "pageNum", defaultValue = "0") @Min(0) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize
    ) {
        WaitlistSearch waitlistSearch =
                WaitlistSearch.builder()
                        .from(from)
                        .to(to)
                        .email(email)
                        .telnum(telnum)
                        .pageNum(pageNum)
                        .pageSize(pageSize)
                        .build();
        WaitlistService service = waitlistServiceFactory.getService(WaitlistProcess.INVESTOR);
        List<InvestorWaitlist> list = service.searchWaitlist(waitlistSearch);
        InvestorWaitlistWrapper response = new InvestorWaitlistWrapper(list);
        return ResponseEntity.ok(response);
    }
}
