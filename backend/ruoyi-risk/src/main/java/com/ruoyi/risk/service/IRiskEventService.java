package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.RiskEvent;
import java.util.List;
import java.util.Map;

/**
 * 风险事件 服务接口
 */
public interface IRiskEventService {

    RiskEvent selectRiskEventById(Long id);

    List<RiskEvent> selectRiskEventList(RiskEvent event);

    int insertRiskEvent(RiskEvent event);

    int updateRiskEvent(RiskEvent event);

    int deleteRiskEventByIds(Long[] ids);

    /** 根据企业ID查询事件 */
    List<RiskEvent> selectByCompanyId(Long companyId);

    /** 按事件类型统计 */
    List<Map<String, Object>> selectEventTypeStats();

    /** 按日期统计预警数量 */
    List<Map<String, Object>> selectDailyAlertStats(int days);

    /** 未读事件数量 */
    int selectUnreadCount();

    /** 标记已读 */
    int markAsRead(Long id);

    /** 标记已处理 */
    int markAsHandled(Long id);
}
