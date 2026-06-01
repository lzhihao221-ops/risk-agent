package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.LoanApplication;
import com.ruoyi.risk.mapper.LoanApplicationMapper;
import com.ruoyi.risk.service.ILoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class LoanApplicationServiceImpl implements ILoanApplicationService {
    @Autowired
    private LoanApplicationMapper mapper;

    @Override
    public LoanApplication selectById(Long id) { return mapper.selectById(id); }
    @Override
    public List<LoanApplication> selectList(LoanApplication query) { return mapper.selectList(query); }
    @Override
    public int insert(LoanApplication app) { return mapper.insert(app); }
    @Override
    public int update(LoanApplication app) { return mapper.update(app); }
    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
    @Override
    public List<Map<String, Object>> selectStatusStats() { return mapper.selectStatusStats(); }
    @Override
    public List<Map<String, Object>> selectTypeStats() { return mapper.selectTypeStats(); }
}
