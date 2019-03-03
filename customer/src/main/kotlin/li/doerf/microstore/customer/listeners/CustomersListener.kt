package li.doerf.microstore.customer.listeners

import li.doerf.microstore.TOPIC_CUSTOMERS
import li.doerf.microstore.customer.services.CustomerService
import li.doerf.microstore.dto.kafka.CustomerCreate
import li.doerf.microstore.dto.kafka.CustomerCreated
import li.doerf.microstore.listeners.ReplayingRecordsListener
import li.doerf.microstore.services.KafkaService
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.*

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
            is CustomerCreated -> return customerService.store(event)
        }
        return null
    }

    override fun handleBusinessLogic(event: Any, correlationId: String, eventResponse: Any?) {
        log.debug("handleBusinessLogic")
        when(event) {
            is CustomerCreate -> {
                val cid = UUID.randomUUID().toString()
                kafkaService.sendEvent(
                        TOPIC_CUSTOMERS,
                        cid,
                        CustomerCreated(
                                cid,
                                event.email,
                                event.firstname,
                                event.lastname
                        ),
                        correlationId)
            }
        }
    }



}