package org.itrip.auths.service;

import org.itrip.dto.Dto;
import org.itrip.pojo.ItripUser;

public interface TokenService {
    String getToken(String userAgent, ItripUser itripUser) throws Exception;

    Dto retoken(String token, String userAgent);
}
