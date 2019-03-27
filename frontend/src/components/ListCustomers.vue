<template>
  <div>
    <CustomerLine
        v-for="customer in customers" v-bind:key="customer.id" v-bind:data="customer"/>
  </div>
</template>

<script>
    import CustomerLine from "./CustomerLine.vue"

    export default {
        name: "ListCustomers",
        components: {CustomerLine},
        computed: {
            customers () {
                return this.$store.state.customers.all
            }
        }
    }
</script>

<style scoped>

</style>