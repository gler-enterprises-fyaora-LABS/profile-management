package com.fyaora.profilemanagement.profileservice.service;

import com.fyaora.profilemanagement.profileservice.dto.WaitlistProcess;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistRequestDTO;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistSearchDTO;

import java.util.List;

public interface WaitlistService {
    WaitlistProcess getProcess();
    void joinWaitlist(WaitlistRequestDTO requestDTO);
    <T extends WaitlistRequestDTO> List<T> searchWaitlist(WaitlistSearchDTO searchDTO);
}
