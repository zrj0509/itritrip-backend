package org.itrip.auths.service;

import com.alibaba.fastjson.JSON;
import org.itrip.common.*;
import org.itrip.dto.Dto;
import org.itrip.pojo.ItripUser;
import org.itrip.vo.ItripTokenVO;
import org.itrip.vo.userinfo.ItripUserVO;
import org.itrip.mapper.itripUser.ItripUserMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService{
    @Resource
    private ItripUserMapper itripUserMapper;
    @Resource
    private EmailService emailService;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private SmsService smsService;
    @Resource
    private TokenService tokenService;
//    @Resource
//    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean validateEmail(String mail) throws Exception {
        String regex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        return Pattern.compile(regex).matcher(mail).find();
    }

    @Override
    public Dto itriptxAddItripUser(ItripUserVO vo) throws Exception {
        ItripUser itripUser=itripUserMapper.getItripUserByUserCode(vo.getUserCode());
        if (itripUser==null){
            ItripUser user=new ItripUser();
            user.setUserCode(vo.getUserCode());
            user.setUserPassword(MD5.getMd5(vo.getUserPassword(),32));
            user.setUserName(vo.getUserName());
            user.setUserType(0);
            user.setActivated(0);
            if (validateEmail(vo.getUserCode())) {
                String activeCode = MD5.getMd5(vo.getUserCode(), 16);
                emailService.send(vo.getUserCode(), activeCode);
            }else {
                //生成激活码 4位数
                String activePhone=String.valueOf(MD5.getRandomCode());
                //发送短信 (手机号，测试模板只能是1，激活码和时间10分)
                smsService.send(vo.getUserCode(),"1",new String[]{activePhone,String.valueOf(10)});
            }
            itripUserMapper.insertItripUser(user);
        }else {
            return DtoUtil.returnFail("此用户已存在", ErrorCode.AUTH_USER_ALREADY_EXISTS);
        }
        return DtoUtil.returnSuccess("添加成功！");
    }

    @Override
    public Dto active(String userCode, String code) throws Exception {
        String key="actCodeMail"+userCode;
        if (redisUtils.exit(key)){
            if (redisUtils.getValue(key).equals(code)){
                ItripUser itripUser=itripUserMapper.getItripUserByUserCode(userCode);
                if (EmptyUtils.isNotEmpty(itripUser)){
                    itripUser.setActivated(1);
                    itripUser.setFlatID(itripUser.getId());
                    itripUser.setModifyDate(new Date());
                    itripUserMapper.updateItripUser(itripUser);
                    return DtoUtil.returnSuccess("激活成功");
                }
            }
        }
        return DtoUtil.returnSuccess("激活失败");
    }

    @Override
    public boolean validatePhone(String phone) throws Exception{
        String regex = "^1[3578]\\d{9}$";
        return Pattern.compile(regex).matcher(phone).find();
    }

    @Override
    public Dto activePhone(String userCode, String code) throws Exception {
        String key="activePhone"+userCode;
        System.out.println(redisUtils.exit(key));
        if (redisUtils.exit(key)){
            System.out.println(redisUtils.getValue(key));
            if (redisUtils.getValue(key).equals(code)){
                ItripUser itripUser=itripUserMapper.getItripUserByUserCode(userCode);
                if (EmptyUtils.isNotEmpty(itripUser)){
                    itripUser.setActivated(1);
                    itripUser.setFlatID(itripUser.getId());
                    itripUser.setModifyDate(new Date());
                    itripUserMapper.updateItripUser(itripUser);
                    return DtoUtil.returnSuccess("激活成功");
                }
            }
        }
        return DtoUtil.returnSuccess("激活失败");
    }

    @Override
    public Dto doLogin(String name, String psw, String userAgent) throws Exception {
        //1.先查找这个用户
        ItripUser user = itripUserMapper.getItripUserByUserCode(name);

        //2.判断是否存在密码相等
        if (user!=null && user.getUserPassword().equals(MD5.getMd5(psw,32))){

            //3.判断用户是否激活
            if(1!=user.getActivated()){
                return DtoUtil.returnFail("用户未激活",ErrorCode.AUTH_ACTIVATE_FAILED);
            }

            //4.生成token字符串
            String token=tokenService.getToken(userAgent,user);
            System.out.println("token=>"+token);

            //5.封装token对象,返回给客户端
            ItripTokenVO tokenVO =new ItripTokenVO();

            //PC-3066014fa0b10792e4a762-23-20170531133947-4f6496
            String[] tokens=token.split("-");
            //创建日期
            String times = tokens[3];
            //设置到tokenVO
            tokenVO.setGenTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(times).getTime());
            //判断PC/移动
            if(token.startsWith("token:PC-")){
                //过期时间 毫秒==>3小时
                tokenVO.setExpTime(3*60*60*1000);
                //保存到redis
                redisUtils.setValueExpire(token, JSON.toJSONString(user),3*60*60);

            }else{
                //手机-一直存在
                tokenVO.setExpTime(Long.MAX_VALUE);
                //保存到redis
                redisUtils.setValue(token,JSON.toJSONString(user));
            }

            //token信息
            tokenVO.setToken(token);

            //返回tokenVO数据
            return DtoUtil.returnDataSuccess(tokenVO);

        }else{
            return DtoUtil.returnFail("用户名或密码不正确!",ErrorCode.AUTH_PARAMETER_ERROR);
        }
    }
}
