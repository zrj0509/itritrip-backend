package org.itrip.common;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.itrip.pojo.ItripUser;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Token验证
 * Created by hanlu on 2017/5/7.
 */
public class ValidationToken {

    private Logger logger = Logger.getLogger(ValidationToken.class);

    //private RedisAPI redisAPI;
    @Resource
    private RedisUtils redisUtil;

    public RedisUtils getRedisUtil() {
        return redisUtil;
    }

    public void setRedisUtil(RedisUtils redisUtil) {
        this.redisUtil = redisUtil;
    }

//    public RedisAPI getRedisAPI() {
//        return redisAPI;
//    }
//    public void setRedisAPI(RedisAPI redisAPI) {
//        this.redisAPI = redisAPI;
//    }
    public ItripUser getCurrentUser(String tokenString){
        //根据token从redis中获取用户信息
			/*
			 test token:
			 key : token:1qaz2wsx
			 value : {"id":"100078","userCode":"myusercode","userPassword":"78ujsdlkfjoiiewe98r3ejrf","userType":"1","flatID":"10008989"}

			*/
        ItripUser itripUser = null;
        if(null == tokenString || "".equals(tokenString)){
            return null;
        }
        try{
            //String userInfoJson = redisAPI.get(tokenString);
            String userInfoJson = redisUtil.getValue(tokenString);
            itripUser = JSONObject.parseObject(userInfoJson,ItripUser.class);
        }catch(Exception e){
            itripUser = null;
            logger.error("get userinfo from redis but is error : " + e.getMessage());
        }
        return itripUser;
    }

    /**
     *  验证token
     * @param userAgent 请求头中header中的User-Agent
     * @param token 前端传过来的token
     * @return
     * @throws Exception
     */
    public boolean valiate(String userAgent, String token) throws Exception{
        //1、判断redis中的token是否还存在
        if(!redisUtil.exit(token)){
            return false;//token已不存在了
        }
        //2、token还存在，需要对前端传进来的token进行验证
        String[] tokenDetails = token.split("-");
        Date tokenGenTime = new SimpleDateFormat("yyyyMMddHHmmss").parse(tokenDetails[3]);//token生成的时间
        long pass = System.currentTimeMillis()-tokenGenTime.getTime();
        //token已失效
        if(pass > 2*60*60*1000){
            return false;
        }
        //验证token的最后6位数
        String tokenUserAgent = tokenDetails[4];
        if(MD5.getMd5(userAgent,6).equals(tokenUserAgent)){
            return true;
        }
        return false;
    }
}
