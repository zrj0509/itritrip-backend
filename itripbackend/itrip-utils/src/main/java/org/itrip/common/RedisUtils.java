package org.itrip.common;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisUtils {
    public StringRedisTemplate stringRedisTemplate;

    public RedisUtils() {
    }

    public RedisUtils(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //CURD操作
    //添加key value

    public String getValue(String key){
        return   stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 添加一个值 到redis  修改
     * @param key
     * @param value
     * @return
     */
    public boolean setValue(String key,String value){
        try {
            stringRedisTemplate.opsForValue().set(key, value);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    /**
     * 添加一个值 到redis  设置失败时间
     * @param key
     * @param value
     * @return
     */
    public boolean setValueExpire(String key,String value,long expire){
        try {
            stringRedisTemplate.opsForValue().set(key, value,expire, TimeUnit.SECONDS);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 根据KEY进行指定的删除
     * @param key
     * @return
     */
    public boolean del(String key){
        try {
            stringRedisTemplate.delete(key);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 根据KEYs进行指定的删除
     * @param keys
     * @return
     */
    public boolean del(String ... keys){
        try {
            for(String key:keys) {
                stringRedisTemplate.delete(key);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 查询所有的KEY
     * @param patten
     * @return
     */
    public Set<String> getAllKey(String patten){
        return  stringRedisTemplate.keys(patten);
    }

    /**
     * 判断key是否还存在
     * @param key
     * @return
     */
    public boolean exit(String key){
        return  stringRedisTemplate.hasKey(key);
    }

    public long getTll(String key){
        return stringRedisTemplate.getExpire(key);
    }


}
