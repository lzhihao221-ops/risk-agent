package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.CapProvision;
import java.util.List;

public interface CapProvisionMapper {
    CapProvision selectById(Long id);
    List<CapProvision> selectList(CapProvision query);
    List<CapProvision> selectByDate(String calcDate);
    int insert(CapProvision record);
    int update(CapProvision record);
    int deleteByIds(Long[] ids);
}
