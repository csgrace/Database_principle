<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-header">
        <div class="login-logo">📚</div>
        <h1>SUSTC PubMed System</h1>
        <p class="login-subtitle">CS307 Database Principles — Full-Stack Course Project</p>
      </div>

      <form @submit.prevent="login" class="login-form">
        <div class="credentials-row">
          <div class="form-group">
            <label for="username">Username</label>
            <input
              id="username"
              v-model="username"
              type="text"
              placeholder="Enter username"
              required
            />
          </div>

          <div class="form-group">
            <label for="password">Password</label>
            <input
              id="password"
              v-model="password"
              type="password"
              placeholder="Enter password"
              required
            />
          </div>
        </div>

        <fieldset class="role-selector">
          <legend>Demo role</legend>
          <label class="role-option" :class="{ selected: role === 'USER' }">
            <input v-model="role" type="radio" value="USER" />
            <span><strong>User</strong><small>Search and analytics</small></span>
          </label>
          <label class="role-option" :class="{ selected: role === 'JOURNAL_ADMIN' }">
            <input v-model="role" type="radio" value="JOURNAL_ADMIN" />
            <span><strong>Journal Admin</strong><small>Search + journal updates</small></span>
          </label>
          <label class="role-option" :class="{ selected: role === 'ADMIN' }">
            <input v-model="role" type="radio" value="ADMIN" />
            <span><strong>Admin</strong><small>All database operations</small></span>
          </label>
        </fieldset>

        <div v-if="errorMessage" class="error-msg">{{ errorMessage }}</div>

        <button type="submit" class="login-btn">Sign In</button>

        <p class="demo-hint">🎓 GitHub Pages demo: choose a role to preview permissions. On localhost, Spring Boot validates credentials and returns the role stored in PostgreSQL.</p>
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
      role: 'USER',
      errorMessage: ''
    };
  },
  methods: {
    ...mapActions(['loginUser']),
    async login() {
      try {
        let authenticatedRole = this.role;

        // The hosted Pages site has no server. When running locally, use the
        // Spring Boot API so PostgreSQL is the source of truth for the role.
        if (window.location.hostname === 'localhost') {
          const response = await fetch('http://localhost:8082/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: this.username, password: this.password })
          });
          if (!response.ok) throw new Error('Invalid username or password');
          const payload = await response.json();
          authenticatedRole = payload.role;
        }

        await this.loginUser({
          username: this.username,
          password: this.password,
          role: authenticatedRole
        });
        this.$router.push('/dashboard');
      } catch (error) {
        this.errorMessage = error.message || 'Login failed. Please try again.';
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

.credentials-row {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 0.75rem;
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

.role-selector {
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  padding: 0.75rem;
}

.role-selector legend {
  padding: 0 0.4rem;
  color: #374151;
  font-size: 0.85rem;
  font-weight: 600;
}

.role-option {
  display: flex;
  align-items: center;
  gap: 0.65rem;
  padding: 0.6rem;
  border-radius: 8px;
  cursor: pointer;
  color: #374151;
}

.role-option.selected {
  background: #eef2ff;
}

.role-option input {
  accent-color: #667eea;
}

.role-option span {
  display: flex;
  flex-direction: column;
  gap: 0.1rem;
  font-size: 0.85rem;
}

.role-option small {
  color: #6b7280;
  font-size: 0.75rem;
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
  line-height: 1.5;
}

@media (max-width: 560px) {
  .credentials-row {
    grid-template-columns: 1fr;
  }
}
</style>
