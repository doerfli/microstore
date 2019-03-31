package li.doerf.microstore.order.listeners

import li.doerf.microstore.TOPIC_ORDERS
import li.doerf.microstore.listeners.ReplayingRecordsListener
import li.doerf.microstore.services.KafkaService
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@KafkaListener(topics = [TOPIC_ORDERS])
@Component
class OrdersListener @Autowired constructor(
        val kafkaService: KafkaService
) : ReplayingRecordsListener() {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    override fun applyEventToStore(event: Any?, correlationId: String): Any? {
        when(event) {
//            is CustomerCreated -> return customerService.store(event)
        }
        return null
    }

    override fun handleBusinessLogic(event: Any, correlationId: String, eventResponse: Any?) {
        log.debug("handleBusinessLogic")
        when(event) {
//            is CustomerCreate -> {
//                val cid = UUID.randomUUID().toString()
//                kafkaService.sendEvent(
//                        TOPIC_CUSTOMERS,
//                        cid,
//                        CustomerCreated(
//                                cid,
//                                event.email,
//                                event.firstname,
//                                event.lastname
//                        ),
//                        correlationId)
//            }
        }
    }



}