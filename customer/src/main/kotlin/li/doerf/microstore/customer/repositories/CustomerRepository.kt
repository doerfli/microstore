package li.doerf.microstore.customer.repositories

import li.doerf.microstore.entities.Customer
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerRepository : CrudRepository<Customer, UUID>