package com.alibaba.otter.node.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisClient {
    private static final Logger LOG = LoggerFactory.getLogger(RedisClient.class);

	@Autowired
	private JedisPool jedisPool;
	
	public  void set(String key,String value){
		
		Jedis jedis = jedisPool.getResource();
		try {
			 jedis.set(key, value);
		} catch (Exception e) {
			LOG.warn("redis set error: "+e);
		}finally{
			if(jedis!=null){
				jedis.close();
			}
			
		}
		
		
	}
	
public  void hset(String key,String field,String value){
		
		Jedis jedis = jedisPool.getResource();
		try {
			 jedis.hset(key,field, value);
		} catch (Exception e) {
			LOG.warn("redis set error: "+e);
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
	}

	

	public  String hget(String key,String field,String value){
		String ret ="";
		Jedis jedis = jedisPool.getResource();
		try {
			ret = jedis.hget(key,field);
		} catch (Exception e) {
			LOG.warn("redis hget error: "+e);
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
		
		return ret;
	}
	
	
	public  String get(String key){
		String ret ="";
		Jedis jedis = jedisPool.getResource();
		try {
			ret = jedis.get(key);
		} catch (Exception e) {
			LOG.warn("redis get error: "+e);
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
		
		return ret;
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	
	
	
}
