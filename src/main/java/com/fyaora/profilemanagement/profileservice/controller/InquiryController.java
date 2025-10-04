package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.model.request.InquiryRequest;
import com.fyaora.profilemanagement.profileservice.model.response.InquiryResponse;
import com.fyaora.profilemanagement.profileservice.model.response.MessageDTO;
import com.fyaora.profilemanagement.profileservice.model.response.ResponseDTO;
import com.fyaora.profilemanagement.profileservice.service.InquiryService;
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
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/v1/inquiry")
public class InquiryController {

    private final MessageSource messageSource;
    private final InquiryService inquiryService;

    @Autowired
    public InquiryController(MessageSource messageSource, InquiryService inquiryService) {
        this.messageSource = messageSource;
        this.inquiryService = inquiryService;
    }

    @Operation(
            summary = "Add inquiry",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully added iquiry", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
            }
    )
    @PostMapping
    public ResponseEntity<ResponseDTO> addInquiry(@Valid @RequestBody InquiryRequest inquiryRequest) {
        inquiryService.addInquiry(inquiryRequest);
        String message = messageSource.getMessage("inquiry.add.success", null, LocaleContextHolder.getLocale());
        ResponseDTO response = new ResponseDTO(message);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "View inquiries",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Return inquiry list", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InquiryResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
            }
    )
    @GetMapping
    public ResponseEntity<InquiryResponse> viewInquiry(
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
            @RequestParam(value = "pageNum", defaultValue = "0") @Min(0) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {
        List<InquiryRequest> results = inquiryService.viewInquiries(from, to, email, pageNum, pageSize);
        InquiryResponse response = new InquiryResponse(results);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Read full inquiry message",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Return inquiry message", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
            }
    )
    @GetMapping("/msg")
    public ResponseEntity<ResponseDTO> readInquiry(@RequestParam(value = "id") long id) {
        String msg = inquiryService.readInquiry(id);
        ResponseDTO response = new ResponseDTO(msg);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
