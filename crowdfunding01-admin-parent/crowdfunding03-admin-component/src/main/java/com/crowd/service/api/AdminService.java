package com.crowd.service.api;

import com.crowd.entity.Admin;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface AdminService {
    void saveAdmin(Admin admin);
    List<Admin> getAll();
    Admin getAdminByLoginAcct(String loginAcct, String userPswd);


//    PageHelper implements Interceptor
//    PageInfo<T>类     List<T> list     pageNum  pageSize   size  total  pages  nextPage  lastPage   hasPreviousPage  hasNextPage

    PageInfo<Admin> getPageInfo(String keyword,Integer pageNum,Integer pageSize);
    Integer remove(Integer index);
    Admin getAdimById(Integer id);
    void update(Admin admin);
    void saveAdminRoleRelationship(Integer adminId,List<Integer> roleIdList);

    Admin getAdminByLoginAcct(String username);
}
