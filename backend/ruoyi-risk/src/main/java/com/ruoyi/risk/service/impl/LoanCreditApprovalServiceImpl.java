package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.LoanCreditApproval;
import com.ruoyi.risk.mapper.LoanCreditApprovalMapper;
import com.ruoyi.risk.service.ILoanCreditApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoanCreditApprovalServiceImpl implements ILoanCreditApprovalService {
    @Autowired
    private LoanCreditApprovalMapper mapper;

    @Override
    public LoanCreditApproval selectById(Long id) { return mapper.selectById(id); }

    @Override
    public List<LoanCreditApproval> selectList(LoanCreditApproval query) { return mapper.selectList(query); }

    @Override
    public int insert(LoanCreditApproval record) { return mapper.insert(record); }

    @Override
    public int update(LoanCreditApproval record) { return mapper.update(record); }

    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
}
