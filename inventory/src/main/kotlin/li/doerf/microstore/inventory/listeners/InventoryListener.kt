package li.doerf.microstore.inventory.listeners

import li.doerf.microstore.TOPIC_INVENTORY
import li.doerf.microstore.dto.kafka.InventoryItemAdd
import li.doerf.microstore.dto.kafka.InventoryItemIncreaseQuantity
import li.doerf.microstore.dto.kafka.InventoryItemReserve
import li.doerf.microstore.dto.kafka.InventoryOrderItems
import li.doerf.microstore.inventory.services.InventoryService
import li.doerf.microstore.listeners.ReplayingRecordsListener
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@KafkaListener(topics = [TOPIC_INVENTORY])
@Component
class InventoryListener @Autowired constructor(
        private val inventoryService: InventoryService
) : ReplayingRecordsListener() {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    override fun applyEventToStore(event: Any?, correlationId: String): Any? {
        when(event) {
            is InventoryItemAdd -> inventoryService.addItem(event)
            is InventoryItemIncreaseQuantity -> inventoryService.increaseQuantity(event)
            is InventoryItemReserve -> inventoryService.reserveItem(event)
        }
        return null
    }

    override fun handleBusinessLogic(event: Any, correlationId: String, eventResponse: Any?) {
        log.debug("handleBusinessLogic")
        when(event) {
            is InventoryOrderItems -> inventoryService.orderItems()
        }
    }
}