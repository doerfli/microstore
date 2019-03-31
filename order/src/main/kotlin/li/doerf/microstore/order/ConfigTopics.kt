package li.doerf.microstore.order

import li.doerf.microstore.TOPIC_ORDERS
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConfigTopics {

    @Bean
    fun topicOrders(): NewTopic {
        return NewTopic(TOPIC_ORDERS, 10, 1.toShort())
    }

}
