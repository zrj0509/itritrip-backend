package org.itrip.biz.controller;

import org.itrip.biz.service.itripUserLinkUser.ItripUserLinkUserService;
import org.itrip.common.DtoUtil;
import org.itrip.common.ErrorCode;
import org.itrip.dto.Dto;
import org.itrip.pojo.ItripUserLinkUser;
import org.itrip.vo.userinfo.ItripAddUserLinkUserVO;
import org.itrip.vo.userinfo.ItripSearchUserLinkUserVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/userinfo")
public class UserInfoController {
    @Resource
    private ItripUserLinkUserService itripUserLinkUserService;
//    @RequestMapping(value = "adduserlinkuser",method = RequestMethod.POST,produces = "application/json")
//    public Dto adduserlinkuser(@RequestBody ItripAddUserLinkUserVO itripAddUserLinkUserVO){
//        try {
//            ItripUserLinkUser itripUserLinkUser=new ItripUserLinkUser();
//            return itripUserLinkUserService.itriptxAddItripUserLinkUser(itripUserLinkUser);
//        }catch (Exception ex){
//            ex.printStackTrace();
//            return DtoUtil.returnFail("服务器未响应", ErrorCode.AUTH_UNKNOWN)
//        }
//        return null;
//    }
    @RequestMapping(value = "queryuserlinkuser",method = RequestMethod.POST,produces = "application/json")
    public Dto queruserlinkuser(@RequestBody ItripSearchUserLinkUserVO vo){
        try {
//            return itripUserLinkUserService.queryItripUserLinkUserPageByMap();
        }catch (Exception ex){
            ex.printStackTrace();
            return  DtoUtil.returnFail("服务器未响应",ErrorCode.AUTH_UNKNOWN);
        }
        return null;
    }
}
