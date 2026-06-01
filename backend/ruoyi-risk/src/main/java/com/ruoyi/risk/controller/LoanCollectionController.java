package com.ruoyi.risk.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.risk.domain.LoanCollection;
import com.ruoyi.risk.domain.LoanCollectionLog;
import com.ruoyi.risk.service.ILoanCollectionService;
import com.ruoyi.risk.service.ILoanCollectionLogService;

/**
 * 催收管理Controller
 */
@RestController
@RequestMapping("/loan/collection")
public class LoanCollectionController extends BaseController
{
    @Autowired
    private ILoanCollectionService loanCollectionService;

    @Autowired
    private ILoanCollectionLogService loanCollectionLogService;

    /**
     * 查询催收管理列表
     */
    @PreAuthorize("@ss.hasPermi('loan:collection:list')")
    @GetMapping("/list")
    public TableDataInfo list(LoanCollection loanCollection)
    {
        startPage();
        List<LoanCollection> list = loanCollectionService.selectList(loanCollection);
        return getDataTable(list);
    }

    /**
     * 获取催收管理详细信息（含催收记录）
     */
    @PreAuthorize("@ss.hasPermi('loan:collection:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(loanCollectionService.selectWithLog(id));
    }

    /**
     * 新增催收管理
     */
    @PreAuthorize("@ss.hasPermi('loan:collection:add')")
    @PostMapping
    public AjaxResult add(@RequestBody LoanCollection loanCollection)
    {
        return toAjax(loanCollectionService.insert(loanCollection));
    }

    /**
     * 修改催收管理
     */
    @PreAuthorize("@ss.hasPermi('loan:collection:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody LoanCollection loanCollection)
    {
        return toAjax(loanCollectionService.update(loanCollection));
    }

    /**
     * 删除催收管理
     */
    @PreAuthorize("@ss.hasPermi('loan:collection:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(loanCollectionService.deleteByIds(ids));
    }

    /**
     * 升级催收方式
     */
    @PreAuthorize("@ss.hasPermi('loan:collection:edit')")
    @GetMapping("/approve/{id}")
    public AjaxResult approve(@PathVariable Long id, String collectionType, String nextAction)
    {
        return toAjax(loanCollectionService.approve(id, collectionType, nextAction));
    }

    // ==================== 催收记录 ====================

    /**
     * 查询催收记录列表
     */
    @PreAuthorize("@ss.hasPermi('loan:collection:list')")
    @GetMapping("/log/list/{collectionId}")
    public AjaxResult logList(@PathVariable Long collectionId)
    {
        List<LoanCollectionLog> list = loanCollectionLogService.selectByCollectionId(collectionId);
        return success(list);
    }

    /**
     * 新增催收记录
     */
    @PreAuthorize("@ss.hasPermi('loan:collection:add')")
    @PostMapping("/log")
    public AjaxResult addLog(@RequestBody LoanCollectionLog loanCollectionLog)
    {
        return toAjax(loanCollectionLogService.insert(loanCollectionLog));
    }

    /**
     * 删除催收记录
     */
    @PreAuthorize("@ss.hasPermi('loan:collection:remove')")
    @DeleteMapping("/log/{ids}")
    public AjaxResult removeLog(@PathVariable Long[] ids)
    {
        return toAjax(loanCollectionLogService.deleteByIds(ids));
    }
}
