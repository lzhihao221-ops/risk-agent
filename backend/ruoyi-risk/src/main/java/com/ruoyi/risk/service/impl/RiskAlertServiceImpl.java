package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.RiskAlertLog;
import com.ruoyi.risk.domain.RiskAlertRule;
import com.ruoyi.risk.domain.RiskCompany;
import com.ruoyi.risk.domain.RiskMessage;
import com.ruoyi.risk.mapper.RiskAlertLogMapper;
import com.ruoyi.risk.mapper.RiskAlertRuleMapper;
import com.ruoyi.risk.mapper.RiskCompanyMapper;
import com.ruoyi.risk.mapper.RiskMessageMapper;
import com.ruoyi.risk.service.IRiskAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预警服务实现
 */
@Service
public class RiskAlertServiceImpl implements IRiskAlertService {

    private static final Logger log = LoggerFactory.getLogger(RiskAlertServiceImpl.class);

    @Autowired
    private RiskAlertRuleMapper ruleMapper;

    @Autowired
    private RiskAlertLogMapper alertLogMapper;

    @Autowired
    private RiskCompanyMapper companyMapper;

    @Autowired
    private RiskMessageMapper messageMapper;

    // === 预警规则 ===

    @Override
    public List<RiskAlertRule> selectRuleList(RiskAlertRule rule) {
        return ruleMapper.selectRiskAlertRuleList(rule);
    }

    @Override
    public RiskAlertRule selectRuleById(Long id) {
        return ruleMapper.selectRiskAlertRuleById(id);
    }

    @Override
    public int insertRule(RiskAlertRule rule) {
        return ruleMapper.insertRiskAlertRule(rule);
    }

    @Override
    public int updateRule(RiskAlertRule rule) {
        return ruleMapper.updateRiskAlertRule(rule);
    }

    @Override
    public int deleteRuleByIds(Long[] ids) {
        return ruleMapper.deleteRiskAlertRuleByIds(ids);
    }

    @Override
    public List<RiskAlertRule> selectActiveRules() {
        return ruleMapper.selectActiveRules();
    }

    // === 预警记录 ===

    @Override
    public List<RiskAlertLog> selectAlertList(RiskAlertLog alertLog) {
        return alertLogMapper.selectRiskAlertLogList(alertLog);
    }

    @Override
    public RiskAlertLog selectAlertById(Long id) {
        return alertLogMapper.selectRiskAlertLogById(id);
    }

    @Override
    public int handleAlert(Long id, Long handlerId, String handlerName, String handleResult) {
        RiskAlertLog alert = new RiskAlertLog();
        alert.setId(id);
        alert.setIsHandled(1);
        alert.setHandlerId(handlerId);
        alert.setHandlerName(handlerName);
        alert.setHandleTime(new Date());
        alert.setHandleResult(handleResult);
        return alertLogMapper.updateRiskAlertLog(alert);
    }

    @Override
    public int selectUnhandledCount() {
        return alertLogMapper.selectUnhandledCount();
    }

    @Override
    public List<Map<String, Object>> selectAlertLevelStats() {
        return alertLogMapper.selectAlertLevelStats();
    }

    @Override
    public List<RiskAlertLog> selectLatestAlerts(int limit) {
        return alertLogMapper.selectLatestAlerts(limit);
    }

    // === 预警触发 ===

    @Override
    public void checkAndTriggerAlert(Long companyId, String eventType, Long eventId, String eventTitle) {
        // 查询所有启用的规则，检查是否有匹配的
        List<RiskAlertRule> activeRules = ruleMapper.selectActiveRules();

        for (RiskAlertRule rule : activeRules) {
            if (matchRule(rule, eventType)) {
                // 去重：同一企业同一规则24小时内不重复预警
                boolean exists = alertLogMapper.existsRecentAlert(companyId, rule.getId(), 24);
                if (exists) {
                    continue;
                }
                // 生成预警记录
                RiskAlertLog alertLog = new RiskAlertLog();
                alertLog.setRuleId(rule.getId());
                alertLog.setCompanyId(companyId);
                alertLog.setEventId(eventId);
                alertLog.setAlertLevel(rule.getAlertLevel());
                alertLog.setAlertTitle(eventTitle);
                alertLog.setAlertContent(buildAlertContent(rule, eventTitle));
                alertLog.setIsNotified(0);
                alertLogMapper.insertRiskAlertLog(alertLog);

                // 发送站内消息通知
                sendAlertMessage(alertLog, rule);
            }
        }
    }

    /** 判断规则是否匹配事件类型 */
    private boolean matchRule(RiskAlertRule rule, String eventType) {
        if (!"EVENT".equals(rule.getRuleType())) {
            return false;
        }
        // rule.eventType 为空表示匹配所有事件类型
        if (rule.getEventType() == null || rule.getEventType().isEmpty()) {
            return true;
        }
        return rule.getEventType().equals(eventType);
    }

    /** 构建预警内容 */
    private String buildAlertContent(RiskAlertRule rule, String eventTitle) {
        return String.format("触发规则【%s】：%s", rule.getRuleName(), eventTitle);
    }

    @Override
    public boolean existsRecentAlert(Long companyId, Long ruleId, int hours) {
        return alertLogMapper.existsRecentAlert(companyId, ruleId, hours);
    }

    /** 发送预警站内消息 */
    private void sendAlertMessage(RiskAlertLog alertLog, RiskAlertRule rule) {
        try {
            // 查询企业对应的管户经理
            RiskCompany company = companyMapper.selectRiskCompanyById(alertLog.getCompanyId());
            if (company != null && company.getManagerId() != null) {
                RiskMessage msg = new RiskMessage();
                msg.setUserId(company.getManagerId());
                msg.setTitle("【预警通知】" + alertLog.getAlertTitle());
                msg.setContent(alertLog.getAlertContent());
                msg.setMsgType(1);
                msg.setRefType("alert");
                msg.setRefId(alertLog.getId());
                messageMapper.insertMessage(msg);
            }
            // 同时通知风控经理（角色ID=3）
            // 这里简化处理，实际可通过角色查询用户
        } catch (Exception e) {
            log.error("发送预警消息失败", e);
        }
    }
}
