package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.GisBranch;
import com.ruoyi.risk.domain.GisCheckinRecord;
import com.ruoyi.risk.domain.GisCustomerRegion;
import com.ruoyi.risk.domain.GisRiskPoint;
import com.ruoyi.risk.mapper.GisMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GisService {

    private static final Logger log = LoggerFactory.getLogger(GisService.class);

    @Autowired
    private GisMapper gisMapper;

    /** 获取网点位置 */
    public List<Map<String, Object>> getBranchLocations() {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            List<GisBranch> branches = gisMapper.selectBranchList();
            for (GisBranch b : branches) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", b.getId());
                map.put("name", b.getBranchName());
                map.put("code", b.getBranchCode());
                map.put("type", b.getBranchType());
                map.put("longitude", b.getLongitude());
                map.put("latitude", b.getLatitude());
                map.put("address", b.getProvince() + b.getCity() + b.getDistrict() + b.getAddress());
                map.put("phone", b.getPhone());
                map.put("manager", b.getManager());
                map.put("staffCount", b.getStaffCount());
                result.add(map);
            }
        } catch (Exception e) {
            log.error("查询网点数据失败", e);
        }
        return result;
    }

    /** 获取客户分布 */
    public List<Map<String, Object>> getCustomerDistribution() {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            List<GisCustomerRegion> regions = gisMapper.selectCustomerRegionList();
            for (GisCustomerRegion r : regions) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", r.getId());
                map.put("region", r.getRegionName());
                map.put("longitude", r.getLongitude());
                map.put("latitude", r.getLatitude());
                map.put("customerCount", r.getCustomerCount());
                map.put("loanCount", r.getLoanCount());
                map.put("loanAmount", r.getLoanAmount());
                map.put("status", r.getRiskLevel());
                result.add(map);
            }
        } catch (Exception e) {
            log.error("查询客户分布数据失败", e);
        }
        return result;
    }

    /** 获取风险热力图数据 */
    public List<Map<String, Object>> getRiskHeatmapData() {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            List<GisRiskPoint> points = gisMapper.selectRiskPointList();
            for (GisRiskPoint p : points) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", p.getId());
                map.put("name", p.getPointName());
                map.put("longitude", p.getLongitude());
                map.put("latitude", p.getLatitude());
                map.put("type", p.getRiskType());
                map.put("level", p.getRiskLevel());
                map.put("intensity", p.getIntensity());
                map.put("amount", p.getAmount());
                map.put("company", p.getCompanyName());
                map.put("description", p.getDescription());
                result.add(map);
            }
        } catch (Exception e) {
            log.error("查询风险热力图数据失败", e);
        }
        return result;
    }

    /** 外勤打卡 */
    public String checkin(Map<String, Object> params) {
        try {
            GisCheckinRecord record = new GisCheckinRecord();
            record.setUserId(params.get("userId") != null ? Long.parseLong(params.get("userId").toString()) : 1L);
            record.setUserName((String) params.get("userName"));
            record.setLongitude(((Number) params.get("longitude")).doubleValue());
            record.setLatitude(((Number) params.get("latitude")).doubleValue());
            record.setAddress((String) params.get("address"));
            record.setCheckinType((String) params.get("checkinType"));
            record.setCheckinTime(new Date());
            record.setCompanyName((String) params.get("companyName"));
            record.setRemark((String) params.get("remark"));
            record.setStatus("0");
            gisMapper.insertCheckinRecord(record);
            log.info("用户 {} 在 ({}, {}) 打卡成功", record.getUserName(), record.getLongitude(), record.getLatitude());
            return "打卡成功！位置: " + record.getAddress();
        } catch (Exception e) {
            log.error("打卡失败", e);
            throw new RuntimeException("打卡失败: " + e.getMessage());
        }
    }

    /** 获取打卡记录 */
    public List<Map<String, Object>> getCheckinRecords(String userId, String startDate, String endDate) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Long uid = (userId != null && !userId.isEmpty()) ? Long.parseLong(userId) : null;
            List<GisCheckinRecord> records = gisMapper.selectCheckinRecordList(uid, startDate, endDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (GisCheckinRecord r : records) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", r.getId());
                map.put("user", r.getUserName());
                map.put("longitude", r.getLongitude());
                map.put("latitude", r.getLatitude());
                map.put("address", r.getAddress());
                map.put("time", r.getCheckinTime() != null ? sdf.format(r.getCheckinTime()) : "");
                map.put("type", r.getCheckinType());
                map.put("company", r.getCompanyName());
                map.put("remark", r.getRemark());
                result.add(map);
            }
        } catch (Exception e) {
            log.error("查询打卡记录失败", e);
        }
        return result;
    }
}
