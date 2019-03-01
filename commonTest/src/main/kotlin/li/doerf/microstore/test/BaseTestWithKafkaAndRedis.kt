package li.doerf.microstore.test

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import redis.embedded.RedisServer

open class BaseTestWithKafkaAndRedis : BaseTestWithKafka() {

    companion object {
        private val REDISSERVER = RedisServer(6379)

        @BeforeAll
        @JvmStatic
        internal fun beforeClass() {
            REDISSERVER.start()
        }

        @AfterAll
        @JvmStatic
        internal fun afterClass() {
            REDISSERVER.stop()
        }
    }


}
