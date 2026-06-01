package com.ruoyi.risk.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.risk.datasource.DataSourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 外部数据查询 Controller
 */
@RestController
@RequestMapping("/risk/external")
public class RiskExternalController extends BaseController {

    @Autowired
    private DataSourceManager dataSourceManager;

    /** 数据源状态 */
    @GetMapping("/sources")
    public AjaxResult listSources() {
        return success(dataSourceManager.listSources());
    }

    /** 查询企业信息 */
    @PreAuthorize("@ss.hasPermi('risk:company:query')")
    @GetMapping("/company")
    public AjaxResult queryCompany(@RequestParam String keyword) {
        Map<String, Object> result = dataSourceManager.queryCompanyInfo(keyword);
        return success(result);
    }

    /** 查询被执行人信息 */
    @PreAuthorize("@ss.hasPermi('risk:event:list')")
    @GetMapping("/execution")
    public AjaxResult queryExecution(@RequestParam String companyName) {
        return success(dataSourceManager.queryExecutionInfo(companyName));
    }

    /** 查询失信被执行人 */
    @PreAuthorize("@ss.hasPermi('risk:event:list')")
    @GetMapping("/dishonest")
    public AjaxResult queryDishonest(@RequestParam String companyName) {
        return success(dataSourceManager.queryDishonestInfo(companyName));
    }

    /** 查询经营异常 */
    @PreAuthorize("@ss.hasPermi('risk:event:list')")
    @GetMapping("/abnormal")
    public AjaxResult queryAbnormal(@RequestParam String companyName) {
        return success(dataSourceManager.queryAbnormalInfo(companyName));
    }

    /** 查询诉讼信息 */
    @PreAuthorize("@ss.hasPermi('risk:event:list')")
    @GetMapping("/lawsuit")
    public AjaxResult queryLawsuit(@RequestParam String companyName) {
        return success(dataSourceManager.queryLawsuitInfo(companyName));
    }
}
