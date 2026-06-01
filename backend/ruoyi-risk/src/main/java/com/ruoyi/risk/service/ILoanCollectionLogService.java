package com.ruoyi.risk.service;

import java.util.List;
import com.ruoyi.risk.domain.LoanCollectionLog;

/**
 * 催收记录Service接口
 */
public interface ILoanCollectionLogService
{
    public List<LoanCollectionLog> selectByCollectionId(Long collectionId);

    public int insert(LoanCollectionLog loanCollectionLog);

    public int deleteByIds(Long[] ids);
}
