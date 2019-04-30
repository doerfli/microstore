package li.doerf.microstore.customer.repositories

import li.doerf.microstore.customer.entities.OrderId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderIdRepository : CrudRepository<OrderId, String>