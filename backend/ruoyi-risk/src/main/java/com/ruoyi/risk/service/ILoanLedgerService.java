package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.LoanLedger;
import java.util.List;
import java.util.Map;

public interface ILoanLedgerService {
    LoanLedger selectById(Long id);
    List<LoanLedger> selectList(LoanLedger query);
    int insert(LoanLedger ledger);
    int update(LoanLedger ledger);
    int deleteByIds(Long[] ids);
    List<Map<String, Object>> selectCategoryStats();
    List<Map<String, Object>> selectOverdueStats();
    Map<String, Object> selectSummary();
}
