package li.doerf.microstore.api.controllers

import li.doerf.microstore.api.listeners.CustomersListener
import li.doerf.microstore.api.rest.dto.CreateCustomerRequest
import li.doerf.microstore.dto.CustomerCreated
import li.doerf.microstore.services.KafkaService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import java.util.concurrent.CompletableFuture

@ExtendWith(MockitoExtension::class)
class CustomerControllerTest {
    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
    @Mock
    private lateinit var kafkaService: KafkaService
    @Mock
    private lateinit var customersListener: CustomersListener

    @Test
    fun testCreate() {
        val req = CreateCustomerRequest(
                "some@body.com",
                "some",
                "Body"
        )
        val uuid = UUID.randomUUID().toString()
        val cc = CustomerCreated(
                uuid,
                "some@body.com",
                "some",
                "Body"
        )
        val future = CompletableFuture.supplyAsync{cc}
        Mockito.`when`(customersListener.registerCorrelationIdForResponse(any())).thenReturn(future)
        val customerController = CustomerController(kafkaService, customersListener)

        val res = customerController.create(req)
        val resp = res.get()

        assertThat(resp.id).isEqualTo(uuid)
        assertThat(resp.email).isEqualTo("some@body.com")
        assertThat(resp.firstname).isEqualTo("some")
        assertThat(resp.lastname).isEqualTo("Body")
    }

}