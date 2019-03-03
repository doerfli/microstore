package li.doerf.microstore.customer.listeners

import li.doerf.microstore.TOPIC_CUSTOMERS
import li.doerf.microstore.customer.services.CustomerService
import li.doerf.microstore.dto.kafka.CustomerCreate
import li.doerf.microstore.dto.kafka.CustomerCreated
import li.doerf.microstore.services.KafkaService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.kafka.support.KafkaHeaders
import java.util.*


@ExtendWith(MockitoExtension::class)
class CustomersListenerTest {

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    /**
     * Returns ArgumentCaptor.capture() as nullable type to avoid java.lang.IllegalStateException
     * when null is returned.
     * source: https://github.com/googlesamples/android-architecture-components/blob/master/BasicRxJavaSampleKotlin/app/src/test/java/com/example/android/observability/MockitoKotlinHelpers.kt
     */
    fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

    @Mock
    private lateinit var customerService: CustomerService
    @Mock
    private lateinit var kafkaService: KafkaService

    @InjectMocks
    private lateinit var customersListener: CustomersListener

    @Test
    fun testReceive_CustomerCreate() {
        val cust = CustomerCreate("some@body.com", "Some", "Body")
        val record = ConsumerRecord(TOPIC_CUSTOMERS, 0, 0, UUID.randomUUID().toString(), cust as Any)
        record.headers().add(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().toByteArray())

        customersListener.receive(record)

        val argumentString = ArgumentCaptor.forClass(String::class.java)
        val argumentEvent = ArgumentCaptor.forClass(CustomerCreated::class.java)
        // hack to work around non-null argument problem
        val verificator = Mockito.verify(kafkaService)
        argumentString.capture()
        argumentString.capture()
        argumentEvent.capture()
        argumentString.capture()
        verificator.sendEvent(uninitialized(), uninitialized(), uninitialized(), uninitialized())

        assertThat(argumentString.allValues[0]).isEqualTo(TOPIC_CUSTOMERS)
        assertThat(argumentString.allValues[1]).isEqualTo(argumentEvent.value.id)
    }

    @Test
    fun testReceiveReplaying_CustomerCreate() {
        val cust = CustomerCreate("some@body.com", "Some", "Body")
        val record = ConsumerRecord(TOPIC_CUSTOMERS, 0, 0, UUID.randomUUID().toString(), cust as Any)
        record.headers().add(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().toByteArray())

        val assignments = mutableMapOf<TopicPartition, Long>()
        val tp = TopicPartition(TOPIC_CUSTOMERS, 0)
        assignments[tp] = 1L
        customersListener.onPartitionsAssigned(assignments, null)

        customersListener.receive(record)

        Mockito.verify(kafkaService, Mockito.never()).sendEvent(any(), any(), any(), any())
    }

    @Test
    fun testReceive_CustomerCreated() {
        val cust = CustomerCreated(UUID.randomUUID().toString(), "some@body.com", "Some", "Body")
        val record = ConsumerRecord(TOPIC_CUSTOMERS, 0, 0, UUID.randomUUID().toString(), cust as Any)
        record.headers().add(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().toByteArray())

        customersListener.receive(record)

        Mockito.verify(customerService).store(cust)
    }

}