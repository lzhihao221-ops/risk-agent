package com.ruoyi.risk.service;

import com.ruoyi.risk.domain.CapEcl;
import java.util.List;

public interface ICapEclService {
    CapEcl selectById(Long id);
    List<CapEcl> selectList(CapEcl query);
    int insert(CapEcl record);
    int update(CapEcl record);
    int deleteByIds(Long[] ids);
}
