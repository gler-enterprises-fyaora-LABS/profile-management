package com.fyaora.profilemanagement.profileservice.dto;

import java.util.List;

public record InvestorWaitlistResponseDTO(
        List<WaitlistInvestorRequestDTO> results
) {
}
