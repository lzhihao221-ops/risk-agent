package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.CapRwa;
import java.util.List;

public interface CapRwaMapper {
    CapRwa selectById(Long id);
    List<CapRwa> selectList(CapRwa query);
    List<CapRwa> selectByDate(String calcDate);
    int insert(CapRwa record);
    int update(CapRwa record);
    int deleteByIds(Long[] ids);
}
