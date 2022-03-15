package com.lcc.crowd;


import com.crowd.entity.Admin;
import com.crowd.entity.Role;
import com.crowd.mapper.AdminMapper;
import com.crowd.mapper.RoleMapper;
import com.crowd.service.api.AdminService;
import com.crowd.util.CrowdUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;


    @Autowired
    private RoleMapper roleMapper;
    @Test
    public void testAdminService(){
        Admin admin = new Admin(null,"Jack","123123","盖唧","tom.qq.com",null);
        adminService.saveAdmin(admin);
    }

    @Test
    public void testMd5(){
        System.out.println(CrowdUtil.md5("123123"));
    }

    @Test
    public void addTestData(){
        for(int i=1;i<101;++i)
            adminService.saveAdmin(new Admin(null,"lcc"+i,"1111","鲁大材"+i,"10066@qq.com",null));
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testInsertAdmin(){
        Admin admin = new Admin(null,"tom","123123","鲁昶材","tom.qq.com",null);
        int count = adminMapper.insert(admin);
        System.out.println(count);
    }


    @Test
    public void testInsertRole(){
//        for(int i = 1; i<=200 ; i++)
//        {
//
//            Role role = new Role(null,"营销管理"+i);
//            roleMapper.insert(role);
//        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123123"));
    }

    @Test
    public void testLog(){
//      slf4j定义的接口  org.slf4j.Logger
//        getLogger()的参数定义了打印改日志信息的类
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);

//        debug->info->warn->error
//        可以在配置文件中配置日志级别，只会打印当前级别和当前级别以上的日志级别
        logger.debug("debug 111111");
        logger.info("info 222222");
        logger.warn("warn 33333");
        logger.error("error 444444");
    }

}
