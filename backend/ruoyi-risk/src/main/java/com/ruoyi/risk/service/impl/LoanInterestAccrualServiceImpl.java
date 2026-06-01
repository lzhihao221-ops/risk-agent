package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.LoanInterestAccrual;
import com.ruoyi.risk.mapper.LoanInterestAccrualMapper;
import com.ruoyi.risk.service.ILoanInterestAccrualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoanInterestAccrualServiceImpl implements ILoanInterestAccrualService {
    @Autowired
    private LoanInterestAccrualMapper mapper;

    @Override
    public LoanInterestAccrual selectById(Long id) { return mapper.selectById(id); }
    @Override
    public List<LoanInterestAccrual> selectList(LoanInterestAccrual query) { return mapper.selectList(query); }
    @Override
    public List<LoanInterestAccrual> selectByLoanId(Long loanId) { return mapper.selectByLoanId(loanId); }
    @Override
    public int insert(LoanInterestAccrual record) { return mapper.insert(record); }
    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
}
