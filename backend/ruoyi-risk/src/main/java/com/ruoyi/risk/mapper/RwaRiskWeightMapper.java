package com.ruoyi.risk.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.Map;

public interface RwaRiskWeightMapper {
    Map<String, Object> selectByExact(@Param("assetClass") String assetClass, @Param("assetSubclass") String assetSubclass, @Param("counterpartyType") String counterpartyType, @Param("ratingGrade") String ratingGrade);
    Map<String, Object> selectByClass(@Param("assetClass") String assetClass, @Param("ratingGrade") String ratingGrade);
}
