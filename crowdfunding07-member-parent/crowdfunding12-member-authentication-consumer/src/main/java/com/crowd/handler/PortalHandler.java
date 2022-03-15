package com.crowd.handler;


import com.crowd.api.MySQLRemoteService;
import com.crowd.constant.CrowdConstant;
import com.crowd.entity.vo.PortalTypeVO;
import com.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PortalHandler {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/")
    public String showProtalPage(Model model){
//        获取主页数据的代码

//        此处把首页展示的所需要的数据放置request中，使用thymeleaf在后端解析好数据，再将HTML页面返回至浏览器
        ResultEntity<List<PortalTypeVO>> portalTypeProjectDataRemote = mySQLRemoteService.getPortalTypeProjectDataRemote();
        if(ResultEntity.SUCCESS.equals(portalTypeProjectDataRemote.getResult())){

            List<PortalTypeVO> data = portalTypeProjectDataRemote.getData();
            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA,data);
        }
        return "portal";
    }

}
