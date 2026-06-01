package com.ruoyi.risk.service;

import java.util.List;
import com.ruoyi.risk.domain.LoanCollection;

/**
 * 催收管理Service接口
 */
public interface ILoanCollectionService
{
    public LoanCollection selectById(Long id);

    public List<LoanCollection> selectList(LoanCollection loanCollection);

    public int insert(LoanCollection loanCollection);

    public int update(LoanCollection loanCollection);

    public int deleteByIds(Long[] ids);

    /**
     * 升级催收方式
     */
    public int approve(Long id, String collectionType, String nextAction);

    /**
     * 查询催收任务及关联的催收记录
     */
    public LoanCollection selectWithLog(Long id);
}
