package com.ruoyi.risk.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.risk.domain.LoanWriteoff;
import com.ruoyi.risk.mapper.LoanWriteoffMapper;
import com.ruoyi.risk.service.ILoanWriteoffService;

/**
 * 贷款核销Service业务层处理
 */
@Service
public class LoanWriteoffServiceImpl implements ILoanWriteoffService
{
    @Autowired
    private LoanWriteoffMapper loanWriteoffMapper;

    @Override
    public LoanWriteoff selectById(Long id)
    {
        return loanWriteoffMapper.selectById(id);
    }

    @Override
    public List<LoanWriteoff> selectList(LoanWriteoff loanWriteoff)
    {
        return loanWriteoffMapper.selectList(loanWriteoff);
    }

    @Override
    public int insert(LoanWriteoff loanWriteoff)
    {
        return loanWriteoffMapper.insert(loanWriteoff);
    }

    @Override
    public int update(LoanWriteoff loanWriteoff)
    {
        return loanWriteoffMapper.update(loanWriteoff);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return loanWriteoffMapper.deleteByIds(ids);
    }

    @Override
    public int approve(Long id, String approveUser, String approveOpinion)
    {
        LoanWriteoff writeoff = loanWriteoffMapper.selectById(id);
        if (writeoff == null)
        {
            return 0;
        }
        writeoff.setStatus(1); // 已核销
        writeoff.setApproveUser(approveUser);
        writeoff.setApproveTime(new Date());
        writeoff.setApproveOpinion(approveOpinion);
        return loanWriteoffMapper.update(writeoff);
    }

    @Override
    public int reject(Long id, String approveUser, String approveOpinion)
    {
        LoanWriteoff writeoff = loanWriteoffMapper.selectById(id);
        if (writeoff == null)
        {
            return 0;
        }
        writeoff.setStatus(2); // 已驳回
        writeoff.setApproveUser(approveUser);
        writeoff.setApproveTime(new Date());
        writeoff.setApproveOpinion(approveOpinion);
        return loanWriteoffMapper.update(writeoff);
    }
}
