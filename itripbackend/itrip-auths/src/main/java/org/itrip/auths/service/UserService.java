package org.itrip.auths.service;

import org.itrip.dto.Dto;
import org.itrip.vo.userinfo.ItripUserVO;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    public boolean validateEmail(String mail)throws Exception;
    public Dto itriptxAddItripUser(ItripUserVO vo)throws Exception;
    public Dto active(String userCode,String code) throws Exception;
    boolean validatePhone(String phone) throws Exception;
    public Dto activePhone(String userCode,String code) throws Exception;
    Dto doLogin(String name, String psw, String userAgent) throws Exception;
}
