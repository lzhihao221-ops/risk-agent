package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.GisBranch;
import com.ruoyi.risk.domain.GisCheckinRecord;
import com.ruoyi.risk.domain.GisCustomerRegion;
import com.ruoyi.risk.domain.GisRiskPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface GisMapper {
    List<GisBranch> selectBranchList();
    List<GisCustomerRegion> selectCustomerRegionList();
    List<GisRiskPoint> selectRiskPointList();
    List<GisCheckinRecord> selectCheckinRecordList(@Param("userId") Long userId, @Param("startDate") String startDate, @Param("endDate") String endDate);
    int insertCheckinRecord(GisCheckinRecord record);
}
