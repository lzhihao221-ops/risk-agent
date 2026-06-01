package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.LoanCreditQuery;
import com.ruoyi.risk.mapper.LoanCreditQueryMapper;
import com.ruoyi.risk.service.ILoanCreditQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoanCreditQueryServiceImpl implements ILoanCreditQueryService {
    @Autowired
    private LoanCreditQueryMapper mapper;

    @Override
    public LoanCreditQuery selectById(Long id) { return mapper.selectById(id); }

    @Override
    public List<LoanCreditQuery> selectList(LoanCreditQuery query) { return mapper.selectList(query); }

    @Override
    public int insert(LoanCreditQuery record) { return mapper.insert(record); }

    @Override
    public int update(LoanCreditQuery record) { return mapper.update(record); }

    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
}
