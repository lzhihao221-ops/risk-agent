package com.ruoyi.risk.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.risk.domain.RiskAlertLog;
import com.ruoyi.risk.service.IRiskAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 预警中心 Controller
 */
@RestController
@RequestMapping("/risk/alert")
public class RiskAlertController extends BaseController {

    @Autowired
    private IRiskAlertService alertService;

    /** 预警列表 */
    @PreAuthorize("@ss.hasPermi('risk:alert:list')")
    @GetMapping("/list")
    public TableDataInfo list(RiskAlertLog alertLog) {
        startPage();
        List<RiskAlertLog> list = alertService.selectAlertList(alertLog);
        return getDataTable(list);
    }

    /** 预警详情 */
    @PreAuthorize("@ss.hasPermi('risk:alert:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(alertService.selectAlertById(id));
    }

    /** 处理预警 */
    @PreAuthorize("@ss.hasPermi('risk:alert:edit')")
    @Log(title = "预警处理", businessType = BusinessType.UPDATE)
    @PostMapping("/handle")
    public AjaxResult handle(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String handleResult = params.get("handleResult").toString();
        // 获取当前用户信息
        Long userId = getUserId();
        String userName = getUsername();
        return toAjax(alertService.handleAlert(id, userId, userName, handleResult));
    }

    /** 预警等级统计 */
    @PreAuthorize("@ss.hasPermi('risk:alert:list')")
    @GetMapping("/stats/level")
    public AjaxResult levelStats() {
        List<Map<String, Object>> stats = alertService.selectAlertLevelStats();
        return success(stats);
    }

    /** 未处理预警数量 */
    @PreAuthorize("@ss.hasPermi('risk:alert:list')")
    @GetMapping("/stats/unhandled")
    public AjaxResult unhandledCount() {
        return success(alertService.selectUnhandledCount());
    }

    /** 导出预警记录 */
    @PreAuthorize("@ss.hasPermi('risk:alert:export')")
    @Log(title = "预警管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RiskAlertLog alertLog) {
        List<RiskAlertLog> list = alertService.selectAlertList(alertLog);
        ExcelUtil<RiskAlertLog> util = new ExcelUtil<>(RiskAlertLog.class);
        util.exportExcel(response, list, "预警数据");
    }
}
