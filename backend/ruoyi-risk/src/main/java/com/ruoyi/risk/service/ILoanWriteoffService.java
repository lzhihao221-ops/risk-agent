package com.ruoyi.risk.service;

import java.util.List;
import com.ruoyi.risk.domain.LoanWriteoff;

/**
 * 贷款核销Service接口
 */
public interface ILoanWriteoffService
{
    public LoanWriteoff selectById(Long id);

    public List<LoanWriteoff> selectList(LoanWriteoff loanWriteoff);

    public int insert(LoanWriteoff loanWriteoff);

    public int update(LoanWriteoff loanWriteoff);

    public int deleteByIds(Long[] ids);

    /**
     * 审批核销
     */
    public int approve(Long id, String approveUser, String approveOpinion);

    /**
     * 驳回核销
     */
    public int reject(Long id, String approveUser, String approveOpinion);
}
