package li.doerf.microstore.customer.listeners

import li.doerf.microstore.TOPIC_CUSTOMERS
import li.doerf.microstore.customer.services.CustomerService
import li.doerf.microstore.dto.CustomerCreate
import li.doerf.microstore.dto.CustomerCreated
import li.doerf.microstore.entities.Customer
import li.doerf.microstore.listeners.ReplayingRecordsListener
import li.doerf.microstore.services.KafkaService
import li.doerf.microstore.utils.getLogger
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CustomersListener @Autowired constructor(
        val customerService: CustomerService,
        val kafkaService: KafkaService
) : ReplayingRecordsListener() {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @KafkaListener(topics = [TOPIC_CUSTOMERS])
    @Transactional
    fun receive(record: ConsumerRecord<String, Any>) {
        log.debug("received: $record")
        val correlationId =
                String(record.headers().headers(KafkaHeaders.CORRELATION_ID).first().value())
        val event = record.value()
        val eventResponse = applyEventToStore(event, correlationId)
        if(isReplaying(record.partition(), record.offset())) {
            return
        }
        processCommand(event, correlationId, eventResponse)
    }

    private fun applyEventToStore(event: Any?, correlationId: String): Any? {
        when(event) {
            is CustomerCreate -> return customerService.create(event)
        }
        return null
    }

    private fun processCommand(event: Any, correlationId: String, eventResponse: Any?) {
        log.debug("processCommand")
        when(event) {
            is CustomerCreate -> {
                val customer = eventResponse as Customer
                kafkaService.sendEvent(
                        CustomerCreated(
                                customer.id,
                                customer.email,
                                customer.firstname,
                                customer.lastname
                        ),
                        correlationId)
            }
        }
    }



}