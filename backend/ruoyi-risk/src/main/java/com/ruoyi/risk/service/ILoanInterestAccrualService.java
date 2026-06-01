package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.LoanInterestAccrual;
import java.util.List;

public interface ILoanInterestAccrualService {
    LoanInterestAccrual selectById(Long id);
    List<LoanInterestAccrual> selectList(LoanInterestAccrual query);
    List<LoanInterestAccrual> selectByLoanId(Long loanId);
    int insert(LoanInterestAccrual record);
    int deleteByIds(Long[] ids);
}
