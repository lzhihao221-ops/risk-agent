package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.CapRwa;
import java.util.List;

public interface ICapRwaService {
    CapRwa selectById(Long id);
    List<CapRwa> selectList(CapRwa query);
    int insert(CapRwa record);
    int update(CapRwa record);
    int deleteByIds(Long[] ids);
    List<CapRwa> selectByDate(String calcDate);
}
