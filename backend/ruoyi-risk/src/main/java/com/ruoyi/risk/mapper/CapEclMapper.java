package com.ruoyi.risk.mapper;

import com.ruoyi.risk.domain.CapEcl;
import java.util.List;

public interface CapEclMapper {
    CapEcl selectById(Long id);
    List<CapEcl> selectList(CapEcl query);
    List<CapEcl> selectByDate(String calcDate);
    int insert(CapEcl record);
    int update(CapEcl record);
    int deleteByIds(Long[] ids);
}
