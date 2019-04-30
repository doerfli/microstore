package li.doerf.microstore.dto.kafka

enum class OrderEndState {
    SHIPPED,
    SHIPPING_FAILED,
    CUSTOMER_NOT_FOUND,
    ITEM_NOT_FOUND,
    ITEM_NOT_ON_STOCK,
    PAYMENT_FAILED
}
