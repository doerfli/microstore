<template>
  <div>
    <h1>Customers</h1>
    <ListCustomers v-bind:customers="customers"/>
    <NewCustomer  v-on:new="addNewCustomer"/>
  </div>
</template>

<script>
    import NewCustomer from './NewCustomer'
    import ListCustomers from './ListCustomers'
    import {AXIOS} from "@/http-common"

    export default {
        name: "Customers",
        components: {
            NewCustomer,
            ListCustomers
        },
        data: function() {
            return {
                customers: [],
                errors: []
            }
        },
        methods: {
            addNewCustomer: function(data) {
                console.log(data);
                this.customers.push(data);
            }
        },
        created: function () {
            AXIOS.get(`/customers`).then(response => {
                console.log(response);
                this.customers = response.data
            })
            .catch(e => {
                this.errors.push(e)
            })
        }
    }
</script>

<style scoped>

</style>