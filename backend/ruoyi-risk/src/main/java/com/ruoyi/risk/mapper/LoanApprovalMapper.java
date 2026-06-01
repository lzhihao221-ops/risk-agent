package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.LoanApproval;
import java.util.List;

public interface LoanApprovalMapper {
    List<LoanApproval> selectByAppId(Long appId);
    int insert(LoanApproval approval);
}
