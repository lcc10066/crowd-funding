package com.crowd.mvc.config;


import com.crowd.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//关于两个IOC容器（spring的IOC容器、springmvc的IOC容器），谁来扫描这个配置类，这个配置类就存在于哪个IOC容器中
//springmvc的IOC容器： xxxHandler, view-controller
//spring的IOC容器：  xxxService, xxxMapper
//filter要找的bean
//标明其为一个配置类
@Configuration

//启用web环境下的权限控制功能
@EnableWebSecurity

//启用全局方法权限控制功能，并设置prePostEnabled = true，保证@PreAuthority,@PostAuthority,@PreFilter,@PostFilter生效
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

//    自动装配我们实现的CrowdUserDetailsService
    @Autowired
    private UserDetailsService userDetailsService;

//    盐值加密  （将其装入IOC容器，便于使用）
//    @Bean
//    public BCryptPasswordEncoder getPasswordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

    /**
     * 将上述方法注释的原因，其无法正常使用。因为WebAppSecurityConfig被springmvc扫描，BCryptPasswordEncoder对应的bean存在于springmvc的IOC容器中
     * 我们在service.impl的类中想要自动装配该bean时，无法正常装配
     * 原因：spring的IOC容器为父容器，springMVC的IOC容器为子容器，子容器有指向父容器的parent属性，可以共享其资源，
     * 但是父容器不知道子容器的存在，无法使用其中的资源
     * 我们将其装配至父容器对应的配置文件spring-persist-tx.xml中
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     *
     *
     * SpringSecurity 处理完登录操作之后把登录成功的User对象以 principal 属性名存入了
     * UsernamePasswordAuthenticationToken 对象,以用于访问
     * 也就是我们自己封装的 SecurityAdmin 对象
     *
     */
    //    public class UsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {
    //        private final Object principal; 存储登录成功的对象
    //        private Object credentials; 存储明文密码
    //        ......
    //      }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
//        内存版本
//        builder.inMemoryAuthentication().withUser("jerry").password("123123").roles("ADMIN");

//        数据库版本
            builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {

                security
                .authorizeRequests()
                .antMatchers("/admin/to/login/page.html")
                .permitAll()
                .antMatchers("/bootstrap/**") // 针对静态资源进行设置，无条件访问
                .permitAll() // 针对静态资源进行设置，无条件访问
                .antMatchers("/crowd/**") // 针对静态资源进行设置，无条件访问
                .permitAll() // 针对静态资源进行设置，无条件访问
                .antMatchers("/css/**") // 针对静态资源进行设置，无条件访问
                .permitAll() // 针对静态资源进行设置，无条件访问
                .antMatchers("/fonts/**") // 针对静态资源进行设置，无条件访问
                .permitAll() // 针对静态资源进行设置，无条件访问
                .antMatchers("/img/**") // 针对静态资源进行设置，无条件访问
                .permitAll() // 针对静态资源进行设置，无条件访问
                .antMatchers("/jquery/**") // 针对静态资源进行设置，无条件访问
                .permitAll() // 针对静态资源进行设置，无条件访问
                .antMatchers("/layer/**") // 针对静态资源进行设置，无条件访问
                .permitAll() // 针对静态资源进行设置，无条件访问
                .antMatchers("/script/**") // 针对静态资源进行设置，无条件访问
                .permitAll() // 针对静态资源进行设置，无条件访问
                .antMatchers("/ztree/**") // 针对静态资源进行设置，无条件访问
                .permitAll()
                .antMatchers("/admin/get/page.html") //小范围在前，大范围在后
//                .hasRole("经理")
                .access("hasAuthority('user:get') OR hasRole('经理')")
//                .access("hasAuthority('user:get') AND hasRole('经理')")
                .anyRequest()  //其他任意的请求，认证后访问
                .authenticated()
                .and()
                .exceptionHandling()
//                        当我们设置访问权限，直接在配置类中设置访问某个资源所需要的角色或者权限时，在拒绝访问后，无法使用springMVC所设置的异常映射
//                        因为权限控制在DelegatingFilterProxy中，（权限不通过时，直接抛出403异常，都没进入MVC模块）访问时权限通过后才会进入SpringMVC的模块，出现异常时才会得到映射。
//                            只能在下方定义 .accessDeniedHandler(new AccessDeniedHandler()...
//                        当我们使用注解在handler方法上标注权限时，可以正常使用springMVC的异常映射
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                       AccessDeniedException e) throws IOException, ServletException {
                        httpServletRequest.setAttribute("exception",new Exception(CrowdConstant.MESSAGE_ACCESS_DENIED));
                        httpServletRequest.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(httpServletRequest,httpServletResponse);
                    }
                })
                .and()
                .csrf()//取消csrf，否则所有的请求，提交时都需要带上CSRF  （防跨站请求伪造功能）
                .disable()
                .formLogin() //开启表单登录功能
                .loginPage("/admin/to/login/page.html")//指定登录页面
                .loginProcessingUrl("/security/do/login.html")//指定处理登录请求的地址 (对应的handler由springSecurity实现)
                .defaultSuccessUrl("/admin/to/main/page.html")    //指定登录成功后前往的地址
                .usernameParameter("loginAcct")//表单提交时用户名的标签名称
                .passwordParameter("userPswd")//表单提交时用密码的标签名称
                .and()
                .logout()
                .logoutUrl("/security/do/logout.html")
                .logoutSuccessUrl("/admin/to/login/page.html") //重定向至该url
                ;
    }
}
