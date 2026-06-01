package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.LoanApplication;
import java.util.List;
import java.util.Map;

public interface LoanApplicationMapper {
    LoanApplication selectById(Long id);
    List<LoanApplication> selectList(LoanApplication query);
    int insert(LoanApplication app);
    int update(LoanApplication app);
    int deleteByIds(Long[] ids);
    List<Map<String, Object>> selectStatusStats();
    List<Map<String, Object>> selectTypeStats();
}
