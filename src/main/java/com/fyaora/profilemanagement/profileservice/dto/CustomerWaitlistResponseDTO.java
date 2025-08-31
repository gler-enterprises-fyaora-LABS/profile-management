package com.fyaora.profilemanagement.profileservice.dto;

import java.util.List;

public record CustomerWaitlistResponseDTO(
        List<WaitlistCustomerRequestDTO> results
) { }
