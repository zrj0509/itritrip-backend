package org.itrip.auths.service;

import com.alibaba.fastjson.JSON;
import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import org.itrip.common.DtoUtil;
import org.itrip.common.ErrorCode;
import org.itrip.common.MD5;
import org.itrip.common.RedisUtils;
import org.itrip.dto.Dto;
import org.itrip.mapper.itripUser.ItripUserMapper;
import org.itrip.pojo.ItripUser;
import org.itrip.vo.ItripTokenVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService{
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private ItripUserMapper itripUserMapper;
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

    @Override
    public Dto retoken(String token, String userAgent) {
        //1.redis中查找这个token是否存在
        if(!redisUtils.exit(token)){
            return DtoUtil.returnFail("token不存在!", ErrorCode.AUTH_TOKEN_INVALID);
        }
        //2.判断非法截取token--》6位随机数
        String tokens[]=token.split("-");
        if(!tokens[4].equals(MD5.getMd5(userAgent,6))){
            return DtoUtil.returnFail("token无效!",ErrorCode.AUTH_TOKEN_INVALID);
        }
        try {
            Date time=new SimpleDateFormat("yyyyMMddHHmmss").parse(tokens[3]);
            Long longTime=(new Date().getTime()-time.getTime())/(1000*60*60);
            if (longTime<=0.5){
                //4.生成token字符串
                ItripUser user= itripUserMapper.getItripUserById(Long.valueOf(tokens[2]));
                System.out.println("当前用户"+user);
                String ntoken=this.getToken(userAgent,user);
                System.out.println("token=>"+ntoken);

                //5.封装token对象,返回给客户端
                ItripTokenVO tokenVO =new ItripTokenVO();

                //PC-3066014fa0b10792e4a762-23-20170531133947-4f6496
                String[] ntokens=token.split("-");
                //创建日期
                String times = ntokens[3];
                //设置到tokenVO
                tokenVO.setGenTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(times).getTime());
                //判断PC/移动
                if(ntoken.startsWith("token:PC-")){
                    //过期时间 毫秒==>3小时
                    tokenVO.setExpTime(3*60*60*1000);
                    //保存到redis
                    redisUtils.setValueExpire(ntoken, JSON.toJSONString(user),3*60*60);

                }else{
                    //手机-一直存在
                    tokenVO.setExpTime(Long.MAX_VALUE);
                    //保存到redis
                    redisUtils.setValue(ntoken,JSON.toJSONString(user));
                }
                //token信息
                redisUtils.del(token);
                tokenVO.setToken(ntoken);
                //返回tokenVO数据
                return DtoUtil.returnDataSuccess(tokenVO);
            }else {
                return DtoUtil.returnFail("token重置失败",ErrorCode.AUTH_TOKEN_INVALID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("服务器连接失败",ErrorCode.AUTH_UNKNOWN);
        }
    }
}
