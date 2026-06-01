package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.LoanLedger;
import com.ruoyi.risk.mapper.LoanLedgerMapper;
import com.ruoyi.risk.service.ILoanLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class LoanLedgerServiceImpl implements ILoanLedgerService {
    @Autowired
    private LoanLedgerMapper mapper;

    @Override
    public LoanLedger selectById(Long id) { return mapper.selectById(id); }
    @Override
    public List<LoanLedger> selectList(LoanLedger query) { return mapper.selectList(query); }
    @Override
    public int insert(LoanLedger ledger) { return mapper.insert(ledger); }
    @Override
    public int update(LoanLedger ledger) { return mapper.update(ledger); }
    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
    @Override
    public List<Map<String, Object>> selectCategoryStats() { return mapper.selectCategoryStats(); }
    @Override
    public List<Map<String, Object>> selectOverdueStats() { return mapper.selectOverdueStats(); }
    @Override
    public Map<String, Object> selectSummary() { return mapper.selectSummary(); }
}
