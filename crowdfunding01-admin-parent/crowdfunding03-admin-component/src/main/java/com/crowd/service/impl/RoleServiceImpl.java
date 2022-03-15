package com.crowd.service.impl;

import com.crowd.entity.Role;
import com.crowd.entity.RoleExample;
import com.crowd.mapper.RoleMapper;
import com.crowd.service.api.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {

        PageHelper.startPage(pageNum,pageSize);

        List<Role> roleList = roleMapper.selectRoleByKeyword(keyword);

        return new PageInfo<>(roleList);
    }

    @Override
    public void saveRole(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }

    @Override
    public void removeRole(List<Integer> roleList) {
        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();
//        where id in (5,10,88)
        criteria.andIdIn(roleList);
        roleMapper.deleteByExample(roleExample);
    }

    @Override
    public List<Role> getAssignedRole(Integer adminId) {

        return roleMapper.selectAssignedRole(adminId);
    }

    @Override
    public List<Role> getUnassignedRole(Integer adminId) {
        return roleMapper.selectUnassignedRole(adminId);
    }
}
