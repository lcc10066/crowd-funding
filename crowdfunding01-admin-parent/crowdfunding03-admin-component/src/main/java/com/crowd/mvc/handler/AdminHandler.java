package com.crowd.mvc.handler;

import com.crowd.constant.CrowdConstant;
import com.crowd.entity.Admin;
import com.crowd.exception.LoginFailedException;
import com.crowd.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AdminHandler {
    @Autowired
    private AdminService adminService;

    @RequestMapping("/admin/do/login.html")
    public String doLogin(@RequestParam("loginAcct") String loginAcct, @RequestParam("userPswd") String userPswd,
                          HttpSession session
                          ){

        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN , admin);


//        避免刷新页面时重复提交表单，且客户端浏览器不能直接访问WEB-INF下的内容，此处使用MVC控制器来进处理
        return "redirect:/admin/to/main/page.html";
    }

    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session){
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }

    @RequestMapping("/admin/get/page.html")
    public String askPageInfo(
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
//            此处的defaultValue类型为String，只能将默认的1 放至字符串中
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
            ModelMap map
    ){
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        map.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);
        return "admin-page";
    }

//    @RequestMapping("/assign/to/assign/role/page.html")
//    public String


//    此处keyword应当放到最后一个参数，因为前两者一定存在，但keyword不一定有，若将keyword放置第二个参数，且其没有传递过来时
//    无法正确的匹配mapping， eg: /admin/remove/1//5.html
    @RequestMapping("/admin/remove/{id}/{pageNum}/{keyword}.html")
    public String remove(@PathVariable("id") Integer id,@PathVariable("pageNum") Integer pageNum,@PathVariable("keyword") String keyword){
        Integer res = adminService.remove(id);

        return "redirect:/admin/get/page.html?keyword="+keyword+"&pageNum="+pageNum;
    }


    @PreAuthorize("hasAuthority('user:save')")
    @RequestMapping("/admin/save.html")
    public String saveAdmin(Admin admin){
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }

    @RequestMapping("/admin/to/edit/page.html")
    public String getEditData(@RequestParam("adminId") Integer id,
//                       @RequestParam("pageNum") Integer pageNum,
//                       @RequestParam(value = "keyword",defaultValue = "") String keyword,
                       ModelMap map
    ){
        Admin adim = adminService.getAdimById(id);
        map.addAttribute("admin",adim);
//        由于此处使用请求转发，到达admin-edit页面后request对象任然为当前请求中的request对象，其中的pageNum keyword还在
        return "admin-edit";
    }


    @RequestMapping("/admin/update.html")
    public String update(Admin admin,
                         @RequestParam("pageNum") Integer pageNum,
                         @RequestParam(value = "keyword",defaultValue = "") String keyword
    ){
        adminService.update(admin);
        return "redirect:/admin/get/page.html?keyword="+keyword+"&pageNum="+pageNum;
    }
}
