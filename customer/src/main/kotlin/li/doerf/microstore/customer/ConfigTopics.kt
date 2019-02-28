package li.doerf.microstore.customer

import li.doerf.microstore.TOPIC_CUSTOMERS
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConfigTopics {

    @Bean
    fun topicCustomers(): NewTopic {
        return NewTopic(TOPIC_CUSTOMERS, 10, 1.toShort())
    }

}
