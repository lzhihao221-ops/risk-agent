package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.LoanDisbursement;
import java.util.List;
import java.util.Map;

public interface ILoanDisbursementService {
    LoanDisbursement selectById(Long id);
    List<LoanDisbursement> selectList(LoanDisbursement query);
    int insert(LoanDisbursement disbursement);
    int update(LoanDisbursement disbursement);
    int deleteByIds(Long[] ids);
    List<Map<String, Object>> selectStatusStats();
    List<Map<String, Object>> selectSummary();
    int approve(Long id, String user);
    int reject(Long id, String user, String reason);
    int disburse(Long id, String user);
}
