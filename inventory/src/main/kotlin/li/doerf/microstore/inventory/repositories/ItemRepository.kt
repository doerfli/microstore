package li.doerf.microstore.inventory.repositories

import li.doerf.microstore.inventory.entities.Item
import org.springframework.data.repository.CrudRepository

interface ItemRepository : CrudRepository<Item, String> {
}