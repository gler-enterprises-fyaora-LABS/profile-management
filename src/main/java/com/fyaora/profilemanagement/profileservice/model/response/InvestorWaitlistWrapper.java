package com.fyaora.profilemanagement.profileservice.model.response;

import com.fyaora.profilemanagement.profileservice.model.request.InvestorWaitlist;
import java.util.List;

public record InvestorWaitlistWrapper(
        List<InvestorWaitlist> results
) {
}
