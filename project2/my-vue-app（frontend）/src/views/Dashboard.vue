<template>
  <div class="dashboard">
    <!-- User Info Card -->
    <div class="user-card">
      <div class="user-avatar">{{ userInitials }}</div>
      <div class="user-info">
        <h2>Welcome, {{ user?.name || 'User' }}!</h2>
        <div class="user-meta">
          <span class="role-badge" :class="userRole">{{ userRole }}</span>
          <span class="permissions-count">{{ permissionCount }} permissions</span>
        </div>
      </div>
    </div>

    <!-- Stats -->
    <div class="stats-row">
      <div class="stat-box">
        <div class="stat-val">2.5M+</div>
        <div class="stat-key">Articles</div>
      </div>
      <div class="stat-box">
        <div class="stat-val">850K+</div>
        <div class="stat-key">Authors</div>
      </div>
      <div class="stat-box">
        <div class="stat-val">12K+</div>
        <div class="stat-key">Journals</div>
      </div>
      <div class="stat-box">
        <div class="stat-val">6</div>
        <div class="stat-key">Services</div>
      </div>
    </div>

    <!-- Services -->
    <div class="section">
      <h2 class="section-title">Available Services</h2>
      <PermissionControl v-if="userRole === 'admin'" />
      <div class="services-grid">
        <div class="service-card" v-for="service in services" :key="service.name">
          <div class="service-icon">{{ service.icon }}</div>
          <h3>{{ service.name }}</h3>
          <ul>
            <li v-for="method in service.methods" :key="method">{{ method }}</li>
          </ul>
        </div>
      </div>
    </div>

    <!-- Tech Stack -->
    <div class="section">
      <h2 class="section-title">Tech Stack</h2>
      <div class="tech-list">
        <span class="tech-tag">Java 17</span>
        <span class="tech-tag">Spring Boot</span>
        <span class="tech-tag">Vue 3</span>
        <span class="tech-tag">Vuex</span>
        <span class="tech-tag">PostgreSQL 16</span>
        <span class="tech-tag">JDBC</span>
        <span class="tech-tag">PL/pgSQL</span>
        <span class="tech-tag">Gradle</span>
        <span class="tech-tag">Vite</span>
      </div>
    </div>

    <div class="demo-banner">
      <strong>🎓 Demo Mode</strong> — This is a frontend showcase. Full functionality requires Spring Boot backend with PostgreSQL.
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import PermissionControl from '@/components/PermissionControl.vue';

export default {
  name: 'Dashboard',
  components: { PermissionControl },
  computed: {
    ...mapGetters(['user', 'userRole']),
    userInitials() {
      const name = this.user?.name || 'U';
      return name.charAt(0).toUpperCase();
    },
    permissionCount() {
      return this.user?.permissions?.length || 0;
    },
    services() {
      return [
        {
          name: 'Article Service',
          icon: '📄',
          methods: ['getArticleByPMID', 'searchArticles', 'getCitationsByYear', 'addArticleAndUpdateIF']
        },
        {
          name: 'Author Service',
          icon: '👤',
          methods: ['getArticlesByAuthor', 'getTopJournal', 'getCollaborationPath']
        },
        {
          name: 'Journal Service',
          icon: '📊',
          methods: ['getImpactFactor', 'updateJournal', 'getArticlesByJournal']
        },
        {
          name: 'Keyword Service',
          icon: '🔑',
          methods: ['getArticleCountByKeyword', 'getKeywordTrends']
        },
        {
          name: 'Grant Service',
          icon: '🌍',
          methods: ['getCountryFundPapers']
        },
        {
          name: 'Database Service',
          icon: '🗄️',
          methods: ['importData', 'truncate', 'getStats', 'getGroupMembers']
        }
      ];
    }
  }
};
</script>

<style scoped>
.dashboard {
  max-width: 1000px;
  margin: 0 auto;
  padding: 2rem;
}

.user-card {
  display: flex;
  align-items: center;
  gap: 1.25rem;
  background: white;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  margin-bottom: 1.5rem;
}

.user-avatar {
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: 700;
}

.user-info h2 {
  font-size: 1.25rem;
  color: #1a1a2e;
  margin-bottom: 0.4rem;
}

.user-meta {
  display: flex;
  gap: 0.75rem;
  align-items: center;
}

.role-badge {
  padding: 0.2rem 0.6rem;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.role-badge.admin {
  background: #d1fae5;
  color: #065f46;
}

.role-badge.user {
  background: #dbeafe;
  color: #1e40af;
}

.permissions-count {
  font-size: 0.85rem;
  color: #6b7280;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1rem;
  margin-bottom: 2rem;
}

.stat-box {
  background: white;
  padding: 1.25rem;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.stat-val {
  font-size: 1.75rem;
  font-weight: 800;
  color: #667eea;
}

.stat-key {
  font-size: 0.85rem;
  color: #6b7280;
  margin-top: 0.25rem;
}

.section {
  background: white;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  margin-bottom: 1.5rem;
}

.section-title {
  font-size: 1.1rem;
  color: #1a1a2e;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #f3f4f6;
}

.services-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1rem;
}

.service-card {
  background: #f9fafb;
  padding: 1.25rem;
  border-radius: 10px;
  border-left: 4px solid #667eea;
}

.service-card h3 {
  font-size: 1rem;
  color: #1a1a2e;
  margin-bottom: 0.5rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.service-card ul {
  list-style: none;
  padding: 0;
}

.service-card li {
  font-size: 0.8rem;
  color: #6b7280;
  padding: 0.2rem 0;
  font-family: 'JetBrains Mono', monospace;
}

.tech-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tech-tag {
  background: #eef2ff;
  color: #4f46e5;
  padding: 0.35rem 0.85rem;
  border-radius: 50px;
  font-size: 0.8rem;
  font-weight: 500;
}

.demo-banner {
  background: #fef3c7;
  border: 1px solid #fbbf24;
  border-radius: 8px;
  padding: 1rem;
  text-align: center;
  font-size: 0.85rem;
  color: #92400e;
}

@media (max-width: 640px) {
  .stats-row {
    grid-template-columns: repeat(2, 1fr);
  }
  .user-card {
    flex-direction: column;
    text-align: center;
  }
}
</style>
