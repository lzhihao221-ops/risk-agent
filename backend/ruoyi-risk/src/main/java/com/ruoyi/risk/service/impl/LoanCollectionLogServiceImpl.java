package com.ruoyi.risk.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.risk.domain.LoanCollectionLog;
import com.ruoyi.risk.mapper.LoanCollectionLogMapper;
import com.ruoyi.risk.service.ILoanCollectionLogService;

/**
 * 催收记录Service业务层处理
 */
@Service
public class LoanCollectionLogServiceImpl implements ILoanCollectionLogService
{
    @Autowired
    private LoanCollectionLogMapper loanCollectionLogMapper;

    @Override
    public List<LoanCollectionLog> selectByCollectionId(Long collectionId)
    {
        return loanCollectionLogMapper.selectByCollectionId(collectionId);
    }

    @Override
    public int insert(LoanCollectionLog loanCollectionLog)
    {
        return loanCollectionLogMapper.insert(loanCollectionLog);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return loanCollectionLogMapper.deleteByIds(ids);
    }
}
