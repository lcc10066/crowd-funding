package com.crowd.service.impl;

import com.crowd.entity.Menu;
import com.crowd.entity.MenuExample;
import com.crowd.mapper.MenuMapper;
import com.crowd.service.api.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getAll() {
        List<Menu> menuList = menuMapper.selectByExample(new MenuExample());
        return menuList;
    }

    @Override
    public void saveMenu(Menu menu) {
        menuMapper.insert(menu);
    }

    @Override
    public void updateMenu(Menu menu) {
//        由于PID没有传入，一定要有选择的更新，保证pid不会被置空
        menuMapper.updateByPrimaryKeySelective(menu);
    }

    @Override
    public void removeMenu(Integer id) {
        menuMapper.deleteByPrimaryKey(id);
    }
}
