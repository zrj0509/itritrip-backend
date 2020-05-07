package org.itrip.auths.controller;

import org.itrip.auths.service.UserService;
import org.itrip.common.DtoUtil;
import org.itrip.common.ErrorCode;
import org.itrip.dto.Dto;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
public class LoginController {
    @Resource
    private UserService userService;
    @RequestMapping(value = "/doLogin" ,method = RequestMethod.POST,produces = "application/json")
    public Dto doLogin(@RequestParam("name") String name, @RequestParam("psw") String psw, HttpServletRequest request){
        try {
            if (StringUtils.isEmpty(name)&& StringUtils.isEmpty(psw)){
                return DtoUtil.returnFail("用户名和密码不能为空!", ErrorCode.AUTH_ILLEGAL_USERCODE);
            }
            //2.登录用户(业务)
            return userService.doLogin(name,psw,request.getHeader("user-agent"));
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("服务器未响应", ErrorCode.AUTH_UNKNOWN);
        }
    }
}
