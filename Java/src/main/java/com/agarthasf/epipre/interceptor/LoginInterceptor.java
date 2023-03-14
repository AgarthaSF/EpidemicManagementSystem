package com.agarthasf.epipre.interceptor;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器：Spring框架特有的，常用于登录校验，权限校验，请求日志打印
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 打印请求信息
        LOG.info("------------- LoginInterceptor 开始 -------------");
        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);


        // OPTIONS请求不做校验,
        // 前后端分离的架构, 前端会发一个OPTIONS请求先做预检, 对预检请求不做校验
        if(request.getMethod().toUpperCase().equals("OPTIONS")){
            return true;
        }

//        // 获取请求头中的用户权限
//        String userType = request.getHeader("type");
//
//        // 解析请求地址
//        String path = request.getRequestURL().toString();
//        String[] split = path.split("/");
//        String requestType = split[3];
//
//        // 根据请求类型与用户权限判断用户是否能够进行访问
//        switch (requestType){
//            case "announcement":
//                if(split[4].equals("delete") || split[4].equals("save")){
//                    return handleResponse(response, userType, "admin");
//                }else{
//                    return true;
//                }
//            case "daily-check":
//                if(split[4].equals("all") || split[4].equals("anomaly")){
//                    return handleResponse(response, userType, "admin");
//                }else if(split[4].equals("list") || split[4].equals("save")){
//                    return handleResponse(response, userType, "student", "teacher");
//                }
//                break;
//
//            case "access-application":
//                if(split[4].equals("processed") || split[4].equals("todo")){
//                    return handleResponse(response, userType, "admin");
//                }else if(split[4].equals("list") || split[4].equals("save")){
//                    return handleResponse(response, userType, "admin", "foreigner");
//                }
//                break;
//
//
//            case "travel-info":
//                if(split[4].equals("anomaly")){
//                    return handleResponse(response, userType, "admin");
//                }else if(split[4].equals("save")){
//                    return handleResponse(response, userType, "teacher",
//                            "student", "foreigner");
//                }
//                break;
//
//            case "out-application":
//                if(split[4].equals("history") || split[4].equals("todo") || split[4].equals("save")
//                 || split[4].equals("delete") || split[4].equals("save")){
//                    return handleResponse(response, userType, "counselor");
//                }else if(split[4].equals("list") || split[4].equals("insert")){
//                    return handleResponse(response, userType, "student");
//                }
//                break;
//
//            case "account":
//            case "counselor":
//            case "diagnosed-case":
//            case "foreign-staff":
//            case "diagnosed-survey":
//                return handleResponse(response, userType, "admin");
//
//            case "school-staff":
//                if(split[4].equals("student") || split[4].equals("teacher")){
//                    return handleResponse(response, userType, "admin");
//                }else if(split[4].equals("list")){
//                    return handleResponse(response, userType, "student");
//                }
//                break;
//
//
//            case "info-upload":
//                if(split[4].equals("all") || split[4].equals("anomaly")){
//                    return handleResponse(response, userType, "admin");
//                }else if(split[4].equals("list") || split[4].equals("save")){
//                    return handleResponse(response, userType, "student",
//                            "teacher");
//                }
//            default:
//                return true;
//
//        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long) request.getAttribute("requestStartTime");
        LOG.info("------------- LoginInterceptor 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LOG.info("LogInterceptor 结束");
    }


    private Boolean handleResponse(HttpServletResponse response, String userType,
                                   String expectedType){
        if (userType == null || (!userType.equals(expectedType))) {
            LOG.info( "权限组错误，请求被拦截" );
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }else{
            return true;
        }
    }

    private Boolean handleResponse(HttpServletResponse response, String userType,
                                   String expectedTypeFirst, String expectedTypeSecond){
        if (userType == null || (!userType.equals(expectedTypeFirst) && !userType.equals(expectedTypeSecond))) {
            LOG.info( "权限组错误，请求被拦截" );
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }else{
            return true;
        }
    }

    private Boolean handleResponse(HttpServletResponse response, String userType,
                                   String expectedTypeFirst, String expectedTypeSecond, String expectedTypeThird){
        if (userType == null || (!userType.equals(expectedTypeFirst) && !userType.equals(expectedTypeSecond)
                && !userType.equals(expectedTypeThird))) {
            LOG.info( "权限组错误，请求被拦截" );
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }else{
            return true;
        }
    }

}
