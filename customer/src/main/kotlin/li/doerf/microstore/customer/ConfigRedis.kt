package li.doerf.microstore.customer

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class ConfigRedis {
    @Value("\${microstore.redishost}")
    private val redisHost: String? = null
    @Value("\${microstore.redisport}")
    private val redisPort: Int? = null

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.setConnectionFactory(jedisConnectionFactory())
        return template
    }

    @Bean
    fun jedisConnectionFactory(): JedisConnectionFactory {
        val jedisConFactory = JedisConnectionFactory()
        jedisConFactory.hostName = redisHost!!
        jedisConFactory.port = redisPort!!
        return jedisConFactory
    }
}