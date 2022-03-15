package com.crowd.mvc.config;


import com.crowd.entity.Admin;
import com.crowd.exception.AccessForbiddenException;
import com.crowd.exception.LoginAcctAlreadyInUseException;
import com.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.crowd.exception.LoginFailedException;
import com.crowd.util.CrowdUtil;
import com.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@ControllerAdvice将当前类标识为异常处理的组件
@ControllerAdvice
public class CrowdExceptionResolver {

//异常处理器的request形参与对于抛出异常的handler中的request不是一个？？？？？？？？
    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseForUpdateException(
            LoginAcctAlreadyInUseForUpdateException exception,
            HttpServletRequest request,
            HttpServletResponse response
    ){
//        String loginAcct = (String) request.getAttribute("loginAcct");   null
//        String userName = (String)request.getAttribute("userName");     null
//        String email = (String)request.getAttribute("email");         null
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName(viewName);
//        Admin admin = new Admin(null, loginAcct, null, userName, email, null);
// model is {admin=Admin{id=null, loginAcct='null', userPswd='null', userName='null', email='null', createTime='null'}
//        modelAndView.addObject("admin",admin);
//        return modelAndView;
        String viewName = "system-error";
        return commonResolve(request,response,exception,viewName);
    }

//    存在问题： 此处使用注解配置为什么无效？ 只能用XML配置
    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(
            AccessForbiddenException exception,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        String viewName = "admin-add";
        return commonResolve(request,response,exception,viewName);
    }

    @ExceptionHandler(value = AccessForbiddenException.class)
    public ModelAndView resolveAccessForbiddenException(
        AccessForbiddenException exception,
        HttpServletRequest request,
        HttpServletResponse response
    ){
        String viewName = "admin-login";
        return commonResolve(request,response,exception,viewName);
    }

    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(
            LoginFailedException exception,  // exception表示当前请求处理中出现的异常对象
            HttpServletRequest request,
            HttpServletResponse response){

        String viewName = "admin-login";
        return commonResolve(request,response,exception,viewName);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(
            NullPointerException exception,  // exception表示当前请求处理中出现的异常对象
            HttpServletRequest request,
            HttpServletResponse response){

        String viewName = "system-error";
        return commonResolve(request,response,exception,viewName);
    }

    private ModelAndView commonResolve(HttpServletRequest request,HttpServletResponse response,Exception exception,String viewName){

        boolean isJson = CrowdUtil.judgeRequestType(request);

        if(isJson){
            ResultEntity<Object> failed = ResultEntity.failed(exception.getMessage());
            Gson gson = new Gson();
            String json = gson.toJson(failed);
            try {
                response.getWriter().write(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("exception",exception);
            modelAndView.setViewName(viewName);
            return modelAndView;
        }
    }
}
