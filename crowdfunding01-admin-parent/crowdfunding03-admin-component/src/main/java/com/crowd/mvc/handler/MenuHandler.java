package com.crowd.mvc.handler;

import com.crowd.entity.Menu;
import com.crowd.service.api.MenuService;
import com.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
//@RestController   相当于Controller +  ResponseBody，只需要在类上加一次，该类中所有方法都将返回值转化为JSON
public class MenuHandler {
    @Autowired
    private MenuService menuService;

    @ResponseBody
    @RequestMapping("/menu/remove.json")
    public ResultEntity<String> removeMenu(@RequestParam("id")Integer id){
        menuService.removeMenu(id);
        return ResultEntity.successWithoutData();
    }



    @ResponseBody
    @RequestMapping("/menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu){
        menuService.updateMenu(menu);
        return ResultEntity.successWithoutData();
    }


    @ResponseBody
    @RequestMapping("/menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu){
        menuService.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }
    @ResponseBody
    @RequestMapping("/menu/get/whole/tree")
    public ResultEntity<Menu> getWholeTree(){
        List<Menu> menuList = menuService.getAll();
//        此时list中的每一个menu的childrenList内还没有元素
        Menu root = null;
        Map<Integer,Menu> map = new HashMap<>();
        for(Menu menu:menuList){
            Integer id = menu.getId();
            map.put(id,menu);
        }
        for(Menu menu:menuList){
            Integer pid = menu.getPid();
            if(pid == null)
            {
                root = menu;
                continue;
            }
            map.get(pid).getChildren().add(menu);
        }
//        直接返回根节点即可
        return ResultEntity.successWithData(root);
    }




//时间复杂度很高
    public ResultEntity<Menu> getWholeTreeOld(){

        List<Menu> menuList = menuService.getAll();

//        此时list中的每一个menu的childrenList内还没有元素

        Menu root = null;

        for (Menu menu:menuList){
            Integer pid = menu.getPid();
            if(pid == null){
                root = menu;
                continue;
            }

            for(Menu father:menuList){
                Integer id = father.getId();
                if(Objects.equals(id,pid))
                {
                    father.getChildren().add(menu);
                    break;
                }
            }
        }
        return ResultEntity.successWithData(root);
    }
}
