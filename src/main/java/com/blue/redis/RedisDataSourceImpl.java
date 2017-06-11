package com.blue.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Component(value="redisDataSource")
public class RedisDataSourceImpl implements RedisDataSource {

	private static Log log= LogFactory.getLog(RedisDataSourceImpl.class);
	
	@Autowired
	private ShardedJedisPool jedisPool;
	
	@Override
	public ShardedJedis getRedisClint() {
		try {
			ShardedJedis	shardedJedis= jedisPool.getResource();
			return shardedJedis;
			
		} catch (Exception e) {
			log.error("getRedisClint error", e);
			
		}
		return null;
	}

	@Override
	public void closeRedisClint(ShardedJedis shardedJedis) {
		if (shardedJedis!=null) {
			shardedJedis.close();
		} else {
			log.error("RedisClint closed error");
		}
	}

	/*@Override
	public void returnResource(ShardedJedis shardedJedis, boolean broken) {
		if (broken) {
		} else {

		}
	}*/

}
