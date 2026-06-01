package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.RiskEvent;
import java.util.List;
import java.util.Map;

/**
 * 风险事件Mapper接口
 */
public interface RiskEventMapper {

    RiskEvent selectRiskEventById(Long id);

    List<RiskEvent> selectRiskEventList(RiskEvent event);

    int insertRiskEvent(RiskEvent event);

    int updateRiskEvent(RiskEvent event);

    int deleteRiskEventByIds(Long[] ids);

    /** 根据企业ID查询事件 */
    List<RiskEvent> selectByCompanyId(Long companyId);

    /** 按事件类型统计 */
    List<Map<String, Object>> selectEventTypeStats();

    /** 按日期统计预警数量（近30天） */
    List<Map<String, Object>> selectDailyAlertStats(int days);

    /** 查询未读事件数量 */
    int selectUnreadCount();
}
