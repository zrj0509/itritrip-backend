package org.zrj;

import jdk.nashorn.internal.scripts.JD;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @ClassName TestRedis
 * @Description: TODO
 * @Author 44401
 * @Date 2020/4/28
 * @Version V1.0
 **/
public class TestRedis {

    @Test
    public void my(){
        //连接本地的 Redis 服务
        Jedis  jedis =new Jedis("localhost");
        System.out.println("connect Ok!");
        //设置 redis 字符串数据
        jedis.set("school","课工场");
        //输出数据
        System.out.println(jedis.get("school"));
    }
}
