package li.doerf.microstore.inventory.services

import li.doerf.microstore.dto.kafka.OrderOpened
import li.doerf.microstore.inventory.entities.Order
import li.doerf.microstore.inventory.repositories.OrderRepository
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrderService @Autowired constructor(
        val orderRepository: OrderRepository
){
    companion object {

        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    fun open(event: OrderOpened) {
        val order = Order(
                event.id,
                event.itemsIds
        )
        orderRepository.save(order)
        log.debug("saved order $order")
    }

    fun delete(id: String) {
        val order = orderRepository.findById(id)
        // TODO revert reservation when order was reserved but not shipped
        order.ifPresent{
            orderRepository.delete(order.get())
        }
        log.debug("removed order")
    }

}