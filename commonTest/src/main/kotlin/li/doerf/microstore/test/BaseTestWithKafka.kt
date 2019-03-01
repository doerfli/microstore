package li.doerf.microstore.test

import li.doerf.microstore.TOPIC_CUSTOMERS
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.TestPropertySource

@EmbeddedKafka(
        topics = [TOPIC_CUSTOMERS],
        partitions = 10)
@TestPropertySource(properties = ["spring.kafka.bootstrap-servers=\${spring.embedded.kafka.brokers}"])
open class BaseTestWithKafka
