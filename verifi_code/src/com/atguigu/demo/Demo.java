package com.atguigu.demo;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class Demo {

	Jedis jedis = null;
	
	@Before
	public void before() {
		//连接 Redis 服务
		jedis = new Jedis("192.168.184.128",6379);
		//查看服务是否运行，打出pong表示OK
		System.out.println("connection is OK==========>:"+jedis.ping());
		
	}
	
	@Test
	public void test1() {
		
		System.out.println(jedis.get("k2"));
		jedis.set("k4", "k4_Redis");
		List<String> values = jedis.mget("k1","k2","k3","k4");
		for (String value : values) {
			System.out.println(value);
		}
	}
	
	@After
	public void after() {
		
		jedis.close();
	}

}
