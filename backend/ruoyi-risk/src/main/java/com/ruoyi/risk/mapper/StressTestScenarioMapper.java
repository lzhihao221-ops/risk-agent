package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.StressTestScenario;
import java.util.List;

public interface StressTestScenarioMapper {
    StressTestScenario selectById(Long scenarioId);
    List<StressTestScenario> selectList(StressTestScenario query);
    int insert(StressTestScenario record);
    int update(StressTestScenario record);
    int deleteByIds(Long[] ids);
}
