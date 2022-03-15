package com.crowd.mvc.handler;

import com.crowd.entity.Role;
import com.crowd.service.api.RoleService;
import com.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RoleHandler {

    @Autowired
    private RoleService roleService;



    @PreAuthorize("hasRole('部长')") //权限控制，同时在WebAppSecurityConfig配置类上添加注解@EnableGlobalMethodSecurity(prePostEnabled = true)
    @ResponseBody
    @RequestMapping("/role/get/page/info.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
            @RequestParam(value = "keyword",defaultValue = "") String keyword
    ){

        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);

        return ResultEntity.successWithData(pageInfo);
    }


    @ResponseBody
    @RequestMapping("/role/save.json")
    public ResultEntity<String> saveRole(
//            @RequestParam("roleName") String roleName
            Role role   //或者直接用对象接收，需要将模态框jsp页面的字段名改为与Role类中一致（name）
    ){
        roleService.saveRole(role);
        ResultEntity<String> resultEntity = ResultEntity.successWithoutData();
        return resultEntity;
    }

    @ResponseBody
    @RequestMapping("/role/update.json")
    public ResultEntity<String> updateRole(Role role){
        roleService.updateRole(role);
        ResultEntity<String> resultEntity = ResultEntity.successWithoutData();
        return resultEntity;
    }

    @ResponseBody
    @RequestMapping("/role/remove/by/role/id/array.json")
    public ResultEntity<String> removeByRoleArray(@RequestBody List<Integer> list){
        roleService.removeRole(list);
        return ResultEntity.successWithoutData();
    }
}
