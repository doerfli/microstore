package li.doerf.microstore.customer.services

import li.doerf.microstore.customer.entities.OrderId
import li.doerf.microstore.customer.repositories.OrderIdRepository
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrderService @Autowired constructor(
        val orderIdRepository: OrderIdRepository
) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    fun getOrderId(customerId: String): Long {
        val orderId = orderIdRepository.findById(customerId).orElse(initOrderId(customerId))
        try {
            return orderId.nextOrderId
        } finally {
            orderId.nextOrderId = orderId.nextOrderId + 1
            orderIdRepository.save(orderId)
            log.debug("updated orderid $orderId")
        }
    }

    private fun initOrderId(customerId: String): OrderId {
        val orderId = OrderId(customerId, 100000)
        orderIdRepository.save(orderId)
        log.info("created orderId $orderId")
        return orderId
    }

}
