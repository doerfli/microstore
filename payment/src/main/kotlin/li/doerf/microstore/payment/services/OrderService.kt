package li.doerf.microstore.payment.services

import li.doerf.microstore.dto.kafka.OrderOpened
import li.doerf.microstore.payment.entities.Order
import li.doerf.microstore.payment.repositories.OrderRepository
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal

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
                event.customerId,
                BigDecimal.ZERO
        )
        orderRepository.save(order)
        log.debug("order stored: $order")
    }

}