package li.doerf.microstore.inventory

import li.doerf.microstore.TOPIC_INVENTORY
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConfigTopics {

    @Bean
    fun topicCustomers(): NewTopic {
        return NewTopic(TOPIC_INVENTORY, 10, 1.toShort())
    }

}
