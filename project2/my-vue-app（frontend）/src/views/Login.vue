<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-header">
        <div class="login-logo">📚</div>
        <h1>SUSTC PubMed System</h1>
        <p class="login-subtitle">CS307 Database Principles — Full-Stack Course Project</p>
      </div>

      <form @submit.prevent="login" class="login-form">
        <div class="form-group">
          <label for="username">Username</label>
          <input
            id="username"
            v-model="username"
            type="text"
            placeholder="Enter username (any for demo)"
            required
          />
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <input
            id="password"
            v-model="password"
            type="password"
            placeholder="Enter password (any for demo)"
            required
          />
        </div>

        <div class="form-options">
          <label class="checkbox-label">
            <input type="checkbox" v-model="isAdmin" />
            <span>Login as Admin</span>
          </label>
        </div>

        <div v-if="errorMessage" class="error-msg">{{ errorMessage }}</div>

        <button type="submit" class="login-btn">Sign In</button>

        <p class="demo-hint">🎓 Demo mode — enter any credentials to explore</p>
      </form>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  name: 'Login',
  data() {
    return {
      username: '',
      password: '',
      isAdmin: true,
      errorMessage: ''
    };
  },
  methods: {
    ...mapActions(['loginUser']),
    async login() {
      try {
        await this.loginUser({
          username: this.username,
          password: this.password,
          role: this.isAdmin ? 'admin' : 'user'
        });
        this.$router.push('/');
      } catch (error) {
        this.errorMessage = 'Login failed. Please try again.';
      }
    }
  }
};
</script>

<style scoped>
.login-page {
  min-height: calc(100vh - 60px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-container {
  background: white;
  border-radius: 16px;
  padding: 2.5rem;
  width: 100%;
  max-width: 420px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.15);
}

.login-header {
  text-align: center;
  margin-bottom: 2rem;
}

.login-logo {
  font-size: 3rem;
  margin-bottom: 0.5rem;
}

.login-header h1 {
  font-size: 1.5rem;
  color: #1a1a2e;
  margin-bottom: 0.25rem;
}

.login-subtitle {
  color: #6b7280;
  font-size: 0.85rem;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}

.form-group label {
  font-size: 0.9rem;
  font-weight: 500;
  color: #374151;
}

.form-group input {
  padding: 0.75rem 1rem;
  border: 2px solid #e5e7eb;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.2s;
}

.form-group input:focus {
  outline: none;
  border-color: #667eea;
}

.form-options {
  display: flex;
  align-items: center;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  font-size: 0.9rem;
  color: #4b5563;
}

.checkbox-label input {
  width: 16px;
  height: 16px;
  accent-color: #667eea;
}

.login-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 0.85rem;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.1s, box-shadow 0.2s;
}

.login-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.error-msg {
  background: #fee2e2;
  color: #dc2626;
  padding: 0.75rem;
  border-radius: 8px;
  font-size: 0.85rem;
  text-align: center;
}

.demo-hint {
  text-align: center;
  font-size: 0.8rem;
  color: #9ca3af;
  margin-top: 0.5rem;
}
</style>
