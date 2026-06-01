package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.risk.domain.RiskEvent;
import com.ruoyi.risk.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 风控看板 Controller（首页Dashboard数据聚合）
 */
@RestController
@RequestMapping("/risk/dashboard")
public class RiskDashboardController extends BaseController {

    @Autowired
    private IRiskCompanyService companyService;

    @Autowired
    private IRiskEventService eventService;

    @Autowired
    private IRiskAlertService alertService;

    @Autowired
    private IRiskTaskService taskService;

    /** 首页看板数据（一次请求返回所有统计） */
    @GetMapping("/summary")
    public AjaxResult summary() {
        Map<String, Object> data = new HashMap<>();

        // 企业统计
        java.util.List<com.ruoyi.risk.domain.RiskCompany> allCompanies = companyService.selectRiskCompanyList(new com.ruoyi.risk.domain.RiskCompany());
        data.put("companyCount", allCompanies.size());
        data.put("riskLevelStats", companyService.selectRiskLevelStats());
        data.put("highRiskCompanies", companyService.selectHighRiskCompanies(10));

        // 今日新增事件
        int todayEventCount = 0;
        java.util.List<com.ruoyi.risk.domain.RiskEvent> allEvents = eventService.selectRiskEventList(new com.ruoyi.risk.domain.RiskEvent());
        data.put("eventCount", allEvents.size());
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        for (com.ruoyi.risk.domain.RiskEvent ev : allEvents) {
            if (ev.getCreateTime() != null) {
                String evDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(ev.getCreateTime());
                if (today.equals(evDate)) todayEventCount++;
            }
        }
        data.put("todayEventCount", todayEventCount);

        // 预警统计
        int unhandledAlerts = alertService.selectUnhandledCount();
        data.put("unhandledAlertCount", unhandledAlerts);
        data.put("alertLevelStats", alertService.selectAlertLevelStats());
        // 最新预警去重
        java.util.List<com.ruoyi.risk.domain.RiskAlertLog> latestAlerts = alertService.selectLatestAlerts(5);
        java.util.Set<String> seenTitles = new java.util.HashSet<>();
        java.util.List<com.ruoyi.risk.domain.RiskAlertLog> dedupedAlerts = new java.util.ArrayList<>();
        for (com.ruoyi.risk.domain.RiskAlertLog a : latestAlerts) {
            String key = a.getCompanyId() + "_" + a.getAlertTitle();
            if (!seenTitles.contains(key)) {
                seenTitles.add(key);
                dedupedAlerts.add(a);
                if (dedupedAlerts.size() >= 5) break;
            }
        }
        data.put("latestAlerts", dedupedAlerts);

        // 事件统计
        data.put("unreadEvents", eventService.selectUnreadCount());
        data.put("eventTypeStats", eventService.selectEventTypeStats());
        data.put("dailyAlertStats", eventService.selectDailyAlertStats(30));

        // 任务统计
        data.put("overdueTasks", taskService.selectOverdueCount());
        data.put("taskStatusStats", taskService.selectTaskStatusStats());

        return success(data);
    }

    /** 预警趋势（近N天） */
    @GetMapping("/alertTrend")
    public AjaxResult alertTrend(@RequestParam(defaultValue = "30") int days) {
        return success(eventService.selectDailyAlertStats(days));
    }
}
