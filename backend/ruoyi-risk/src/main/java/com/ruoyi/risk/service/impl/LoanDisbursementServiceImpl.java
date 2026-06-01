package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.LoanDisbursement;
import com.ruoyi.risk.mapper.LoanDisbursementMapper;
import com.ruoyi.risk.service.ILoanDisbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class LoanDisbursementServiceImpl implements ILoanDisbursementService {
    @Autowired
    private LoanDisbursementMapper mapper;

    @Override
    public LoanDisbursement selectById(Long id) { return mapper.selectById(id); }
    @Override
    public List<LoanDisbursement> selectList(LoanDisbursement query) { return mapper.selectList(query); }
    @Override
    public int insert(LoanDisbursement record) { return mapper.insert(record); }
    @Override
    public int update(LoanDisbursement record) { return mapper.update(record); }
    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }

    @Override
    public List<Map<String, Object>> selectStatusStats() {
        return mapper.selectStatusStats();
    }

    @Override
    public List<Map<String, Object>> selectSummary() {
        return mapper.selectSummary();
    }

    @Override
    public int approve(Long id, String user) {
        LoanDisbursement record = mapper.selectById(id);
        if (record == null) return 0;
        record.setStatus(0);
        record.setApproveUser(user);
        record.setApproveTime(new java.util.Date());
        return mapper.update(record);
    }

    @Override
    public int reject(Long id, String user, String reason) {
        LoanDisbursement record = mapper.selectById(id);
        if (record == null) return 0;
        record.setStatus(2);
        record.setApproveUser(user);
        record.setApproveTime(new java.util.Date());
        record.setRemark(reason);
        return mapper.update(record);
    }

    @Override
    public int disburse(Long id, String user) {
        LoanDisbursement record = mapper.selectById(id);
        if (record == null) return 0;
        record.setStatus(1);
        record.setDisburseTime(new java.util.Date());
        return mapper.update(record);
    }
}
