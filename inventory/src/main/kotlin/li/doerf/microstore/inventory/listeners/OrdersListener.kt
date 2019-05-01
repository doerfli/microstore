package li.doerf.microstore.inventory.listeners

import li.doerf.microstore.TOPIC_ORDERS
import li.doerf.microstore.dto.kafka.OrderCustomerExists
import li.doerf.microstore.dto.kafka.OrderItemsReserved
import li.doerf.microstore.inventory.services.InventoryService
import li.doerf.microstore.listeners.ReplayingRecordsListener
import li.doerf.microstore.services.KafkaService
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@KafkaListener(topics = [TOPIC_ORDERS])
@Component
class OrdersListener @Autowired constructor(
        val kafkaService: KafkaService,
        val inventoryService: InventoryService
) : ReplayingRecordsListener() {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    override fun applyEventToStore(event: Any?, correlationId: String): Any? {
        when(event) {
        }
        return null
    }

    override fun handleBusinessLogic(event: Any, correlationId: String, eventResponse: Any?) {
        log.debug("handleBusinessLogic")
        when(event) {
            is OrderCustomerExists -> {
                val totalAmount = inventoryService.reserveItems(event.itemsIds)
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
//            is OrderCreate -> {
//                val oid = UUID.randomUUID().toString()
//                kafkaService.sendEvent(
//                        TOPIC_ORDERS,
//                        oid,
//                        OrderOpened(
//                                oid,
//                                event.customerId,
//                                event.itemsIds
//                        ),
//                        correlationId)
//            }
            // TODO send items when payment successful
        }
    }



}