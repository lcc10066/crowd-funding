package com.crowd.mapper;

import com.crowd.entity.Admin;
import com.crowd.entity.AdminExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminMapper {
    long countByExample(AdminExample example);

    int deleteByExample(AdminExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    List<Admin> selectByExample(AdminExample example);

    Admin selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByExample(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

//    为了实现PageHelper的分页查询而实现的方法，在xml配置文件中也需要添加相应的sql语句
    List<Admin> selectAdminListByKeyword(String keyword);

    void deleteOldRelationship(Integer adminId);

    void insertNewRelationship(@Param("adminId")Integer adminId,@Param("roleIdList") List<Integer> roleIdList);
}