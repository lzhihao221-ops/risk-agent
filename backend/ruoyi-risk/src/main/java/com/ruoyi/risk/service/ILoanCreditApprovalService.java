package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.LoanCreditApproval;
import java.util.List;

public interface ILoanCreditApprovalService {
    LoanCreditApproval selectById(Long id);
    List<LoanCreditApproval> selectList(LoanCreditApproval query);
    int insert(LoanCreditApproval record);
    int update(LoanCreditApproval record);
    int deleteByIds(Long[] ids);
}
