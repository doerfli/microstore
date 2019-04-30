package li.doerf.microstore.order.listeners

import li.doerf.microstore.TOPIC_ORDERS
import li.doerf.microstore.dto.kafka.OrderCreate
import li.doerf.microstore.dto.kafka.OrderOpened
import li.doerf.microstore.listeners.ReplayingRecordsListener
import li.doerf.microstore.order.services.OrderService
import li.doerf.microstore.services.KafkaService
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.*

@KafkaListener(topics = [TOPIC_ORDERS])
@Component
class OrdersListener @Autowired constructor(
        val kafkaService: KafkaService,
        val orderService: OrderService
) : ReplayingRecordsListener() {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    override fun applyEventToStore(event: Any?, correlationId: String): Any? {
        when(event) {
            is OrderOpened -> return orderService.open(event)
        }
        return null
    }

    override fun handleBusinessLogic(event: Any, correlationId: String, eventResponse: Any?) {
        log.debug("handleBusinessLogic")
        when(event) {
            is OrderCreate -> {
                val oid = UUID.randomUUID().toString()
                kafkaService.sendEvent(
                        TOPIC_ORDERS,
                        oid,
                        OrderOpened(
                                oid,
                                event.customerId,
                                event.itemsIds
                        ),
                        correlationId)
            }
            // TODO generate order number per customer
            // TODO finish order
        }
    }



}