package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.RiskCheckTask;
import com.ruoyi.risk.mapper.RiskCheckTaskMapper;
import com.ruoyi.risk.service.IRiskTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 贷后检查任务 服务实现
 */
@Service
public class RiskTaskServiceImpl implements IRiskTaskService {

    @Autowired
    private RiskCheckTaskMapper taskMapper;

    @Override
    public RiskCheckTask selectTaskById(Long id) {
        return taskMapper.selectRiskCheckTaskById(id);
    }

    @Override
    public List<RiskCheckTask> selectTaskList(RiskCheckTask task) {
        return taskMapper.selectRiskCheckTaskList(task);
    }

    @Override
    public int insertTask(RiskCheckTask task) {
        // 新建任务默认状态为待执行
        if (task.getTaskStatus() == null) {
            task.setTaskStatus(0);
        }
        return taskMapper.insertRiskCheckTask(task);
    }

    @Override
    public int updateTask(RiskCheckTask task) {
        return taskMapper.updateRiskCheckTask(task);
    }

    @Override
    public int deleteTaskByIds(Long[] ids) {
        return taskMapper.deleteRiskCheckTaskByIds(ids);
    }

    @Override
    public int completeTask(Long id, String checkResult, String checkReport) {
        RiskCheckTask task = new RiskCheckTask();
        task.setId(id);
        task.setTaskStatus(2); // 已完成
        task.setCheckResult(checkResult);
        task.setCheckReport(checkReport);
        task.setCompleteTime(new Date());
        return taskMapper.updateRiskCheckTask(task);
    }

    @Override
    public int selectOverdueCount() {
        return taskMapper.selectOverdueCount();
    }

    @Override
    public List<Map<String, Object>> selectTaskStatusStats() {
        return taskMapper.selectTaskStatusStats();
    }

    @Override
    public List<RiskCheckTask> selectTodoTasks(Long userId, int limit) {
        return taskMapper.selectTodoTasks(userId, limit);
    }
}
