package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.RiskMessage;
import com.ruoyi.risk.mapper.RiskMessageMapper;
import com.ruoyi.risk.service.IRiskMessageService;
import com.ruoyi.risk.mapper.RiskCompanyMapper;
import com.ruoyi.risk.domain.RiskCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskMessageServiceImpl implements IRiskMessageService {

    @Autowired
    private RiskMessageMapper messageMapper;

    @Autowired
    private RiskCompanyMapper companyMapper;

    @Override
    public RiskMessage selectMessageById(Long id) {
        return messageMapper.selectMessageById(id);
    }

    @Override
    public List<RiskMessage> selectMessageList(RiskMessage message) {
        return messageMapper.selectMessageList(message);
    }

    @Override
    public List<RiskMessage> selectUnreadByUserId(Long userId) {
        return messageMapper.selectUnreadByUserId(userId);
    }

    @Override
    public int countUnreadByUserId(Long userId) {
        return messageMapper.countUnreadByUserId(userId);
    }

    @Override
    public int insertMessage(RiskMessage message) {
        return messageMapper.insertMessage(message);
    }

    @Override
    public int markAsRead(Long id) {
        return messageMapper.markAsRead(id);
    }

    @Override
    public int markAllAsRead(Long userId) {
        return messageMapper.markAllAsRead(userId);
    }

    @Override
    public int deleteMessageById(Long id) {
        return messageMapper.deleteMessageById(id);
    }

    @Override
    public int deleteMessageByIds(Long[] ids) {
        return messageMapper.deleteMessageByIds(ids);
    }

    @Override
    public void sendAlertNotification(Long userId, Long alertId, String alertTitle, String alertContent) {
        RiskMessage msg = new RiskMessage();
        msg.setUserId(userId);
        msg.setTitle("【预警通知】" + alertTitle);
        msg.setContent(alertContent);
        msg.setMsgType(1);
        msg.setRefType("alert");
        msg.setRefId(alertId);
        messageMapper.insertMessage(msg);
    }

    @Override
    public void sendTaskNotification(Long userId, Long taskId, String taskTitle) {
        RiskMessage msg = new RiskMessage();
        msg.setUserId(userId);
        msg.setTitle("【任务提醒】" + taskTitle);
        msg.setContent("您有一个新的排查任务需要处理：" + taskTitle);
        msg.setMsgType(2);
        msg.setRefType("task");
        msg.setRefId(taskId);
        messageMapper.insertMessage(msg);
    }

    @Override
    public void broadcastSystemNotice(String title, String content) {
        // 简化实现：向所有企业对应的管户经理发送通知
        // 实际应查询用户表，这里仅做演示
        List<RiskCompany> companies = companyMapper.selectRiskCompanyList(new RiskCompany());
        for (RiskCompany company : companies) {
            if (company.getManagerId() != null) {
                RiskMessage msg = new RiskMessage();
                msg.setUserId(company.getManagerId());
                msg.setTitle("【系统通知】" + title);
                msg.setContent(content);
                msg.setMsgType(3);
                msg.setRefType("system");
                messageMapper.insertMessage(msg);
            }
        }
    }
}
