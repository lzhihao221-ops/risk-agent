package com.ruoyi.risk.service.impl;

import com.ruoyi.risk.domain.RiskEvent;
import com.ruoyi.risk.mapper.RiskEventMapper;
import com.ruoyi.risk.service.IRiskAlertService;
import com.ruoyi.risk.service.IRiskEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 风险事件 服务实现
 */
@Service
public class RiskEventServiceImpl implements IRiskEventService {

    @Autowired
    private RiskEventMapper eventMapper;

    @Autowired
    private IRiskAlertService alertService;

    @Override
    public RiskEvent selectRiskEventById(Long id) {
        return eventMapper.selectRiskEventById(id);
    }

    @Override
    public List<RiskEvent> selectRiskEventList(RiskEvent event) {
        return eventMapper.selectRiskEventList(event);
    }

    @Override
    @Transactional
    public int insertRiskEvent(RiskEvent event) {
        int rows = eventMapper.insertRiskEvent(event);
        // 插入事件后，自动检查是否触发预警
        if (rows > 0 && event.getId() != null) {
            alertService.checkAndTriggerAlert(
                event.getCompanyId(),
                event.getEventType(),
                event.getId(),
                event.getEventTitle()
            );
        }
        return rows;
    }

    @Override
    public int updateRiskEvent(RiskEvent event) {
        return eventMapper.updateRiskEvent(event);
    }

    @Override
    public int deleteRiskEventByIds(Long[] ids) {
        return eventMapper.deleteRiskEventByIds(ids);
    }

    @Override
    public List<RiskEvent> selectByCompanyId(Long companyId) {
        return eventMapper.selectByCompanyId(companyId);
    }

    @Override
    public List<Map<String, Object>> selectEventTypeStats() {
        return eventMapper.selectEventTypeStats();
    }

    @Override
    public List<Map<String, Object>> selectDailyAlertStats(int days) {
        return eventMapper.selectDailyAlertStats(days);
    }

    @Override
    public int selectUnreadCount() {
        return eventMapper.selectUnreadCount();
    }

    @Override
    public int markAsRead(Long id) {
        RiskEvent event = new RiskEvent();
        event.setId(id);
        event.setIsRead(1);
        return eventMapper.updateRiskEvent(event);
    }

    @Override
    public int markAsHandled(Long id) {
        RiskEvent event = new RiskEvent();
        event.setId(id);
        event.setIsHandled(1);
        return eventMapper.updateRiskEvent(event);
    }
}
