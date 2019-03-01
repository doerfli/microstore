package li.doerf.microstore.customer.services

import li.doerf.microstore.customer.repositories.CustomerRepository
import li.doerf.microstore.dto.CustomerCreate
import li.doerf.microstore.test.BaseTestWithKafkaAndRedis
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Ignore
@SpringBootTest
class CustomerServiceTest @Autowired constructor(
        val customerService: CustomerService,
        val customerRepository: CustomerRepository
) : BaseTestWithKafkaAndRedis() {

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