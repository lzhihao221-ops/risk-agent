package com.ruoyi.risk.mapper;

import java.util.List;
import com.ruoyi.risk.domain.RegIndicator;

public interface RegIndicatorMapper {
    public RegIndicator selectById(Long id);
    public List<RegIndicator> selectList(RegIndicator regIndicator);
    public List<RegIndicator> selectByType(String indicatorType);
    public int insert(RegIndicator regIndicator);
    public int update(RegIndicator regIndicator);
    public int deleteByIds(Long[] ids);
}
