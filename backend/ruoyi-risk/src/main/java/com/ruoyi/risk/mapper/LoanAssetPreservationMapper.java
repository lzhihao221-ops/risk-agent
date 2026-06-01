package com.ruoyi.risk.mapper;

import java.util.List;
import com.ruoyi.risk.domain.LoanAssetPreservation;

/**
 * 资产保全Mapper接口
 */
public interface LoanAssetPreservationMapper
{
    public LoanAssetPreservation selectById(Long id);

    public List<LoanAssetPreservation> selectList(LoanAssetPreservation loanAssetPreservation);

    public int insert(LoanAssetPreservation loanAssetPreservation);

    public int update(LoanAssetPreservation loanAssetPreservation);

    public int deleteByIds(Long[] ids);
}
