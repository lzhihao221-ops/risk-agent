package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.LoanCreditLimit;
import java.util.List;

public interface ILoanCreditLimitService {
    LoanCreditLimit selectById(Long id);
    LoanCreditLimit selectByCompanyId(Long companyId);
    List<LoanCreditLimit> selectList(LoanCreditLimit query);
    int insert(LoanCreditLimit record);
    int update(LoanCreditLimit record);
    int deleteByIds(Long[] ids);
    int refreshAvailableLimit(Long id);
}
