package com.blue.redis;

import redis.clients.jedis.ShardedJedis;

public interface RedisDataSource {
	
	ShardedJedis getRedisClint();
	
	void closeRedisClint(ShardedJedis shardedJedis);
	
//	void returnResource(ShardedJedis shardedJedis,boolean broken);
	//redis封装 		http://www.cnblogs.com/tankaixiong/p/3660075.html
	//redis 购物车 	http://blog.csdn.net/zuoanyinxiang/article/details/50290899
	
	
}
