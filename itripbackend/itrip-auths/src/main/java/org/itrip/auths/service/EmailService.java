package org.itrip.auths.service;

public interface EmailService {
    public void send(String userCode,String activeCode) throws Exception;
}
