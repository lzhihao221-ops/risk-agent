package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.LoanInterestAccrual;
import java.util.List;
import java.util.Map;

public interface LoanInterestAccrualMapper {
    LoanInterestAccrual selectById(Long id);
    List<LoanInterestAccrual> selectList(LoanInterestAccrual query);
    List<LoanInterestAccrual> selectByLoanId(Long loanId);
    int insert(LoanInterestAccrual accrual);
    int deleteByIds(Long[] ids);
    List<Map<String, Object>> selectSummary();
}
