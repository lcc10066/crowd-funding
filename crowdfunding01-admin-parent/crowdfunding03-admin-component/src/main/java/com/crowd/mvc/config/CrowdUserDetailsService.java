package com.crowd.mvc.config;

import com.crowd.entity.Admin;
import com.crowd.entity.Role;
import com.crowd.service.api.AdminService;
import com.crowd.service.api.AuthService;
import com.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import sun.dc.pr.PRError;

import java.util.ArrayList;
import java.util.List;

@Component
public class CrowdUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminService.getAdminByLoginAcct(username);
        Integer adminId = admin.getId();
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);
        List<String> assignedAuthNameList = authService.getAssignedAuthNameByAdminId(adminId);

        List<GrantedAuthority> authorities = new ArrayList<>();

        for(Role role:assignedRoleList){
            String roleName = "ROLE_"+role.getName();
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
            authorities.add(simpleGrantedAuthority);
        }
        for(String authName:assignedAuthNameList){
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authName);
            authorities.add(simpleGrantedAuthority);
        }

        SecurityAdmin securityAdmin = new SecurityAdmin(admin, authorities);

        return securityAdmin;
    }
}
