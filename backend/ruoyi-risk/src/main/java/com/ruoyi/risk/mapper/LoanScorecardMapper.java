package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.LoanScorecard;
import java.util.List;

public interface LoanScorecardMapper {
    LoanScorecard selectById(Long id);
    LoanScorecard selectLatestByCompanyId(Long companyId);
    List<LoanScorecard> selectList(LoanScorecard query);
    int insert(LoanScorecard scorecard);
}
