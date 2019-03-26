<template>
  <div>
    {{data.name}} - On stock: {{data.quantityAvailable}} @ ₸ {{data.price | amount}}
  </div>
</template>

<script>
    export default {
        name: "InventoryItem",
        props: {
            data: {}
        },
        filters: {
            amount: function(amountDbl) {
                return amountDbl.toFixed(2)
            }
        }
    }
</script>

<style scoped>

</style>