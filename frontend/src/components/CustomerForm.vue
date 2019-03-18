<template>
  <div>
    <input type="text" v-model="firstname" /><br/>
    <input type="text" v-model="lastname" /><br/>
    <input type="text" v-model="email" /><br/>
    <button v-on:click="$emit('save', {firstname: firstname, lastname: lastname, email: email})">Save</button>
  </div>
</template>

<script>
  export default {
        name: "CustomerForm",
        data: function () {
            return {
                firstname: "First name ...",
                lastname: "Last name ...",
                email: "Em@ail ..."
            }
        }
    }
</script>

<style scoped>

</style>