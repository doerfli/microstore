package li.doerf.microstore.api.listeners

import li.doerf.microstore.TOPIC_CUSTOMERS
import li.doerf.microstore.dto.CustomerCreated
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.support.KafkaHeaders
import java.util.*

@SpringBootTest
class CustomersListenerTest @Autowired constructor(
        private val customersListener: CustomersListener
){

//    private fun <T> any(): T {
//        Mockito.any<T>()
//        return uninitialized()
//    }
//
//    private fun <T> uninitialized(): T = null as T

    @Test
    fun testReceive() {
        val correlationId = UUID.randomUUID().toString()
        val custId = UUID.randomUUID().toString()
        val cust = CustomerCreated(custId, "some@body.com", "Some", "Body")
        val record = ConsumerRecord(TOPIC_CUSTOMERS, 0, 0, UUID.randomUUID().toString(), cust as Any)
        record.headers().add(KafkaHeaders.CORRELATION_ID, correlationId.toByteArray())

        val future = customersListener.registerCorrelationIdForResponse(correlationId)
        customersListener.receive(record)

        assertThat(future.join().id).isEqualTo(custId)
    }
}