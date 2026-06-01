package com.ruoyi.risk.mapper;

import java.util.List;
import com.ruoyi.risk.domain.LoanWriteoff;

/**
 * 贷款核销Mapper接口
 */
public interface LoanWriteoffMapper
{
    public LoanWriteoff selectById(Long id);

    public List<LoanWriteoff> selectList(LoanWriteoff loanWriteoff);

    public int insert(LoanWriteoff loanWriteoff);

    public int update(LoanWriteoff loanWriteoff);

    public int deleteByIds(Long[] ids);
}
