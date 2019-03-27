<template>
  <div>
    <h1>Customers</h1>
    <ListCustomers />
    <NewCustomer  v-on:new="addNewCustomer"/>
  </div>
</template>

<script>
    import NewCustomer from './NewCustomer'
    import ListCustomers from './ListCustomers'

    export default {
        name: "Customers",
        components: {
            NewCustomer,
            ListCustomers
        },
        methods: {
            addNewCustomer: function(data) {
                this.$store.commit('customers/add', data)
            }
        },
        created: function () {
            this.$store.dispatch('customers/getAll')
        }
    }
</script>

<style scoped>

</style>