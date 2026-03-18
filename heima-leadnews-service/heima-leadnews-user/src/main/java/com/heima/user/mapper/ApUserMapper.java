package com.heima.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.heima.model.user.pojos.ApUser;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ApUserMapper extends BaseMapper<ApUser> {
    @Override
    int insert(ApUser entity);

    @Override
    int deleteById(Serializable id);

    @Override
    int deleteByMap(Map<String, Object> columnMap);

    @Override
    int delete(Wrapper<ApUser> queryWrapper);

    @Override
    int deleteBatchIds(Collection<? extends Serializable> idList);

    @Override
    int updateById(ApUser entity);

    @Override
    int update(ApUser entity, Wrapper<ApUser> updateWrapper);

    @Override
    ApUser selectById(Serializable id);

    @Override
    List<ApUser> selectBatchIds(Collection<? extends Serializable> idList);

    @Override
    List<ApUser> selectByMap(Map<String, Object> columnMap);

    @Override
    ApUser selectOne(Wrapper<ApUser> queryWrapper);

    @Override
    Integer selectCount(Wrapper<ApUser> queryWrapper);

    @Override
    List<ApUser> selectList(Wrapper<ApUser> queryWrapper);

    @Override
    List<Map<String, Object>> selectMaps(Wrapper<ApUser> queryWrapper);

    @Override
    List<Object> selectObjs(Wrapper<ApUser> queryWrapper);

    @Override
    <E extends IPage<ApUser>> E selectPage(E page, Wrapper<ApUser> queryWrapper);

    @Override
    <E extends IPage<Map<String, Object>>> E selectMapsPage(E page, Wrapper<ApUser> queryWrapper);
}
