package li.doerf.microstore.order.services

import li.doerf.microstore.dto.kafka.OrderOpened
import li.doerf.microstore.order.entities.Order
import li.doerf.microstore.order.entities.OrderStatus
import li.doerf.microstore.order.repositories.OrderRepository

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

    fun open(event: OrderOpened): Order {
        log.debug("opening order")
        val order = Order(
                event.id,
                event.customerId,
                event.itemsIds,
                OrderStatus.OPENED
        )
        orderRepository.save(order)
        log.info("Order opened $order")
        return order
    }

}