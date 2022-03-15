package com.crowd.service.impl;

import com.crowd.entity.Auth;
import com.crowd.entity.AuthExample;
import com.crowd.mapper.AuthMapper;
import com.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Override
    public List<Auth> getAll() {
        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
        return authMapper.selectAssignedAuthIdByRoleId(roleId);
    }

    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {
        List<Integer> roleIdList = map.get("roleId");
        Integer roleId = roleIdList.get(0);

        authMapper.deleteOldRelationship(roleId);

        List<Integer> authIdArray = map.get("authIdArray");
        if(authIdArray != null && authIdArray.size()>0)
            authMapper.insertNewRelationship(roleId,authIdArray);
    }

    @Override
    public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
        return authMapper.selectAssignedAuthNameByAdminId(adminId);
    }
}
