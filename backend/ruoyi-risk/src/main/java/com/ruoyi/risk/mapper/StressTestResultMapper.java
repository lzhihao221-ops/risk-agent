package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.StressTestResult;
import java.util.List;

public interface StressTestResultMapper {
    List<StressTestResult> selectList(StressTestResult query);
    List<StressTestResult> selectByDate(String testDate);
    int insert(StressTestResult record);
    int deleteByIds(Long[] ids);
}
