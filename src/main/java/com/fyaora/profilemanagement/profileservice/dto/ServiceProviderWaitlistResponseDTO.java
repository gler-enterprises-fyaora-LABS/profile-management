package com.fyaora.profilemanagement.profileservice.dto;

import java.util.List;

public record ServiceProviderWaitlistResponseDTO(
        List<WaitlistServiceProviderRequestDTO> results
) {  }
