package li.doerf.microstore.customer.listeners

import li.doerf.microstore.TOPIC_CUSTOMERS
import li.doerf.microstore.customer.entities.Customer
import li.doerf.microstore.customer.services.CustomerService
import li.doerf.microstore.dto.kafka.CustomerCreate
import li.doerf.microstore.dto.kafka.CustomerCreated
import li.doerf.microstore.listeners.ReplayingRecordsListener
import li.doerf.microstore.services.KafkaService
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@KafkaListener(topics = [TOPIC_CUSTOMERS])
@Component
class CustomersListener @Autowired constructor(
        val customerService: CustomerService,
        val kafkaService: KafkaService
) : ReplayingRecordsListener() {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    override fun applyEventToStore(event: Any?, correlationId: String): Any? {
        when(event) {
            is CustomerCreate -> return customerService.create(event)
        }
        return null
    }

    override fun processCommand(event: Any, correlationId: String, eventResponse: Any?) {
        log.debug("processCommand")
        when(event) {
            is CustomerCreate -> {
                val customer = eventResponse as Customer
                kafkaService.sendEvent(
                        TOPIC_CUSTOMERS,
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