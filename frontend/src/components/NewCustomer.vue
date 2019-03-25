<template>
  <div>
    <h2>Create new Customer</h2>
    <CustomerForm v-on:save="saveNewCustomer"/>

  </div>
</template>

<script>
    import CustomerForm from "./CustomerForm";
    // import axios from 'axios'
    import {AXIOS} from "@/http-common"

    export default {
        name: "NewCustomer",
        components: {CustomerForm},
        data: function() {
            return {
                errors: []
            }
        },
        methods: {
            saveNewCustomer: function(data) {
                console.log("saving");
                console.log(data);
                AXIOS.post(`/customers`, {
                    firstname: data.firstname,
                    lastname: data.lastname,
                    email: data.email
                }).then(response => {
                    console.log(response);
                    this.$emit('new', response.data);
                })
                .catch(e => {
                    this.errors.push(e)
                })
            }
        }
    }
</script>

<style scoped>

</style>