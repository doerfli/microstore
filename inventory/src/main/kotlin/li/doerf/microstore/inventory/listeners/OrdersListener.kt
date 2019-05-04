package li.doerf.microstore.inventory.listeners

import li.doerf.microstore.TOPIC_ORDERS
import li.doerf.microstore.dto.kafka.*
import li.doerf.microstore.inventory.services.InventoryService
import li.doerf.microstore.inventory.services.OrderService
import li.doerf.microstore.listeners.ReplayingRecordsListener
import li.doerf.microstore.services.KafkaService
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@KafkaListener(topics = [TOPIC_ORDERS])
@Component
class OrdersListener @Autowired constructor(
        private val kafkaService: KafkaService,
        private val inventoryService: InventoryService,
        private val orderService: OrderService
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
            is OrderCustomerExists -> {
                val totalAmount = inventoryService.reserveItems(event.id)
                kafkaService.sendEvent(
                        TOPIC_ORDERS,
                        event.id,
                        OrderItemsReserved(
                                event.id,
                                totalAmount
                        ),
                        correlationId
                )
            }
            is OrderPaymentSuccessful -> {
                inventoryService.prepareItemsShipping(event.id)
                kafkaService.sendEvent(
                        TOPIC_ORDERS,
                        event.id,
                        OrderItemsShipped(
                                event.id
                        ),
                        correlationId
                )
            }
        }
    }



}