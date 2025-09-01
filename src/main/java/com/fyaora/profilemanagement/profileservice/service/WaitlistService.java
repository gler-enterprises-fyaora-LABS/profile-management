package com.fyaora.profilemanagement.profileservice.service;

import com.fyaora.profilemanagement.profileservice.model.enums.WaitlistProcess;
import com.fyaora.profilemanagement.profileservice.model.request.WaitlistRequest;
import com.fyaora.profilemanagement.profileservice.model.request.WaitlistSearch;

import java.util.List;

public interface WaitlistService {
    WaitlistProcess getProcess();
    void joinWaitlist(WaitlistRequest requestDTO);
    <T extends WaitlistRequest> List<T> searchWaitlist(WaitlistSearch searchDTO);
}
