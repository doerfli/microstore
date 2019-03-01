package li.doerf.microstore.customer.listeners

import li.doerf.microstore.TOPIC_CUSTOMERS
import li.doerf.microstore.customer.services.CustomerService
import li.doerf.microstore.dto.CustomerCreate
import li.doerf.microstore.entities.Customer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.kafka.support.KafkaHeaders
import java.util.*


//@EmbeddedKafka(partitions = 10,
//        topics = [TOPIC_CUSTOMERS])
@ExtendWith(MockitoExtension::class)
class CustomersListenerTest {

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    @Mock
    private lateinit var customerService: CustomerService

    @InjectMocks
    private lateinit var customersListener: CustomersListener

    @Test
    fun testReceive() {
        val cust = CustomerCreate("some@body.com", "Some", "Body")
        val record = ConsumerRecord(TOPIC_CUSTOMERS, 0, 0, UUID.randomUUID().toString(), cust as Any)
        record.headers().add(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().toByteArray())

        Mockito.`when`(customerService.create(any())).thenReturn(Customer(UUID.randomUUID().toString(), cust.email, cust.firstname, cust.lastname))

        customersListener.receive(record)

        Mockito.verify(customerService).create(any())
        Mockito.verify(customerService).sendEvent(any(), any())
    }

    @Test
    fun testReceiveReplaying() {
        val cust = CustomerCreate("some@body.com", "Some", "Body")
        val record = ConsumerRecord(TOPIC_CUSTOMERS, 0, 0, UUID.randomUUID().toString(), cust as Any)
        record.headers().add(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().toByteArray())

        Mockito.`when`(customerService.create(any())).thenReturn(Customer(UUID.randomUUID().toString(), cust.email, cust.firstname, cust.lastname))

        val assignments = mutableMapOf<TopicPartition, Long>()
        val tp = TopicPartition(TOPIC_CUSTOMERS, 0)
        assignments[tp] = 1L
        customersListener.onPartitionsAssigned(assignments, null)

        customersListener.receive(record)

        Mockito.verify(customerService).create(any())
        Mockito.verify(customerService, Mockito.never()).sendEvent(any(), any())
    }

}