package li.doerf.microstore.inventory.repositories

import li.doerf.microstore.inventory.entities.Item
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : CrudRepository<Item, String> {
}