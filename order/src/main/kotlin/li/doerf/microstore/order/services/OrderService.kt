package li.doerf.microstore.order.services

import li.doerf.microstore.dto.kafka.OrderCustomerExists
import li.doerf.microstore.dto.kafka.OrderEndState
import li.doerf.microstore.dto.kafka.OrderOpened
import li.doerf.microstore.order.entities.Order
import li.doerf.microstore.order.entities.OrderStatus
import li.doerf.microstore.order.repositories.OrderRepository
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

    fun open(event: OrderOpened): Order {
        log.debug("opening order")
        val order = Order(
                event.id,
                event.customerId,
                0, // initialize with 0 ... generated by customerservice
                BigDecimal.ZERO,
                event.itemsIds,
                OrderStatus.OPENED,
                null,
                null
        )
        orderRepository.save(order)
        log.info("Order opened $order")
        return order
    }

    fun updateWithCustomerInfo(event: OrderCustomerExists): Any? {
        val order = orderRepository.findById(event.id).orElseThrow()
        order.customerOrderId = event.customerOrderId
        orderRepository.save(order)
        log.info("order id for order ${event.id} for customer ${event.customerId } is ${event.customerOrderId}")
        return order
    }

    fun getOrder(id: String): Order {
        return orderRepository.findById(id).orElseThrow()
    }

    fun updateState(id: String, state: OrderStatus) {
        val order = orderRepository.findById(id).orElseThrow()
        order.status = state
        orderRepository.save(order)
        log.debug("order $id : set state for to $state")
    }

    fun updateStateAndAmount(id: String, state: OrderStatus, amount: BigDecimal) {
        val order = orderRepository.findById(id).orElseThrow()
        order.status = state
        order.totalAmount = amount
        orderRepository.save(order)
        log.debug("order $id : set state for to $state")
    }

    fun updateStateFinished(id: String, state: OrderEndState, errorText: String?) {
        val order = orderRepository.findById(id).orElseThrow()
        order.status = OrderStatus.FINISHED
        order.endState = state
        order.errorText = errorText
        orderRepository.save(order)
        log.debug("order $id : save end state - $order")
    }

}