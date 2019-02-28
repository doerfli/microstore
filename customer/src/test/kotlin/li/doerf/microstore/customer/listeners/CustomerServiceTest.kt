package li.doerf.microstore.customer.listeners

import li.doerf.microstore.customer.repositories.CustomerRepository
import li.doerf.microstore.customer.services.CustomerService
import li.doerf.microstore.dto.CustomerCreate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest

//@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
class CustomerServiceTest @Autowired constructor(
        val customerService: CustomerService,
        val customerRepository: CustomerRepository
) {

    @Test
    fun testCreate() {
        val customerToCreate = CustomerCreate(
                "some@body.com",
                "Some",
                "Body"
        )

        val cust = customerService.create(customerToCreate)
        assertThat(cust.id).isNotNull()
        assertThat(cust.email).isEqualTo(customerToCreate.email)
        assertThat(cust.firstname).isEqualTo(customerToCreate.firstname)
        assertThat(cust.lastname).isEqualTo(customerToCreate.lastname)

        val newRec = customerRepository.findById(cust.id)
        assertThat(newRec).isNotNull
    }

}