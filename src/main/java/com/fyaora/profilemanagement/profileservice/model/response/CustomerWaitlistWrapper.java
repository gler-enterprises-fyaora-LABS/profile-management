package com.fyaora.profilemanagement.profileservice.model.response;

import com.fyaora.profilemanagement.profileservice.model.request.CustomerWaitlist;
import java.util.List;

public record CustomerWaitlistWrapper(
        List<CustomerWaitlist> results
) { }
