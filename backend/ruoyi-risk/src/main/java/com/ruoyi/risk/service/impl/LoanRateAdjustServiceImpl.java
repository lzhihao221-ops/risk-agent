package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.LoanRateAdjust;
import com.ruoyi.risk.mapper.LoanRateAdjustMapper;
import com.ruoyi.risk.service.ILoanRateAdjustService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoanRateAdjustServiceImpl implements ILoanRateAdjustService {
    @Autowired
    private LoanRateAdjustMapper mapper;

    @Override
    public LoanRateAdjust selectById(Long id) { return mapper.selectById(id); }
    @Override
    public List<LoanRateAdjust> selectList(LoanRateAdjust query) { return mapper.selectList(query); }
    @Override
    public int insert(LoanRateAdjust record) { return mapper.insert(record); }
    @Override
    public int update(LoanRateAdjust record) { return mapper.update(record); }
    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
}
