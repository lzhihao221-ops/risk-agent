package com.ruoyi.risk.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.risk.domain.RiskCheckTask;
import com.ruoyi.risk.service.IRiskTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 排查任务 Controller
 */
@RestController
@RequestMapping("/risk/task")
public class RiskTaskController extends BaseController {

    @Autowired
    private IRiskTaskService taskService;

    /** 任务列表 */
    @PreAuthorize("@ss.hasPermi('risk:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(RiskCheckTask task) {
        startPage();
        List<RiskCheckTask> list = taskService.selectTaskList(task);
        return getDataTable(list);
    }

    /** 任务详情 */
    @PreAuthorize("@ss.hasPermi('risk:task:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(taskService.selectTaskById(id));
    }

    /** 新增任务 */
    @PreAuthorize("@ss.hasPermi('risk:task:add')")
    @Log(title = "排查任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RiskCheckTask task) {
        return toAjax(taskService.insertTask(task));
    }

    /** 修改任务 */
    @PreAuthorize("@ss.hasPermi('risk:task:edit')")
    @Log(title = "排查任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RiskCheckTask task) {
        return toAjax(taskService.updateTask(task));
    }

    /** 完成任务 */
    @PreAuthorize("@ss.hasPermi('risk:task:edit')")
    @Log(title = "任务完成", businessType = BusinessType.UPDATE)
    @PutMapping("/complete/{id}")
    public AjaxResult complete(@PathVariable Long id, @RequestParam(required = false) String checkResult, @RequestParam(required = false) String checkReport) {
        return toAjax(taskService.completeTask(id, checkResult, checkReport));
    }

    /** 删除任务 */
    @PreAuthorize("@ss.hasPermi('risk:task:remove')")
    @Log(title = "排查任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(taskService.deleteTaskByIds(new Long[]{id}));
    }
}
