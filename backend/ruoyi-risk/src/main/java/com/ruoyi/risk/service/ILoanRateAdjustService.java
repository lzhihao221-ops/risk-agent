package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.LoanRateAdjust;
import java.util.List;

public interface ILoanRateAdjustService {
    LoanRateAdjust selectById(Long id);
    List<LoanRateAdjust> selectList(LoanRateAdjust query);
    int insert(LoanRateAdjust record);
    int update(LoanRateAdjust record);
    int deleteByIds(Long[] ids);
}
