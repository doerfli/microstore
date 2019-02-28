package li.doerf.microstore.api.controllers

import li.doerf.microstore.TOPIC_CUSTOMERS
import li.doerf.microstore.api.listeners.CustomersListener
import li.doerf.microstore.api.rest.dto.CreateCustomerRequest
import li.doerf.microstore.api.rest.dto.CreateCustomerResponse
import li.doerf.microstore.dto.CustomerCreate
import li.doerf.microstore.dto.CustomerCreated
import li.doerf.microstore.utils.getLogger
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.util.concurrent.ListenableFutureCallback
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/customers")
class CustomerController @Autowired constructor(
        private val kafkaTemplate: KafkaTemplate<String, Any>,
        private val customersListener: CustomersListener
) {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @PostMapping
    fun create(@RequestBody customer: CreateCustomerRequest): CompletableFuture<CreateCustomerResponse> {
        log.info("received request to create customer $customer")
        val event = createCreateCustomerEvent(customer)
        val correlationId = UUID.randomUUID().toString()
        val future = registerCorrelationIdForResponse(correlationId)
        sendEvent(event, correlationId)
        return future
    }

    private fun registerCorrelationIdForResponse(correlationId: String): CompletableFuture<CreateCustomerResponse> {
        return customersListener
                .registerCorrelationIdForResponse(correlationId)
                .thenApply { x -> processResponse(x)}
                .exceptionally { processResponse(
                        // TODO return http status
                        CustomerCreated(
                                "null",
                                "null",
                                "null",
                                "null"
                        )
                ) }

    }

    private fun processResponse(event: CustomerCreated): CreateCustomerResponse {
        return CreateCustomerResponse(
                event.id,
                event.email,
                event.firstname,
                event.lastname
        )
    }

    private fun sendEvent(event: CustomerCreate, correlationId: String) {
        val record = ProducerRecord<String, Any>(TOPIC_CUSTOMERS, event)
        record.headers().add(RecordHeader(KafkaHeaders.CORRELATION_ID, correlationId.toByteArray()))

        val sendFuture = kafkaTemplate.send(record)
        sendFuture.addCallback(object: ListenableFutureCallback<Any> {
            override fun onSuccess(result: Any?) {
                log.debug("onSuccess")
            }

            override fun onFailure(ex: Throwable) {
                log.error("onFailure", ex)
            }
        })
    }

    private fun createCreateCustomerEvent(customer: CreateCustomerRequest): CustomerCreate {
        return CustomerCreate(
                customer.email,
                customer.firstname,
                customer.lastname
        )
    }

}
