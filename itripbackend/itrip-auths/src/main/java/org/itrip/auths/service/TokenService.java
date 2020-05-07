package org.itrip.auths.service;

import org.itrip.pojo.ItripUser;

public interface TokenService {
    String getToken(String userAgent, ItripUser itripUser) throws Exception;
}
