<template>
  <div>
    <InventoryItem
        v-for="item in items" v-bind:key="item.id" v-bind:data="item"/>
  </div>
</template>

<script>
    import InventoryItem from "./InventoryItem";

    export default {
        name: "InventoryIndex",
        components: {
            InventoryItem
        },
        computed: {
            items () {
                return this.$store.state.inventory.all
            }
        },
        created: function () {
            this.$store.dispatch("inventory/reload")
        }
    }
</script>

<style scoped>

</style>