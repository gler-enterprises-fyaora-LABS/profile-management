package com.fyaora.profilemanagement.profileservice.model.response;

import java.util.List;

public record InvestorWaitlistWrapper(
        List<InvestorWaitlist> results
) {
}
