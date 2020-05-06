package org.itrip.auths.service;

import org.itrip.dto.Dto;
import org.itrip.pojo.ItripUser;
import org.itrip.vo.userinfo.ItripUserVO;
import org.itrip.mapper.itripUser.ItripUserMapper;
import org.itrip.common.DtoUtil;
import org.itrip.common.ErrorCode;
import org.itrip.common.MD5;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService{
    @Resource
    private ItripUserMapper itripUserMapper;
    @Resource
    private EmailService emailService;
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
            String activeCode=MD5.getMd5(vo.getUserCode(),16);
            emailService.send(vo.getUserCode(),activeCode);
            itripUserMapper.insertItripUser(user);
        }else {
            return DtoUtil.returnFail("此用户已存在", ErrorCode.AUTH_USER_ALREADY_EXISTS);
        }
        return DtoUtil.returnSuccess("添加成功！");
    }
}
