package com.ruoyi.risk.mapper;

import java.util.List;
import com.ruoyi.risk.domain.LoanCollection;

/**
 * 催收管理Mapper接口
 */
public interface LoanCollectionMapper
{
    public LoanCollection selectById(Long id);

    public List<LoanCollection> selectList(LoanCollection loanCollection);

    public int insert(LoanCollection loanCollection);

    public int update(LoanCollection loanCollection);

    public int deleteByIds(Long[] ids);
}
