package org.itrip.auths.controller;

import org.itrip.auths.service.UserService;
import org.itrip.common.DtoUtil;
import org.itrip.common.ErrorCode;
import org.itrip.common.RedisUtils;
import org.itrip.dto.Dto;
import org.itrip.vo.userinfo.ItripUserVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class UserController {
    @Resource
    private UserService userService;
    @RequestMapping(value = "/doregister" ,method = RequestMethod.POST,produces = "application/json")
    public Dto doRegisterByMail(@RequestBody ItripUserVO vo){
        //1.邮箱格式验证
        try {
            if(!userService.validateEmail(vo.getUserCode())){
                return DtoUtil.returnFail("邮箱格式不正确,请重新输入", ErrorCode.AUTH_ILLEGAL_USERCODE);
            }

            //2.添加用户
            return userService.itriptxAddItripUser(vo);

        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("服务器未响应",ErrorCode.AUTH_UNKNOWN);
        }
    }
    @RequestMapping(value = "/doactive" ,method = RequestMethod.PUT,produces = "application/json")
    public Dto doactive(@RequestParam("userCode") String userCode, @RequestParam("code") String code){
        try {
            return userService.active(userCode,code);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("服务器未响应",ErrorCode.AUTH_UNKNOWN);
        }
    }
    @RequestMapping(value = "/doactivePhone" ,method = RequestMethod.PUT,produces = "application/json")
    public Dto doactivePhone(@RequestParam("userCode") String userCode, @RequestParam("code") String code){
        try {
            return userService.activePhone(userCode,code);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("服务器未响应",ErrorCode.AUTH_UNKNOWN);
        }
    }
    @RequestMapping(value = "/doregisterbyphone" ,method = RequestMethod.POST,produces = "application/json")
    public Dto doRegisterByPhone(@RequestBody ItripUserVO vo){
        //1.手机格式验证
        try {
            if(!userService.validatePhone(vo.getUserCode())){
                return DtoUtil.returnFail("手机格式不正确,请重新输入", ErrorCode.AUTH_ILLEGAL_USERCODE);
            }

            //2.添加用户
            return userService.itriptxAddItripUser(vo);

        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("服务器未响应",ErrorCode.AUTH_UNKNOWN);
        }
    }
}
