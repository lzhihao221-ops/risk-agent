package com.ruoyi.risk.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.risk.domain.LoanCollection;
import com.ruoyi.risk.domain.LoanCollectionLog;
import com.ruoyi.risk.mapper.LoanCollectionMapper;
import com.ruoyi.risk.mapper.LoanCollectionLogMapper;
import com.ruoyi.risk.service.ILoanCollectionService;

/**
 * 催收管理Service业务层处理
 */
@Service
public class LoanCollectionServiceImpl implements ILoanCollectionService
{
    @Autowired
    private LoanCollectionMapper loanCollectionMapper;

    @Autowired
    private LoanCollectionLogMapper loanCollectionLogMapper;

    @Override
    public LoanCollection selectById(Long id)
    {
        return loanCollectionMapper.selectById(id);
    }

    @Override
    public List<LoanCollection> selectList(LoanCollection loanCollection)
    {
        return loanCollectionMapper.selectList(loanCollection);
    }

    @Override
    public int insert(LoanCollection loanCollection)
    {
        return loanCollectionMapper.insert(loanCollection);
    }

    @Override
    public int update(LoanCollection loanCollection)
    {
        return loanCollectionMapper.update(loanCollection);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return loanCollectionMapper.deleteByIds(ids);
    }

    @Override
    public int approve(Long id, String collectionType, String nextAction)
    {
        LoanCollection collection = loanCollectionMapper.selectById(id);
        if (collection == null)
        {
            return 0;
        }
        collection.setCollectionType(collectionType);
        collection.setNextAction(nextAction);
        collection.setStatus(2); // 已升级
        return loanCollectionMapper.update(collection);
    }

    @Override
    public LoanCollection selectWithLog(Long id)
    {
        LoanCollection collection = loanCollectionMapper.selectById(id);
        if (collection != null)
        {
            List<LoanCollectionLog> logs = loanCollectionLogMapper.selectByCollectionId(id);
            collection.setCollectionLogs(logs);
        }
        return collection;
    }
}
