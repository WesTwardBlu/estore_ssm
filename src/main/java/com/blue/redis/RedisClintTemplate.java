package com.blue.redis;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.ShardedJedis;


/**
 * 对jedis的封装
 * */
@Repository("redisClintTemplate")
public class RedisClintTemplate {
	private static Log log= LogFactory.getLog(RedisClintTemplate.class);
	
	@Resource(name="redisDataSource")
	private RedisDataSource redisDataSource;
	
	public Long hset(String key,String field,String value){
		Long result= null; 
		ShardedJedis jedis= redisDataSource.getRedisClint();
		try {
			result = jedis.hset(key, field, value);
		} catch (Exception e) {
			log.error("RedisClintTemplate hset error\n"+ e.getMessage(), e);
		} finally{
			redisDataSource.closeRedisClint(jedis);
		}
		
		return result;
	}
	
	
	public String hget(String key,String field){
		String result= null; 
		ShardedJedis jedis= redisDataSource.getRedisClint();
		try {
			result = jedis.hget(key, field);
		} catch (Exception e) {
			log.error("RedisClintTemplate hset error\n"+ e.getMessage(), e);
		} finally{
			redisDataSource.closeRedisClint(jedis);
		}
		
		return result;
	}
	/**
	 * hset与hsetnx:hset创建新的或者覆盖旧的;hsetnx:若key.field不存在，则创建。若已经存在，则不操作
	 * */
	public Long hsetnx(String key,String field,String value){
		Long result= null; 
		ShardedJedis jedis= redisDataSource.getRedisClint();
		try {
			result = jedis.hsetnx(key, field, value);
		} catch (Exception e) {
			log.error("RedisClintTemplate hsetnx error\n"+ e.getMessage(), e);
		} finally{
			redisDataSource.closeRedisClint(jedis);
		}
		
		return result;
	}
	
	public Long hincrBy(String key,String field,long value){
		Long result= null;
		ShardedJedis jedis= redisDataSource.getRedisClint();
		try {
			result = jedis.hincrBy(key, field, value);
		} catch (Exception e) {
			log.error("RedisClintTemplate hincrBy error\n"+ e.getMessage(), e);
		} finally{
			redisDataSource.closeRedisClint(jedis);
		}
		
		return result;
	}
	
	public boolean hexists(String key,String field){
		boolean result= false;
		ShardedJedis jedis= redisDataSource.getRedisClint();
		try {
			result = jedis.hexists(key, field);
		} catch (Exception e) {
			log.error("RedisClintTemplate hexists error\n"+ e.getMessage(), e);
		} finally{
			redisDataSource.closeRedisClint(jedis);
		}
		
		
		return result;
	}
	
	public Long del(String key){
		Long result= null;
		ShardedJedis jedis= redisDataSource.getRedisClint();
		try {
			result = jedis.del(key);
		} catch (Exception e) {
			log.error("RedisClintTemplate del error\n"+ e.getMessage(), e);
		} finally{
			redisDataSource.closeRedisClint(jedis);
		}
		return result;
	}
	
	public Long hdel(String key,String field){
		Long result= null;
		ShardedJedis jedis= redisDataSource.getRedisClint();
		try {
			result = jedis.hdel(key,field);
		} catch (Exception e) {
			log.error("RedisClintTemplate hdel error\n"+ e.getMessage(), e);
		} finally{
			redisDataSource.closeRedisClint(jedis);
		}
		return result;
	}
	
	public Long hlen(String key){
		Long result= null;
		ShardedJedis jedis= redisDataSource.getRedisClint();
		try {
			result = jedis.hlen(key);
		} catch (Exception e) {
			log.error("RedisClintTemplate hlen error\n"+ e.getMessage(), e);
		} finally{
			redisDataSource.closeRedisClint(jedis);
		}
		return result;
		
	}
	
	public Map<String, String> hgetAll(String key){
		Map<String, String> result= null;
		ShardedJedis jedis= redisDataSource.getRedisClint();
		try {
			result = jedis.hgetAll(key);
		} catch (Exception e) {
			log.error("RedisClintTemplate hgetAll error\n"+ e.getMessage(), e);
		} finally{
			redisDataSource.closeRedisClint(jedis);
			
		}
		return result;
	}
	
	
	
}
