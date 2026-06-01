package com.ruoyi.risk.task;

import com.ruoyi.risk.domain.RiskCompany;
import com.ruoyi.risk.mapper.RiskCompanyMapper;
import com.ruoyi.risk.service.IRiskCompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时扫描任务
 * - 每天凌晨 2 点自动扫描所有监控企业风险
 */
@Component
public class RiskScanTask {

    private static final Logger log = LoggerFactory.getLogger(RiskScanTask.class);

    @Autowired
    private IRiskCompanyService companyService;

    @Autowired
    private RiskCompanyMapper companyMapper;

    /**
     * 每天凌晨 2 点执行全量风险扫描
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void dailyRiskScan() {
        log.info("========== 开始每日风险扫描 ==========");
        List<RiskCompany> companies = companyMapper.selectRiskCompanyList(new RiskCompany());
        int scanned = 0;
        int upgraded = 0;

        for (RiskCompany company : companies) {
            try {
                int oldLevel = company.getRiskLevel() != null ? company.getRiskLevel() : 0;
                companyService.scanCompanyRisk(company.getId());
                RiskCompany updated = companyMapper.selectRiskCompanyById(company.getId());
                if (updated != null && updated.getRiskLevel() > oldLevel) {
                    upgraded++;
                    log.warn("企业 [{}] 风险等级上升: {} → {}", company.getCompanyName(), oldLevel, updated.getRiskLevel());
                }
                scanned++;
            } catch (Exception e) {
                log.error("扫描企业 [{}] 失败: {}", company.getCompanyName(), e.getMessage());
            }
        }

        log.info("========== 扫描完成: 共扫描 {} 家, {} 家风险等级上升 ==========", scanned, upgraded);
    }
}
