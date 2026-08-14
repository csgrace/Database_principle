<template>
  <div class="dashboard">
    <h1>📚 PubMed Literature Search System</h1>
    <p class="subtitle">CS307 Database Principles — Full-Stack Course Project</p>

    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-number">2,500,000+</div>
        <div class="stat-label">Articles Indexed</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">850,000+</div>
        <div class="stat-label">Authors</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">12,000+</div>
        <div class="stat-label">Journals</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">8</div>
        <div class="stat-label">REST Endpoints</div>
      </div>
    </div>

    <div class="services-section">
      <h2>Available Services</h2>

      <div class="service-category">
        <h3>📄 Article Service</h3>
        <ul>
          <li><code>getArticleByPMID</code> — Retrieve article details by PubMed ID</li>
          <li><code>getArticleCitationsByYear</code> — Citation trends over years</li>
          <li><code>addArticleAndUpdateIF</code> — Add article and update impact factor</li>
          <li><code>searchArticles</code> — Full-text search with pagination</li>
        </ul>
      </div>

      <div class="service-category">
        <h3>👤 Author Service</h3>
        <ul>
          <li><code>getArticlesByAuthorSortedByCitations</code> — Author's publications ranked</li>
          <li><code>getJournalWithMostArticlesByAuthor</code> — Author's primary journal</li>
          <li><code>getMinArticlesToLinkAuthors</code> — Collaboration path analysis</li>
        </ul>
      </div>

      <div class="service-category">
        <h3>📊 Journal Service</h3>
        <ul>
          <li><code>getImpactFactor</code> — Get journal impact factor</li>
          <li><code>updateJournalName</code> — Update journal information</li>
          <li><code>getArticlesByJournal</code> — List journal articles</li>
        </ul>
      </div>

      <div class="service-category">
        <h3>🔑 Keyword Service</h3>
        <ul>
          <li><code>getArticleCountByKeywordInPastYears</code> — Keyword trend analysis</li>
        </ul>
      </div>

      <div class="service-category">
        <h3>🌍 Grant Service</h3>
        <ul>
          <li><code>getCountryFundPapers</code> — Country funding analysis</li>
        </ul>
      </div>

      <div class="service-category">
        <h3>🗄️ Database Service</h3>
        <ul>
          <li><code>importData</code> — Bulk data import via GoodLoader</li>
          <li><code>truncate</code> — Clear database tables</li>
          <li><code>sum</code> — Database statistics overview</li>
          <li><code>getGroupMembers</code> — Group member information</li>
        </ul>
      </div>
    </div>

    <div class="tech-stack">
      <h2>Tech Stack</h2>
      <div class="tech-pills">
        <span class="tech-pill">Java 17</span>
        <span class="tech-pill">Spring Boot</span>
        <span class="tech-pill">Vue 3</span>
        <span class="tech-pill">Vuex</span>
        <span class="tech-pill">Vue Router</span>
        <span class="tech-pill">PostgreSQL 16</span>
        <span class="tech-pill">JDBC</span>
        <span class="tech-pill">Gradle</span>
        <span class="tech-pill">PL/pgSQL</span>
        <span class="tech-pill">Vite</span>
      </div>
    </div>

    <div class="demo-note">
      <p>🎓 This is a demo showcase of the CS307 Database Principles project. The full application requires a running Spring Boot backend with PostgreSQL database.</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Dashboard'
};
</script>

<style scoped>
.dashboard {
  max-width: 900px;
  margin: 0 auto;
  padding: 2rem;
}

h1 {
  color: #1a73e8;
  font-size: 2rem;
  margin-bottom: 0.5rem;
}

.subtitle {
  color: #5f6368;
  font-size: 1.1rem;
  margin-bottom: 2rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 1rem;
  margin-bottom: 3rem;
}

.stat-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 1.5rem;
  border-radius: 12px;
  text-align: center;
}

.stat-card:nth-child(2) {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-card:nth-child(3) {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-card:nth-child(4) {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-number {
  font-size: 2rem;
  font-weight: 800;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.9rem;
  opacity: 0.9;
}

.services-section {
  margin-bottom: 2rem;
}

.services-section h2 {
  color: #202124;
  border-bottom: 2px solid #e8eaed;
  padding-bottom: 0.5rem;
  margin-bottom: 1.5rem;
}

.service-category {
  margin-bottom: 1.5rem;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
  border-left: 4px solid #1a73e8;
}

.service-category h3 {
  color: #1a73e8;
  margin-bottom: 0.75rem;
}

.service-category ul {
  list-style: none;
  padding: 0;
}

.service-category li {
  padding: 0.4rem 0;
  color: #5f6368;
  font-size: 0.95rem;
}

.service-category code {
  background: #e8f0fe;
  color: #1967d2;
  padding: 0.15rem 0.4rem;
  border-radius: 4px;
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.85rem;
  margin-right: 0.5rem;
}

.tech-stack {
  margin-bottom: 2rem;
}

.tech-stack h2 {
  color: #202124;
  border-bottom: 2px solid #e8eaed;
  padding-bottom: 0.5rem;
  margin-bottom: 1rem;
}

.tech-pills {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tech-pill {
  background: #e8f0fe;
  color: #1967d2;
  padding: 0.4rem 1rem;
  border-radius: 50px;
  font-size: 0.85rem;
  font-weight: 500;
}

.demo-note {
  background: #fff3cd;
  border: 1px solid #ffc107;
  border-radius: 8px;
  padding: 1rem;
  margin-top: 2rem;
}

.demo-note p {
  color: #856404;
  font-size: 0.9rem;
  margin: 0;
}
</style>
