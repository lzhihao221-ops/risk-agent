package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.VarCalculation;
import java.util.List;

public interface VarCalculationMapper {
    List<VarCalculation> selectList(VarCalculation query);
    int insert(VarCalculation record);
    int deleteByIds(Long[] ids);
}
