<template>
  <div>
    <h1 v-if="hasAccess">Welcome to the Dashboard</h1>
    <p v-else>You do not have permission to view this content.</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userRole: null, // This will be set based on the logged-in user's role
    };
  },
  computed: {
    hasAccess() {
      // Check user role against required permissions
      return this.$store.getters.userRole === 'admin'; // Example: only admin can access
    },
  },
  created() {
    // Fetch user role from store or API
    this.userRole = this.$store.state.userRole; // Assuming userRole is stored in Vuex
  },
};
</script>

<style scoped>
h1 {
  color: green;
}
p {
  color: red;
}
</style>