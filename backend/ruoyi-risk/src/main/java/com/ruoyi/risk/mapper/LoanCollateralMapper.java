package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.LoanCollateral;
import java.util.List;

public interface LoanCollateralMapper {
    LoanCollateral selectById(Long id);
    List<LoanCollateral> selectList(LoanCollateral query);
    List<LoanCollateral> selectByCompanyId(Long companyId);
    int insert(LoanCollateral collateral);
    int update(LoanCollateral collateral);
}
