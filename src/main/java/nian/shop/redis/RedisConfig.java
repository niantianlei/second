package nian.shop.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix="redis")
@Data
public class RedisConfig {
	private String host;
	private int port;
	private int timeout;
	private int poolMaxTotal;
	private int poolMaxIdle;
	private int poolMaxWait;
}
