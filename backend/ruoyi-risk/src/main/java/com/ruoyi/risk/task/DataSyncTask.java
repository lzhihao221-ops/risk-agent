package com.ruoyi.risk.task;

import com.ruoyi.risk.datasource.DataSourceManager;
import com.ruoyi.risk.domain.RiskCompany;
import com.ruoyi.risk.domain.RiskEvent;
import com.ruoyi.risk.mapper.RiskCompanyMapper;
import com.ruoyi.risk.service.IRiskCompanyService;
import com.ruoyi.risk.service.IRiskEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 定时数据同步任务
 * 从外部数据源采集企业风险数据
 */
@Component
public class DataSyncTask {

    private static final Logger log = LoggerFactory.getLogger(DataSyncTask.class);

    @Autowired
    private DataSourceManager dataSourceManager;

    @Autowired
    private RiskCompanyMapper companyMapper;

    @Autowired
    private IRiskEventService eventService;

    @Autowired
    private IRiskCompanyService companyService;

    /**
     * 每天凌晨3点：从外部数据源同步风险事件
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void syncRiskEvents() {
        if (dataSourceManager.getAvailableSource() == null) {
            log.info("无可用数据源，跳过数据同步");
            return;
        }

        log.info("========== 开始同步外部风险数据 ==========");
        List<RiskCompany> companies = companyMapper.selectRiskCompanyList(new RiskCompany());
        int newEvents = 0;

        for (RiskCompany company : companies) {
            try {
                // 查询被执行信息
                Map<String, Object> execResult = dataSourceManager.queryExecutionInfo(company.getCompanyName());
                if (!execResult.containsKey("error")) {
                    List<?> records = (List<?>) execResult.getOrDefault("result", List.of());
                    for (Object record : records) {
                        // 检查是否已存在
                        RiskEvent existing = new RiskEvent();
                        existing.setCompanyId(company.getId());
                        existing.setEventType("EXECUTION");
                        // 简单去重：同一企业不重复插入相同类型的事件
                        List<RiskEvent> existingEvents = eventService.selectRiskEventList(existing);
                        if (existingEvents.isEmpty()) {
                            RiskEvent event = new RiskEvent();
                            event.setCompanyId(company.getId());
                            event.setEventType("EXECUTION");
                            event.setEventTitle("被执行人信息 - " + company.getCompanyName());
                            event.setSeverity(3);
                            event.setIsRead(0);
                            event.setIsHandled(0);
                            event.setDataSource("天眼查");
                            eventService.insertRiskEvent(event);
                            newEvents++;
                        }
                    }
                }

                // 查询失信信息
                Map<String, Object> dishonestResult = dataSourceManager.queryDishonestInfo(company.getCompanyName());
                if (!dishonestResult.containsKey("error")) {
                    List<?> records = (List<?>) dishonestResult.getOrDefault("result", List.of());
                    if (!records.isEmpty()) {
                        RiskEvent existing = new RiskEvent();
                        existing.setCompanyId(company.getId());
                        existing.setEventType("DISHONEST");
                        List<RiskEvent> existingEvents = eventService.selectRiskEventList(existing);
                        if (existingEvents.isEmpty()) {
                            RiskEvent event = new RiskEvent();
                            event.setCompanyId(company.getId());
                            event.setEventType("DISHONEST");
                            event.setEventTitle("失信被执行人 - " + company.getCompanyName());
                            event.setSeverity(3);
                            event.setIsRead(0);
                            event.setIsHandled(0);
                            event.setDataSource("天眼查");
                            eventService.insertRiskEvent(event);
                            newEvents++;
                        }
                    }
                }

                // 查询经营异常
                Map<String, Object> abnormalResult = dataSourceManager.queryAbnormalInfo(company.getCompanyName());
                if (!abnormalResult.containsKey("error")) {
                    List<?> records = (List<?>) abnormalResult.getOrDefault("result", List.of());
                    if (!records.isEmpty()) {
                        RiskEvent existing = new RiskEvent();
                        existing.setCompanyId(company.getId());
                        existing.setEventType("ABNORMAL");
                        List<RiskEvent> existingEvents = eventService.selectRiskEventList(existing);
                        if (existingEvents.isEmpty()) {
                            RiskEvent event = new RiskEvent();
                            event.setCompanyId(company.getId());
                            event.setEventType("ABNORMAL");
                            event.setEventTitle("经营异常 - " + company.getCompanyName());
                            event.setSeverity(2);
                            event.setIsRead(0);
                            event.setIsHandled(0);
                            event.setDataSource("天眼查");
                            eventService.insertRiskEvent(event);
                            newEvents++;
                        }
                    }
                }

                // 同步后重新计算风险评分
                companyService.scanCompanyRisk(company.getId());

                Thread.sleep(1000); // 限速：每秒1次请求
            } catch (Exception e) {
                log.error("同步企业 [{}] 数据失败: {}", company.getCompanyName(), e.getMessage());
            }
        }

        log.info("========== 数据同步完成: 新增 {} 条风险事件 ==========", newEvents);
    }
}
