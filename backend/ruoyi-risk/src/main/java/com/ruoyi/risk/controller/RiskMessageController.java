package com.ruoyi.risk.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.risk.domain.RiskMessage;
import com.ruoyi.risk.service.IRiskMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 站内消息 Controller
 */
@RestController
@RequestMapping("/risk/message")
public class RiskMessageController extends BaseController {

    @Autowired
    private IRiskMessageService messageService;

    /** 消息列表 */
    @PreAuthorize("@ss.hasPermi('risk:message:list')")
    @GetMapping("/list")
    public TableDataInfo list(RiskMessage message) {
        startPage();
        message.setUserId(getUserId());
        List<RiskMessage> list = messageService.selectMessageList(message);
        return getDataTable(list);
    }

    /** 未读消息 */
    @PreAuthorize("@ss.hasPermi('risk:message:list')")
    @GetMapping("/unread")
    public AjaxResult unread() {
        Long userId = getUserId();
        List<RiskMessage> list = messageService.selectUnreadByUserId(userId);
        int count = messageService.countUnreadByUserId(userId);
        AjaxResult result = success(list);
        result.put("unreadCount", count);
        return result;
    }

    /** 未读数量 */
    @PreAuthorize("@ss.hasPermi('risk:message:list')")
    @GetMapping("/unreadCount")
    public AjaxResult unreadCount() {
        return success(messageService.countUnreadByUserId(getUserId()));
    }

    /** 标记已读 */
    @PreAuthorize("@ss.hasPermi('risk:message:edit')")
    @PutMapping("/read/{id}")
    public AjaxResult markRead(@PathVariable Long id) {
        return toAjax(messageService.markAsRead(id));
    }

    /** 全部已读 */
    @PreAuthorize("@ss.hasPermi('risk:message:edit')")
    @PutMapping("/readAll")
    public AjaxResult markAllRead() {
        return toAjax(messageService.markAllAsRead(getUserId()));
    }

    /** 删除消息 */
    @PreAuthorize("@ss.hasPermi('risk:message:remove')")
    @Log(title = "消息管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(messageService.deleteMessageByIds(ids));
    }
}
