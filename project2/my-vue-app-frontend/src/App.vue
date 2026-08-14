<template>
  <div id="app">
    <nav class="app-nav">
      <div class="nav-brand">
        <span class="nav-icon">📚</span>
        <span>SUSTC PubMed System</span>
      </div>
      <div class="nav-user" v-if="isAuthenticated">
        <span class="user-badge" :class="userRole.toLowerCase()">{{ userRole }}</span>
        <span class="user-name">{{ user?.name || 'User' }}</span>
        <button class="logout-btn" @click="handleLogout">Logout</button>
      </div>
    </nav>
    <main class="app-main">
      <router-view />
    </main>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'App',
  computed: {
    ...mapGetters(['isAuthenticated', 'userRole', 'user'])
  },
  methods: {
    ...mapActions(['logout']),
    handleLogout() {
      this.logout();
      this.$router.push('/login');
    }
  }
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  background: #f5f7fa;
  color: #1a1a2e;
  min-height: 100vh;
}

#app {
  min-height: 100vh;
}

.app-nav {
  background: #1a1a2e;
  color: white;
  padding: 0 2rem;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: 700;
  font-size: 1.1rem;
}

.nav-icon {
  font-size: 1.5rem;
}

.nav-user {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.user-badge.admin {
  background: #10b981;
  color: white;
}

.user-badge.user {
  background: #3b82f6;
  color: white;
}

.user-badge.journal_admin {
  background: #8b5cf6;
  color: white;
}

.user-name {
  font-weight: 500;
}

.logout-btn {
  background: rgba(255,255,255,0.1);
  border: 1px solid rgba(255,255,255,0.2);
  color: white;
  padding: 0.4rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.85rem;
  transition: all 0.2s;
}

.logout-btn:hover {
  background: rgba(255,255,255,0.2);
}

.app-main {
  min-height: calc(100vh - 60px);
}
</style>
