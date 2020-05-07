package org.itrip.auths.service;

public interface SmsService {
    void send(String to,String templateId,String[] datas);
}
