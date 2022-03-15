package com.crowd.service.api;

import com.crowd.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RoleService {
    PageInfo<Role> getPageInfo(Integer pageNum,Integer pageSize,String keyword);

    void saveRole(Role role);

    void updateRole(Role role);

    void removeRole(List<Integer> roleList);

    List<Role> getAssignedRole(Integer adminId);

    List<Role> getUnassignedRole(Integer adminId);
}
