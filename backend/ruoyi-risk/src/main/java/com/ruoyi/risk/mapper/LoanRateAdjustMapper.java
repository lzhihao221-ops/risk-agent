package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.LoanRateAdjust;
import java.util.List;

public interface LoanRateAdjustMapper {
    LoanRateAdjust selectById(Long id);
    List<LoanRateAdjust> selectList(LoanRateAdjust query);
    List<LoanRateAdjust> selectByLoanId(Long loanId);
    int insert(LoanRateAdjust adjust);
    int update(LoanRateAdjust adjust);
    int deleteByIds(Long[] ids);
}
