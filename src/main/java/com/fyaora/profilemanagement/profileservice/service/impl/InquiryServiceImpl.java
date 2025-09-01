package com.fyaora.profilemanagement.profileservice.service.impl;

import com.fyaora.profilemanagement.profileservice.model.request.InquiryRequest;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Inquiry;
import com.fyaora.profilemanagement.profileservice.model.db.repository.InquiryRepository;
import com.fyaora.profilemanagement.profileservice.model.db.mapper.InquiryMapper;
import com.fyaora.profilemanagement.profileservice.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InquiryServiceImpl implements InquiryService {

    private InquiryMapper inquiryMapper;
    private final InquiryRepository inquiryRepository;

    @Autowired
    public InquiryServiceImpl(InquiryMapper inquiryMapper, InquiryRepository inquiryRepository) {
        this.inquiryMapper = inquiryMapper;
        this.inquiryRepository = inquiryRepository;
    }

    public void addInquiry(InquiryRequest inquiryRequest) {
        Inquiry inquiry = inquiryMapper.toEntity(inquiryRequest);
        inquiry.setEnabled(Boolean.TRUE);
        inquiry.setCreatedDatetime(LocalDateTime.now());
        inquiryRepository.save(inquiry);
    }
}
