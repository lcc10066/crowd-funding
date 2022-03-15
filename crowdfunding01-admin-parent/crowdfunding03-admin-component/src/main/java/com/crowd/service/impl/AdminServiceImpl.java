package com.crowd.service.impl;

import com.crowd.constant.CrowdConstant;
import com.crowd.entity.Admin;
import com.crowd.entity.AdminExample;
import com.crowd.exception.LoginAcctAlreadyInUseException;
import com.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.crowd.exception.LoginFailedException;
import com.crowd.mapper.AdminMapper;
import com.crowd.service.api.AdminService;
import com.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

//    springSecurity 盐值加密
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void saveAdmin(Admin admin) {
        String pswd = admin.getUserPswd();
//        pswd = CrowdUtil.md5(pswd);
//        修改为盐值加密
        pswd = passwordEncoder.encode(pswd);
        admin.setUserPswd(pswd);

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);
        admin.setCreateTime(createTime);
//        为了防止用户名重复添加，已在设计数据库时为acct字段添加了唯一约束，所以在执行insert时可能会抛出异常（acct重复）
//        org.springframework.dao.DuplicateKeyException
        try{

            adminMapper.insert(admin);
        }catch (Exception e){
            e.printStackTrace();
            if(e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {

        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> admins = adminMapper.selectByExample(adminExample);
        if (admins == null || admins.size() == 0)
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        if (admins.size() > 1)
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        Admin admin = admins.get(0);

        String userPswdDd = admin.getUserPswd();
        String userPswdFrom = CrowdUtil.md5(userPswd);

        if (!Objects.equals(userPswdDd, userPswdFrom))
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        return admin;
    }
    //    PageHelper implements Interceptor

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
//keyword 为查询关键词（模糊查询，匹配所有字段）
//        如果要实现分页，就加这一句，不需要实现就不加
        PageHelper.startPage(pageNum,pageSize);

//        下述方法返回的对象为ArrayList的子类 Page<E> extends ArrayList<E>
        List<Admin> list = adminMapper.selectAdminListByKeyword(keyword);

// PageInfo<T>类     List<T> list     pageNum  pageSize   size  total  pages  nextPage  lastPage  hasPreviousPage  hasNextPage
        return new PageInfo<Admin>(list);
    }

    @Override
    public Integer remove(Integer index) {
        return adminMapper.deleteByPrimaryKey(index);
    }

    @Override
    public Admin getAdimById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Admin admin) {
//          null值的字段不更新
        try{
            adminMapper.updateByPrimaryKeySelective(admin);
        }catch (Exception e){
            e.printStackTrace();
            if(e instanceof DuplicateKeyException)
                throw new LoginAcctAlreadyInUseForUpdateException();
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        adminMapper.deleteOldRelationship(adminId);
        if(roleIdList != null && roleIdList.size()>0 )
            adminMapper.insertNewRelationship(adminId,roleIdList);
    }

    @Override
    public Admin getAdminByLoginAcct(String username) {
        AdminExample example = new AdminExample();
        AdminExample.Criteria criteria = example.createCriteria();
        criteria.andLoginAcctEqualTo(username);
        List<Admin> adminList = adminMapper.selectByExample(example);
        Admin admin = adminList.get(0);
        return admin;
    }
}
