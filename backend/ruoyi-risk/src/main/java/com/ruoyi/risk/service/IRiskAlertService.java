package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.RiskAlertLog;
import com.ruoyi.risk.domain.RiskAlertRule;
import java.util.List;
import java.util.Map;

/**
 * 预警服务接口
 */
public interface IRiskAlertService {

    // === 预警规则 ===
    List<RiskAlertRule> selectRuleList(RiskAlertRule rule);
    RiskAlertRule selectRuleById(Long id);
    int insertRule(RiskAlertRule rule);
    int updateRule(RiskAlertRule rule);
    int deleteRuleByIds(Long[] ids);
    List<RiskAlertRule> selectActiveRules();

    // === 预警记录 ===
    List<RiskAlertLog> selectAlertList(RiskAlertLog alertLog);
    RiskAlertLog selectAlertById(Long id);
    int handleAlert(Long id, Long handlerId, String handlerName, String handleResult);
    int selectUnhandledCount();
    List<Map<String, Object>> selectAlertLevelStats();
    List<RiskAlertLog> selectLatestAlerts(int limit);

    // === 预警触发 ===
    /** 检查风险事件是否触发预警规则，并生成预警记录 */
    void checkAndTriggerAlert(Long companyId, String eventType, Long eventId, String eventTitle);
    boolean existsRecentAlert(Long companyId, Long ruleId, int hours);
}
