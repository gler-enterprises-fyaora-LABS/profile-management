package com.fyaora.profilemanagement.profileservice.model.response;

import com.fyaora.profilemanagement.profileservice.model.request.ServiceProviderWaitlist;
import java.util.List;

public record ServiceProviderWaitlistWrapper(
        List<ServiceProviderWaitlist> results
) {  }
