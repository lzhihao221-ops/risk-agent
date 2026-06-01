package com.ruoyi.risk.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.risk.domain.LoanAssetPreservation;
import com.ruoyi.risk.mapper.LoanAssetPreservationMapper;
import com.ruoyi.risk.service.ILoanAssetPreservationService;

/**
 * 资产保全Service业务层处理
 */
@Service
public class LoanAssetPreservationServiceImpl implements ILoanAssetPreservationService
{
    @Autowired
    private LoanAssetPreservationMapper loanAssetPreservationMapper;

    @Override
    public LoanAssetPreservation selectById(Long id)
    {
        return loanAssetPreservationMapper.selectById(id);
    }

    @Override
    public List<LoanAssetPreservation> selectList(LoanAssetPreservation loanAssetPreservation)
    {
        return loanAssetPreservationMapper.selectList(loanAssetPreservation);
    }

    @Override
    public int insert(LoanAssetPreservation loanAssetPreservation)
    {
        return loanAssetPreservationMapper.insert(loanAssetPreservation);
    }

    @Override
    public int update(LoanAssetPreservation loanAssetPreservation)
    {
        return loanAssetPreservationMapper.update(loanAssetPreservation);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return loanAssetPreservationMapper.deleteByIds(ids);
    }

    @Override
    public int updateRecoveryAmount(Long id, BigDecimal recoveryAmount)
    {
        LoanAssetPreservation preservation = loanAssetPreservationMapper.selectById(id);
        if (preservation == null)
        {
            return 0;
        }
        preservation.setRecoveryAmount(recoveryAmount);
        return loanAssetPreservationMapper.update(preservation);
    }
}
