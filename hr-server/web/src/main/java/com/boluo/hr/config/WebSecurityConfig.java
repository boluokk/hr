package com.boluo.hr.config;

import com.boluo.hr.pojo.Hr;
import com.boluo.hr.pojo.RespBean;
import com.boluo.hr.service.HrService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author @1352955539(boluo)
 * @date 2021/1/26 - 12:09
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    HrService hrService;

    @Autowired
    CustomUrlDecisionManager customUrlDecisionManager;

    @Autowired
    CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService).passwordEncoder(new BCryptPasswordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(customUrlDecisionManager);
                        o.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        return o;
                    }
                })
//                .anyRequest().authenticated()
                .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
                        res.setContentType("application/json;charset=utf-8");
                        Hr hr = (Hr) authentication.getPrincipal();
                        hr.setPassword(null);
                        RespBean rb = RespBean.ok("????????????",hr);
                        PrintWriter writer = res.getWriter();
                        writer.write(new ObjectMapper().writeValueAsString(rb));
                        writer.flush();
                        writer.close();
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
                        res.setContentType("application/json;charset=utf-8");
                        RespBean respBean = RespBean.error("????????????!");
                        if(e instanceof LockedException) {
                            respBean.setMsg("???????????????????????????????????????");
                        }else if(e instanceof DisabledException) {
                            respBean.setMsg("???????????????????????????????????????");
                        }else if(e instanceof CredentialsExpiredException) {
                            respBean.setMsg("????????????????????????????????????");
                        }else if(e instanceof AccountExpiredException) {
                            respBean.setMsg("????????????????????????????????????");
                        }else if(e instanceof BadCredentialsException){
                            respBean.setMsg("?????????????????????????????????????????????");
                        } else if(e instanceof InternalAuthenticationServiceException) {
                            respBean.setMsg("?????????????????????????????????????????????");
                        }
                        PrintWriter writer = res.getWriter();
                        writer.write(new ObjectMapper().writeValueAsString(respBean));
                        writer.flush();
                        writer.close();
                    }
                }).permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
                        res.setContentType("application/json;charset=utf-8");
                        PrintWriter writer = res.getWriter();
                        RespBean respBean = RespBean.ok("????????????");
                        writer.write(new ObjectMapper().writeValueAsString(respBean));
                        writer.flush();
                        writer.close();
                    }
                })
                .and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
                res.setContentType("application/json;charset=utf-8");
                res.setStatus(HttpStatus.UNAUTHORIZED.value());
                RespBean respBean = RespBean.error("????????????!");
                if(e instanceof InsufficientAuthenticationException) {
                    respBean.setMsg("????????????????????????????????????");
                }
                PrintWriter writer = res.getWriter();
                writer.write(new ObjectMapper().writeValueAsString(respBean));
                writer.flush();
                writer.close();
            }
        });
    }
}
