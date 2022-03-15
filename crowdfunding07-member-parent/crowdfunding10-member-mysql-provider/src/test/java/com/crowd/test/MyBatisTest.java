package com.crowd.test;


import com.crowd.entity.po.MemberPO;
import com.crowd.entity.vo.PortalTypeVO;
import com.crowd.mapper.MemberPOMapper;
import com.crowd.mapper.ProjectPOMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBatisTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberPOMapper memberPOMapper;

    private Logger logger = LoggerFactory.getLogger(MyBatisTest.class);

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Test
    public void testConnection() throws SQLException {

        Connection connection = dataSource.getConnection();
        logger.debug(connection.toString());
    }

    @Test
    public void testMysql(){
        memberPOMapper.insert(new MemberPO(null,"jack","123123","接客","jack@qq.com",1,1,"jack","aaa",2));
    }

    @Test
    public void test2(){
        List<PortalTypeVO> portalTypeVOList = projectPOMapper.selectPortalTypeVOList();
        System.out.println(portalTypeVOList.size());
    }
}
