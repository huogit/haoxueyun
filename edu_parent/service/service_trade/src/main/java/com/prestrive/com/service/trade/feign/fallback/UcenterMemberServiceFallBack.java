package com.prestrive.com.service.trade.feign.fallback;

import com.prestrive.com.service.base.dto.MemberDto;
import com.prestrive.com.service.trade.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UcenterMemberServiceFallBack implements UcenterMemberService {

    @Override
    public MemberDto getMemberDtoByMemberId(String memberId) {
        log.error("熔断保护");
        return null;
    }
}
