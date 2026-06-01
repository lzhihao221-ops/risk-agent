package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.CapProvision;
import java.util.List;

public interface ICapProvisionService {
    CapProvision selectById(Long id);
    List<CapProvision> selectList(CapProvision query);
    int insert(CapProvision record);
    int update(CapProvision record);
    int deleteByIds(Long[] ids);
}
