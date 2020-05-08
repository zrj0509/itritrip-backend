package org.itrip.auths.controller;

import org.itrip.auths.service.TokenService;
import org.itrip.common.DtoUtil;
import org.itrip.common.ErrorCode;
import org.itrip.dto.Dto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ValueConstants;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class TokenController {
    @Resource
    private TokenService tokenService;
    @RequestMapping(value = "retoken" ,method = RequestMethod.POST,produces = "application/json")
    public Dto doRetoken(HttpServletRequest request){
        try {
            String token=request.getHeader("token");
            String userAgent=request.getHeader("user-agent");
            return tokenService.retoken(token,userAgent);
        } catch (Exception ex){
            ex.printStackTrace();
            return DtoUtil.returnFail("服务器未响应", ErrorCode.AUTH_UNKNOWN);
        }
    }
}
