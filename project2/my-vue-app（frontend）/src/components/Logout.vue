<template>
  <div>
    <h2>Logout</h2>
    <button @click="logout">Logout</button>
  </div>
</template>

<script>
export default {
  methods: {
    logout() {
      // Logic to handle user logout
      this.$store.dispatch('logout');
      this.$router.push({ name: 'Login' });
    }
  }
}
</script>

<style scoped>
/* Add any specific styles for the Logout component here */
</style>