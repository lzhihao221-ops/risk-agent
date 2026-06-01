package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.RiskAlertLog;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 预警记录Mapper接口
 */
public interface RiskAlertLogMapper {

    RiskAlertLog selectRiskAlertLogById(Long id);

    List<RiskAlertLog> selectRiskAlertLogList(RiskAlertLog alertLog);

    int insertRiskAlertLog(RiskAlertLog alertLog);

    int updateRiskAlertLog(RiskAlertLog alertLog);

    int deleteRiskAlertLogByIds(Long[] ids);

    /** 查询未处理预警数量 */
    int selectUnhandledCount();

    /** 按预警等级统计 */
    List<Map<String, Object>> selectAlertLevelStats();

    /** 查询最新预警（首页展示） */
    List<RiskAlertLog> selectLatestAlerts(int limit);

    /** 检查是否存在近期预警（去重） */
    boolean existsRecentAlert(Long companyId, Long ruleId, int hours);
}
