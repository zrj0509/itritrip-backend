package org.itrip.auths.service;

import org.itrip.dto.Dto;
import org.itrip.vo.userinfo.ItripUserVO;

public interface UserService {
    public boolean validateEmail(String mail)throws Exception;
    public Dto itriptxAddItripUser(ItripUserVO vo)throws Exception;
}
