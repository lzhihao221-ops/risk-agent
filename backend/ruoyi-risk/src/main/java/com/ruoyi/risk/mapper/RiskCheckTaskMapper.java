package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.RiskCheckTask;
import java.util.List;
import java.util.Map;

/**
 * 贷后检查任务Mapper接口
 */
public interface RiskCheckTaskMapper {

    RiskCheckTask selectRiskCheckTaskById(Long id);

    List<RiskCheckTask> selectRiskCheckTaskList(RiskCheckTask task);

    int insertRiskCheckTask(RiskCheckTask task);

    int updateRiskCheckTask(RiskCheckTask task);

    int deleteRiskCheckTaskByIds(Long[] ids);

    /** 查询逾期任务数量 */
    int selectOverdueCount();

    /** 按状态统计 */
    List<Map<String, Object>> selectTaskStatusStats();

    /** 查询用户待办任务 */
    List<RiskCheckTask> selectTodoTasks(Long userId, int limit);
}
