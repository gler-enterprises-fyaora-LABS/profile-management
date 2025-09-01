package com.fyaora.profilemanagement.profileservice.model.response;

import java.util.List;

public record CustomerWaitlistWrapper(
        List<CustomerWaitlist> results
) { }
