package com.crowd.handler;

import com.crowd.api.MySQLRemoteService;
import com.crowd.api.RedisRemoteService;
import com.crowd.config.ShortMessageProperties;
import com.crowd.constant.CrowdConstant;
import com.crowd.entity.po.MemberPO;
import com.crowd.entity.vo.MemberLoginVO;
import com.crowd.entity.vo.MemberVO;
import com.crowd.util.CrowdUtil;
import com.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
public class MemberHandler {


    @Autowired
    private ShortMessageProperties messageProperties;

    @Autowired
    private RedisRemoteService redisRemoteService;


    @Autowired
    private MySQLRemoteService mySQLRemoteService;


    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }

    @RequestMapping("/auth/member/do/login")
    public String login(@RequestParam("loginacct") String loginacct, @RequestParam("userpswd") String userpswd,
                        ModelMap modelMap, HttpSession session) {

        // 1.调用远程接口根据登录账号查询MemberPO对象
        ResultEntity<MemberPO> resultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);

        if (ResultEntity.FAILED.equals(resultEntity.getResult())) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());

            return "member-login";

        }

        MemberPO memberPO = resultEntity.getData();

        if (memberPO == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);

            return "member-login";
        }

        // 2.比较密码
        String userpswdDataBase = memberPO.getUserpswd();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        boolean matcheResult = passwordEncoder.matches(userpswd, userpswdDataBase);

        if (!matcheResult) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);

            return "member-login";
        }

        // 3.创建MemberLoginVO对象存入Session域
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);


//        需要添加“http://localhost”， 如果不添加，则重定向默认为当前项目，即http://localhost:4000
//        注意： http://localhost（:80） 与 http://localhost:4000 是不同的项目
//        return "redirect:/auth/member/to/center/page";
        return "redirect:http://localhost/auth/member/to/center/page";
//        return "redirect:http://112.74.89.58:443/auth/member/to/center/page";
    }



    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap){

        // 1.获取用户输入的手机号
        String phoneNum = memberVO.getPhoneNum();

         // 2.拼Redis中存储验证码的Key
         String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
         // 3.从Redis读取Key对应的value
         ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKeyRemote(key);
        String result = resultEntity.getResult();
        //4.检查查询操作是否有效
         if (ResultEntity.FAILED.equals(result)) {

             modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
             return "member-reg";
         }

         String redisCode = resultEntity.getData();
         if (redisCode == null) {

             modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,
             CrowdConstant.MESSAGE_CODE_NOT_EXISTS);

             return "member-reg";
         }

        // 这里注册，把验证码写死为1234了，不用启redis服务也可以注册
//        String redisCode = "1234";
        // 5.如果从Redis能够查询到value则比较表单验证码和Redis验证码
        String formCode = memberVO.getCode();


        if (!Objects.equals(formCode, redisCode)) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_INVALID);

            return "member-reg";
        }

        // 6.如果验证码一致，则从Redis删除
         redisRemoteService.removeRedisKeyRemote(key);

        // 7.执行密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String userpswdBeforeEncode = memberVO.getUserpswd();

        String userpswdAfterEncode = passwordEncoder.encode(userpswdBeforeEncode);

        memberVO.setUserpswd(userpswdAfterEncode);

        // 8.执行保存
        // ①创建空的MemberPO对象
        MemberPO memberPO = new MemberPO();

        // ②复制属性
        BeanUtils.copyProperties(memberVO, memberPO);

        // ③调用远程方法
        ResultEntity<String> saveMemberResultEntity = mySQLRemoteService.saveMember(memberPO);

        if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMemberResultEntity.getMessage());

            return "member-reg";
        }

        // 使用重定向避免刷新浏览器导致重新执行注册流程
        return "redirect:/auth/member/to/login/page";

    }



    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum")String phoneNum){

        ResultEntity<String> resultEntity = CrowdUtil.sendCodeByShortMessage(
                messageProperties.getHost(),
                messageProperties.getPath(),
                messageProperties.getMethod(),
                phoneNum,
                messageProperties.getAppCode(),
                messageProperties.getSign(),
                messageProperties.getSkin());


        // 2.判断短信发送结果
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            // 3.如果发送成功，则将验证码存入Redis
            // ①从上一步操作的结果中获取随机生成的验证码
            String code = resultEntity.getData();

            // ②拼接一个用于在Redis中存储数据的key
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

            // ③调用远程接口存入Redis
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code,
                    15, TimeUnit.MINUTES);

            // ④判断结果
            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())) {

                return ResultEntity.successWithoutData();
            } else {
                return saveCodeResultEntity;
            }
        } else {
            return resultEntity;
        }
    }
}
