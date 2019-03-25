<template>
  <div>
    <input type="text" v-model="firstname" /><br/>
    <input type="text" v-model="lastname" /><br/>
    <input type="text" v-model="email" /><br/>
    <button v-on:click="$emit('save', {firstname: firstname, lastname: lastname, email: email})">Save</button>
    <button v-on:click="clear()">Clear</button>
  </div>
</template>

<script>
  import faker from 'faker';
  export default {
      name: "CustomerForm",
      data: function () {
          return {
              firstname: faker.name.firstName(),
              lastname: faker.name.lastName(),
              email: faker.internet.email()
          }
      },
      methods: {
          clear: function() {
              this.firstname = "";
              this.lastname = "";
              this.email = "";
          }
      }

    }
</script>

<style scoped>

</style>