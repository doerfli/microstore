package li.doerf.microstore.api.controllers

import li.doerf.microstore.TOPIC_ORDERS
import li.doerf.microstore.api.listeners.OrdersListener
import li.doerf.microstore.api.rest.dto.CreateOrderRequest
import li.doerf.microstore.api.rest.dto.CreateOrderResponse
import li.doerf.microstore.api.rest.dto.OrderStatus
import li.doerf.microstore.dto.kafka.OrderCreate
import li.doerf.microstore.dto.kafka.OrderCreated
import li.doerf.microstore.services.KafkaService
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/orders")
class OrderController @Autowired constructor(
        private val kafkaService: KafkaService,
        private val ordersListener: OrdersListener
) {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @PostMapping
    fun create(@RequestBody order: CreateOrderRequest): CompletableFuture<CreateOrderResponse> {
        log.info("received request for new order $order")
        val event = createOrderCreateEvent(order)
        val correlationId = UUID.randomUUID().toString()
        val future = registerCorrelationIdForResponse(correlationId)
        kafkaService.sendEvent(TOPIC_ORDERS, event, correlationId)
        return future
    }

    private fun createOrderCreateEvent(order: CreateOrderRequest): Any {
        return OrderCreate(
                order.customerId,
                order.itemsIds
        )
    }

    private fun registerCorrelationIdForResponse(correlationId: String): CompletableFuture<CreateOrderResponse> {
        return ordersListener
                .registerCorrelationIdForResponse(correlationId)
                .thenApply { x -> processResponse(x)}
                .exceptionally { throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR) }

    }


    private fun processResponse(event: OrderCreated): CreateOrderResponse {
        return CreateOrderResponse(
                event.id, // TODO
                0, // TODO
                BigDecimal.ZERO, // TODO
                OrderStatus.SHIPPING_FAILED, // TODO
                "something happened" // TODO
        )
    }

}