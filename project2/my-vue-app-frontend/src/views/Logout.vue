<template>
  <div class="logout-page">
    <div class="logout-container">
      <h2>👋 Logged Out Successfully</h2>
      <p>You have been logged out of the SUSTC PubMed System.</p>
      <button @click="goToLogin" class="logout-btn">Return to Login</button>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    goToLogin() {
      this.$router.push('/login');
    }
  }
}
</script>

<style scoped>
.logout-page {
  min-height: calc(100vh - 60px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.logout-container {
  background: white;
  border-radius: 16px;
  padding: 2.5rem;
  text-align: center;
  box-shadow: 0 20px 60px rgba(0,0,0,0.15);
}

.logout-btn {
  margin-top: 1.5rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 0.75rem 2rem;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
}
</style>
