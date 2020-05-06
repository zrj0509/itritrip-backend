package org.itrip.auths.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/api")
@Api(value = "测试swagger",tags = "测试swagger的使用")
public class Test1 {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @RequestMapping(value = "/myswagger",method = RequestMethod.GET)
    public void test1Swag(String zrj){
        System.out.println("name="+zrj);
    }
    @RequestMapping(value = "/neswagger",method = RequestMethod.POST)
    @ApiOperation(tags = "我的方法",httpMethod = "POST",value = "second",notes = "find name",protocols = "http")
    public void test2Swag(@ApiParam(name = "name",required = true) @RequestParam("name") String zrj){
        System.out.println("name="+zrj);
    }
    @RequestMapping("/myredis")
    @ApiOperation(value = "我的redis",tags = "redis",protocols = "http")
    public void testRedis(@ApiParam(name = "name",required = true) @RequestParam("name") String name){
//        可设置缓存时间
//        字符串
        stringRedisTemplate.opsForValue().set("city",name,600, TimeUnit.SECONDS);
        System.out.println("city="+stringRedisTemplate.opsForValue().get("city"));
        stringRedisTemplate.opsForValue().set("backs",name);
        System.out.println("backs="+stringRedisTemplate.opsForValue().get("backs"));
        //字符串的key=value
        stringRedisTemplate.opsForValue().set("test","100");
        //绑定test的值-10；
        System.out.println("test==>"+stringRedisTemplate.opsForValue().get("test"));
        stringRedisTemplate.boundValueOps("test").increment(-10);
        System.out.println("min 10 test==>"+stringRedisTemplate.opsForValue().get("test"));
        //绑定test的值+50；
        stringRedisTemplate.boundValueOps("test").increment(50);
        System.out.println("add 50 test==>"+stringRedisTemplate.opsForValue().get("test"));
        //根据key获取过期时间
        System.out.println("expire test==>"+stringRedisTemplate.getExpire("test"));
        //根据key获取过期时间并换算成指定单位
        System.out.println("expire second test==>"+stringRedisTemplate.getExpire("test",TimeUnit.SECONDS));
        ///根据key删除缓存
        stringRedisTemplate.delete("test");
        //检查key是否存在，返回boolean值
        System.out.println("has test==>"+stringRedisTemplate.hasKey("test"));
        //向指定key中存放set集合
        stringRedisTemplate.opsForSet().add("numbers","1110","1120","1130");
        Set<String> strings=stringRedisTemplate.opsForSet().members("numbers");
        System.out.println("Set");
        for (String str:
             strings) {
            System.out.println(str);
        }
//        list
        stringRedisTemplate.opsForList().leftPushAll("lists","numbers","backs");
        List<String> list=stringRedisTemplate.opsForList().range("lists",0,3);
        System.out.println("LIST");
        for (String str:
             list) {
            System.out.println(str);
        }
//        哈希
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("name","zrj");
        hashMap.put("age","21");
        hashMap.put("sex","男");
        stringRedisTemplate.opsForHash().putAll("hash",hashMap);
        Set<Object> set=stringRedisTemplate.opsForHash().keys("hash");
        for (Object key:
                set) {
            System.out.println("key="+key+"\t value="+stringRedisTemplate.opsForHash().get("hash",key));
        }
//        zset

    }
}
