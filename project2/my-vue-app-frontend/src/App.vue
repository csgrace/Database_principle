<template>
  <div id="app">
    <nav class="app-nav">
      <div class="nav-brand">
        <span class="nav-icon">📚</span>
        <span>SUSTC PubMed System</span>
      </div>
      <span class="nav-caption">Database Principles Course Project</span>
    </nav>
    <main class="app-main">
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  name: 'App'
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

.nav-caption {
  color: #cbd5e1;
  font-size: 0.8rem;
}

.app-main {
  min-height: calc(100vh - 60px);
}

@media (max-width: 560px) {
  .app-nav {
    padding: 0 1rem;
  }

  .nav-caption {
    display: none;
  }
}
</style>
