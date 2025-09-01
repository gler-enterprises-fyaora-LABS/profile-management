package com.fyaora.profilemanagement.profileservice.model.response;

import java.util.List;

public record ServiceProviderWaitlistWrapper(
        List<ServiceProviderWaitlist> results
) {  }
