package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.LoanCreditQuery;
import java.util.List;

public interface LoanCreditQueryMapper {
    LoanCreditQuery selectById(Long id);
    List<LoanCreditQuery> selectList(LoanCreditQuery query);
    int insert(LoanCreditQuery record);
    int update(LoanCreditQuery record);
    int deleteByIds(Long[] ids);
}
