package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.RiskCheckTask;
import java.util.List;
import java.util.Map;

/**
 * 贷后检查任务 服务接口
 */
public interface IRiskTaskService {

    RiskCheckTask selectTaskById(Long id);

    List<RiskCheckTask> selectTaskList(RiskCheckTask task);

    int insertTask(RiskCheckTask task);

    int updateTask(RiskCheckTask task);

    int deleteTaskByIds(Long[] ids);

    /** 完成任务 */
    int completeTask(Long id, String checkResult, String checkReport);

    /** 逾期任务数量 */
    int selectOverdueCount();

    /** 按状态统计 */
    List<Map<String, Object>> selectTaskStatusStats();

    /** 用户待办任务 */
    List<RiskCheckTask> selectTodoTasks(Long userId, int limit);
}
