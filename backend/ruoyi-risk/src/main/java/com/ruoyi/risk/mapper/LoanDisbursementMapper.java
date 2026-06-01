package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.LoanDisbursement;
import java.util.List;
import java.util.Map;

public interface LoanDisbursementMapper {
    LoanDisbursement selectById(Long id);
    List<LoanDisbursement> selectList(LoanDisbursement query);
    int insert(LoanDisbursement disbursement);
    int update(LoanDisbursement disbursement);
    int deleteByIds(Long[] ids);
    List<Map<String, Object>> selectStatusStats();
    List<Map<String, Object>> selectSummary();
}
