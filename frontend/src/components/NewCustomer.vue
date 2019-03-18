<template>
  <div>
    <h2>Create new Customer</h2>
    <CustomerForm v-on:save="saveNewCustomer"/>

  </div>
</template>

<script>
    import CustomerForm from "./CustomerForm";
    export default {
        name: "NewCustomer",
        components: {CustomerForm},
        methods: {
            saveNewCustomer: function(data) {
                console.log("saving");
                console.log(data);
            }
        }
    }
</script>

<style scoped>

</style>