package com.fyaora.profilemanagement.profileservice.service;

import com.fyaora.profilemanagement.profileservice.model.request.InquiryRequest;
import java.time.LocalDate;
import java.util.List;

public interface InquiryService {
    void addInquiry(final InquiryRequest inquiryRequest);
    List<InquiryRequest> viewInquiries(final LocalDate from, final LocalDate to, String email, final int pageNum, final int pageSize);
    String readInquiry(final Long id);
}
