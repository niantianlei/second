package nian.shop.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisPoolFactory {
	@Autowired
	RedisConfig redisConfig;
	
	@Bean
	public JedisPool JedisPoolFactory() {
		JedisPoolConfig jedispoolConfig = new JedisPoolConfig();
		jedispoolConfig.setMaxIdle(jedispoolConfig.getMaxIdle());
		jedispoolConfig.setMaxTotal(jedispoolConfig.getMaxTotal());
		jedispoolConfig.setMaxWaitMillis(jedispoolConfig.getMaxWaitMillis() * 1000);
		JedisPool jedisPool = new JedisPool(jedispoolConfig, redisConfig.getHost(), redisConfig.getPort(),
				redisConfig.getTimeout() * 1000);
		return jedisPool;

	}
}
