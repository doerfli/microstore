package li.doerf.microstore.order.listeners

import li.doerf.microstore.TOPIC_ORDERS
import li.doerf.microstore.dto.kafka.*
import li.doerf.microstore.listeners.ReplayingRecordsListener
import li.doerf.microstore.order.entities.OrderStatus
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
            is OrderCustomerExists -> return orderService.updateWithCustomerInfo(event)
            is OrderItemsReserved -> return orderService.updateState(event.id, OrderStatus.ITEMS_RESERVED)
            is OrderPaymentSuccessful -> return orderService.updateState(event.id, OrderStatus.PAYMENT_SUCCESSFUL)
            is OrderItemsShipped -> return orderService.updateState(event.id, OrderStatus.ITEMS_SHIPPED)
            is OrderFinished -> return orderService.updateState(event.id, OrderStatus.FINISHED)
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
            is OrderItemsShipped -> {
                val order = orderService.getOrder(event.id)
                kafkaService.sendEvent(
                        TOPIC_ORDERS,
                        order.id,
                        OrderFinished(
                                order.id,
                                order.customerId,
                                OrderEndState.SHIPPED,
                                null
                        ),
                        correlationId
                )
            }
        }
    }



}