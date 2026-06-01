package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.LoanCreditLimit;
import com.ruoyi.risk.mapper.LoanCreditLimitMapper;
import com.ruoyi.risk.service.ILoanCreditLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class LoanCreditLimitServiceImpl implements ILoanCreditLimitService {
    @Autowired
    private LoanCreditLimitMapper mapper;

    @Override
    public LoanCreditLimit selectById(Long id) { return mapper.selectById(id); }

    @Override
    public LoanCreditLimit selectByCompanyId(Long companyId) { return mapper.selectByCompanyId(companyId); }

    @Override
    public List<LoanCreditLimit> selectList(LoanCreditLimit query) { return mapper.selectList(query); }

    @Override
    public int insert(LoanCreditLimit record) { return mapper.insert(record); }

    @Override
    public int update(LoanCreditLimit record) { return mapper.update(record); }

    @Override
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }

    @Override
    public int refreshAvailableLimit(Long id) {
        LoanCreditLimit limit = mapper.selectById(id);
        if (limit == null) { return 0; }
        BigDecimal available = limit.getTotalLimit().subtract(limit.getUsedLimit());
        limit.setAvailableLimit(available);
        return mapper.update(limit);
    }
}
