package com.crowd.mvc.handler;

import com.crowd.entity.Auth;
import com.crowd.entity.Role;
import com.crowd.service.api.AdminService;
import com.crowd.service.api.AuthService;
import com.crowd.service.api.RoleService;
import com.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class AssignHandler {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;


    @ResponseBody
    @RequestMapping("/assign/do/role/assign/auth.json")
    public ResultEntity<String> saveRoleAuthRelationship(@RequestBody Map<String ,List<Integer>> map){
        authService.saveRoleAuthRelationship(map);
        return ResultEntity.successWithoutData();
    }





    @ResponseBody
    @RequestMapping("/assign/get/assigned/auth/id/by/role/id.json")
    public ResultEntity<List<Integer>> getAssignedAuthIdByRoleId(@RequestParam("roleId")Integer roleId){

        List<Integer> authIdList =  authService.getAssignedAuthIdByRoleId(roleId);
        return ResultEntity.successWithData(authIdList);
    }

//    使用ZTree的简单JSON解析格式，将list自动解析为JSON
    @ResponseBody
    @RequestMapping("/assign/get/all/auth.json")
    public ResultEntity<List<Auth>> getAllAuth(){

        List<Auth> authList = authService.getAll();
        return ResultEntity.successWithData(authList);
    }



    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAssignRolePage(@RequestParam("adminId")Integer adminId, ModelMap map){

        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);
        List<Role> unAssignedRoleList = roleService.getUnassignedRole(adminId);
        map.addAttribute("assignedRoleList",assignedRoleList);
        map.addAttribute("unAssignedRoleList",unAssignedRoleList);
        return "assign-role";
    }


    @RequestMapping("/assign/do/role/assign.html")
    public String saveAdminRoleRelationShip(
            @RequestParam("adminId")Integer adminId,
            @RequestParam("pageNum")Integer pageNum,
            @RequestParam("keyword")String keyword,
//          允许用户在页面上取消所有role进行提交
//          required = false表示这个参数不是必须的
            @RequestParam(value = "roleIdList",required = false)List<Integer> roleIdList
    ){

        adminService.saveAdminRoleRelationship(adminId,roleIdList);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }


}
