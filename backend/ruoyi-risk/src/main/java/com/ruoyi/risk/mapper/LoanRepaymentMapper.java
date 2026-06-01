package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.LoanRepayment;
import java.util.List;

public interface LoanRepaymentMapper {
    List<LoanRepayment> selectByLoanId(Long loanId);
    int insert(LoanRepayment repayment);
    int updateStatus(LoanRepayment repayment);
}
