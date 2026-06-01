package com.ruoyi.risk.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.risk.domain.RiskEvent;
import com.ruoyi.risk.service.IRiskEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 风险事件 Controller
 */
@RestController
@RequestMapping("/risk/event")
public class RiskEventController extends BaseController {

    @Autowired
    private IRiskEventService eventService;

    /** 事件列表 */
    @PreAuthorize("@ss.hasPermi('risk:event:list')")
    @GetMapping("/list")
    public TableDataInfo list(RiskEvent event) {
        startPage();
        List<RiskEvent> list = eventService.selectRiskEventList(event);
        return getDataTable(list);
    }

    /** 事件详情 */
    @PreAuthorize("@ss.hasPermi('risk:event:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(eventService.selectRiskEventById(id));
    }

    /** 新增事件（自动触发预警检查） */
    @PreAuthorize("@ss.hasPermi('risk:event:add')")
    @Log(title = "风险事件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RiskEvent event) {
        return toAjax(eventService.insertRiskEvent(event));
    }

    /** 标记已读 */
    @PreAuthorize("@ss.hasPermi('risk:event:edit')")
    @PutMapping("/read/{id}")
    public AjaxResult markRead(@PathVariable Long id) {
        return toAjax(eventService.markAsRead(id));
    }

    /** 标记已处理 */
    @PreAuthorize("@ss.hasPermi('risk:event:edit')")
    @Log(title = "事件处理", businessType = BusinessType.UPDATE)
    @PutMapping("/handle/{id}")
    public AjaxResult markHandled(@PathVariable Long id) {
        return toAjax(eventService.markAsHandled(id));
    }

    /** 删除事件 */
    @PreAuthorize("@ss.hasPermi('risk:event:remove')")
    @Log(title = "风险事件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(eventService.deleteRiskEventByIds(ids));
    }

    /** 事件类型统计 */
    @PreAuthorize("@ss.hasPermi('risk:event:list')")
    @GetMapping("/stats/type")
    public AjaxResult typeStats() {
        return success(eventService.selectEventTypeStats());
    }

    /** 导出风险事件 */
    @PreAuthorize("@ss.hasPermi('risk:event:export')")
    @Log(title = "风险事件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RiskEvent event) {
        List<RiskEvent> list = eventService.selectRiskEventList(event);
        ExcelUtil<RiskEvent> util = new ExcelUtil<>(RiskEvent.class);
        util.exportExcel(response, list, "风险事件");
    }
}
