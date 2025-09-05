package com.fyaora.profilemanagement.profileservice.model.response;

import com.fyaora.profilemanagement.profileservice.model.request.InquiryRequest;
import java.util.List;

public record InquiryResponse(
        List<InquiryRequest> results
) { }
