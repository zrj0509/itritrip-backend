package org.itrip.auths.service;

import org.itrip.common.RedisUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService{
    @Resource
    private MailSender mailSender;
    @Resource
    private SimpleMailMessage simpleMailMessage;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void send(String userCode, String activeCode) throws Exception {
        simpleMailMessage.setTo(userCode);
        simpleMailMessage.setText("注册邮箱"+userCode+"激活码"+activeCode);
        mailSender.send(simpleMailMessage);
        System.out.println("text"+simpleMailMessage.getText());
        redisUtils.setValueExpire("actCodeMail"+userCode,activeCode,30*60);
//        stringRedisTemplate.opsForValue().set("actCodeMail"+userCode,activeCode,30*60, TimeUnit.SECONDS);
    }
}
