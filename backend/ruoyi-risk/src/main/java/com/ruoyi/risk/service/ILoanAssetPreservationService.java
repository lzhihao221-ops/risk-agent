package com.ruoyi.risk.service;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.risk.domain.LoanAssetPreservation;

/**
 * 资产保全Service接口
 */
public interface ILoanAssetPreservationService
{
    public LoanAssetPreservation selectById(Long id);

    public List<LoanAssetPreservation> selectList(LoanAssetPreservation loanAssetPreservation);

    public int insert(LoanAssetPreservation loanAssetPreservation);

    public int update(LoanAssetPreservation loanAssetPreservation);

    public int deleteByIds(Long[] ids);

    /**
     * 更新回收金额
     */
    public int updateRecoveryAmount(Long id, BigDecimal recoveryAmount);
}
