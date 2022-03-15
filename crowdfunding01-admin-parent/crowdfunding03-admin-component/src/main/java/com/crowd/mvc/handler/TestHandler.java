package com.crowd.mvc.handler;

import com.crowd.entity.Admin;
import com.crowd.service.api.AdminService;
import com.crowd.util.CrowdUtil;
import com.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class TestHandler {
    @Autowired
    private AdminService adminService;

    @RequestMapping("/test/ssm.html")
    public String testHandler(ModelMap map, HttpServletRequest request){

        /**
         * 该请求不是ajax请求  为false
         */
        boolean isJson = CrowdUtil.judgeRequestType(request);

        System.out.println("------------------"+isJson+"------------------");

        List<Admin> admins = adminService.getAll();
        map.addAttribute("adminList",admins);
//        int a = 10/0;

        throw new NullPointerException();
//        return "target";
    }
    /*
    向request域共享数据：
            1.  servlet API  request.setAttribute("testScope", "hello,servletAPI"); return "success";
            2.  ModelAndView
            3.  Model
            4.  map
            5.  ModelMap
     向session、application域共享数据
            session.setAttribute("testSessionScope", "hello,session");
            ServletContext application = session.getServletContext();
            application.setAttribute("testApplicationScope", "hello,application");
            return "success";
     */

    @RequestMapping("/send/array/btn1.html")
    @ResponseBody
    public String testReceiveArray(@RequestParam("array[]") List<Integer> list){
        for(Integer integer:list)
            System.out.println(integer);
        return "responseBody";
    }

    @RequestMapping("/send/array/btn2.json")
    @ResponseBody
    public String testAjaxJson(@RequestBody List<Integer> list){
        Logger logger = LoggerFactory.getLogger(TestHandler.class);

        for (Integer integer:list)
            logger.info(integer+"");
        return "success";
    }
//
//    @RequestMapping("/send/array/object.json")
//    @ResponseBody
//    public String testAjaxJsonObject(@RequestBody Student student,HttpServletRequest request){
//
//        /**
//         * 测试该请求是否为Ajax请求   结果为true
//         */
//        boolean isJson = CrowdUtil.judgeRequestType(request);
//
//        System.out.println("------------------"+isJson+"------------------");
//
//        Logger logger = LoggerFactory.getLogger(TestHandler.class);
//        logger.info(student.toString());
//        return "success";
//    }
//
//    @RequestMapping("/send/array/AjaxEntity.sss")
//    @ResponseBody
//    public ResultEntity<Student> testAjaxJsonObject2(@RequestBody Student student){
//        ResultEntity<Student> resultEntity = ResultEntity.successWithData(student);
//        return resultEntity;
//    }
//



}
