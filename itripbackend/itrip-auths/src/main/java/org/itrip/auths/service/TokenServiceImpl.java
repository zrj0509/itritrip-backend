package org.itrip.auths.service;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import org.itrip.common.MD5;
import org.itrip.pojo.ItripUser;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService{
    @Override
    public String getToken(String userAgent, ItripUser itripUser) throws Exception {
        //拼接token
        StringBuilder builder =new StringBuilder("token:");
        //UASparser对象
        UASparser uaSparser =new UASparser(OnlineUpdater.getVendoredInputStream());;
        //生成信息对象
        UserAgentInfo userAgentInfo =uaSparser.parse(userAgent);
        //获取设备类型
        String  type =userAgentInfo.getDeviceType();
        System.out.println(type);
        //判断是哪一个类型
        if (type.equals("Personal computer")){
            builder.append("PC-");
        }else if(type.equals("unknown")){
            builder.append("PC-");
        }else{
            builder.append("MOBILE-");
        }

        //用户邮箱/手机
        builder.append(MD5.getMd5(itripUser.getUserCode(),32)+"-");

        //用户ID
        builder.append(itripUser.getId()+"-");

        //创建时间
        builder.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"-");

        //6位随机数
        builder.append(MD5.getMd5(userAgent,6));
        return builder.toString();
    }
}
