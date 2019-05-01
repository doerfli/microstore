package li.doerf.microstore.payment.services

import li.doerf.microstore.dto.kafka.CustomerIncreaseLimit
import li.doerf.microstore.payment.entities.Customer
import li.doerf.microstore.payment.repositories.CustomerRepository
import li.doerf.microstore.payment.repositories.OrderRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.util.*


@ExtendWith(MockitoExtension::class)
class CreditLimitServiceTest {

    @Mock
    private lateinit var customerRepository: CustomerRepository
    @Mock
    private lateinit var orderRepository: OrderRepository

    @Test
    fun testIncreaseLimit() {
        val uuid = UUID.randomUUID().toString()
        val customer = Customer(
                uuid,
                BigDecimal.ZERO
        )
        Mockito.`when`(customerRepository.findById(uuid)).thenReturn(Optional.of(customer))
        val input = CustomerIncreaseLimit(
                uuid,
                BigDecimal(1000)
        )

        val creditLimitService = CreditLimitService(customerRepository, orderRepository)
        creditLimitService.increaseLimit(input)

        val argument = ArgumentCaptor.forClass(Customer::class.java)
        Mockito.verify(customerRepository).save(argument.capture())

        assertThat(argument.value.creditLimit).isEqualTo(BigDecimal(1000))
    }
}