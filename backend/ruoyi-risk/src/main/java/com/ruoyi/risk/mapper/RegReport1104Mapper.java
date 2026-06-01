package com.ruoyi.risk.mapper;

import java.util.List;
import com.ruoyi.risk.domain.RegReport1104;

public interface RegReport1104Mapper {
    public RegReport1104 selectById(Long id);
    public List<RegReport1104> selectList(RegReport1104 regReport1104);
    public int insert(RegReport1104 regReport1104);
    public int update(RegReport1104 regReport1104);
    public int deleteByIds(Long[] ids);
}
