package com.ruoyi.risk.mapper;

import java.util.List;
import com.ruoyi.risk.domain.LoanCollectionLog;

/**
 * 催收记录Mapper接口
 */
public interface LoanCollectionLogMapper
{
    public List<LoanCollectionLog> selectByCollectionId(Long collectionId);

    public int insert(LoanCollectionLog loanCollectionLog);

    public int deleteByIds(Long[] ids);
}
