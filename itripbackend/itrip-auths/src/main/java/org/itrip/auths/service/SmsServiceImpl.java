package org.itrip.auths.service;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.itrip.common.RedisUtils;
import org.itrip.common.SystemConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Set;

@Service
public class SmsServiceImpl implements SmsService{
    //sms系统配置注入
    @Resource
    private SystemConfig systemConfig;

    //redis注入
    @Resource
    private RedisUtils redisUtils;

    @Override
    public void send(String to, String templateId, String[] datas) {
        //sms对象
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        // 初始化服务器地址和端口，生产环境配置成app.cloopen.com，端口是8883.
        restAPI.init(systemConfig.getSmsServerIP(), systemConfig.getSmsServerPort());
        // 初始化主账号名称和主账号令牌，登陆云通讯网站后，可在控制首页中看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN。
        restAPI.setAccount(systemConfig.getSmsAccountSid(), systemConfig.getSmsAuthToken());
        // 请使用管理控制台中已创建应用的APPID。
        restAPI.setAppId(systemConfig.getSmsAppID());
        //结果
        HashMap<String, Object> result = restAPI.sendTemplateSMS(to,templateId,datas);
        ///
        System.out.println("SDKTestGetSubAccounts result=" + result);
        if("000000".equals(result.get("statusCode"))){
            //正常返回输出data包体信息（map）
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }
            //将数据保存到redis中 10分
            redisUtils.setValueExpire("activePhone"+to,datas[0],60*10);

        }else{
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
    }
}
