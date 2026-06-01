package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.RiskMessage;
import java.util.List;

public interface IRiskMessageService {

    RiskMessage selectMessageById(Long id);

    List<RiskMessage> selectMessageList(RiskMessage message);

    List<RiskMessage> selectUnreadByUserId(Long userId);

    int countUnreadByUserId(Long userId);

    int insertMessage(RiskMessage message);

    int markAsRead(Long id);

    int markAllAsRead(Long userId);

    int deleteMessageById(Long id);

    int deleteMessageByIds(Long[] ids);

    /** 发送预警通知给指定用户 */
    void sendAlertNotification(Long userId, Long alertId, String alertTitle, String alertContent);

    /** 发送任务提醒给指定用户 */
    void sendTaskNotification(Long userId, Long taskId, String taskTitle);

    /** 广播系统通知给所有管理员 */
    void broadcastSystemNotice(String title, String content);
}
