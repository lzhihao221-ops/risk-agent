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
import com.ruoyi.risk.domain.LoanWriteoff;
import com.ruoyi.risk.service.ILoanWriteoffService;

/**
 * 贷款核销Controller
 */
@RestController
@RequestMapping("/loan/writeoff")
public class LoanWriteoffController extends BaseController
{
    @Autowired
    private ILoanWriteoffService loanWriteoffService;

    /**
     * 查询贷款核销列表
     */
    @PreAuthorize("@ss.hasPermi('loan:writeoff:list')")
    @GetMapping("/list")
    public TableDataInfo list(LoanWriteoff loanWriteoff)
    {
        startPage();
        List<LoanWriteoff> list = loanWriteoffService.selectList(loanWriteoff);
        return getDataTable(list);
    }

    /**
     * 获取贷款核销详细信息
     */
    @PreAuthorize("@ss.hasPermi('loan:writeoff:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(loanWriteoffService.selectById(id));
    }

    /**
     * 新增贷款核销
     */
    @PreAuthorize("@ss.hasPermi('loan:writeoff:add')")
    @PostMapping
    public AjaxResult add(@RequestBody LoanWriteoff loanWriteoff)
    {
        return toAjax(loanWriteoffService.insert(loanWriteoff));
    }

    /**
     * 修改贷款核销
     */
    @PreAuthorize("@ss.hasPermi('loan:writeoff:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody LoanWriteoff loanWriteoff)
    {
        return toAjax(loanWriteoffService.update(loanWriteoff));
    }

    /**
     * 删除贷款核销
     */
    @PreAuthorize("@ss.hasPermi('loan:writeoff:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(loanWriteoffService.deleteByIds(ids));
    }

    /**
     * 审批核销
     */
    @PreAuthorize("@ss.hasPermi('loan:writeoff:edit')")
    @GetMapping("/approve/{id}")
    public AjaxResult approve(@PathVariable Long id, String approveUser, String approveOpinion)
    {
        return toAjax(loanWriteoffService.approve(id, approveUser, approveOpinion));
    }

    /**
     * 驳回核销
     */
    @PreAuthorize("@ss.hasPermi('loan:writeoff:edit')")
    @GetMapping("/reject/{id}")
    public AjaxResult reject(@PathVariable Long id, String approveUser, String approveOpinion)
    {
        return toAjax(loanWriteoffService.reject(id, approveUser, approveOpinion));
    }
}
