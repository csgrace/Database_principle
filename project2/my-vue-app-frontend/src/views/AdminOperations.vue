<template>
  <div class="management-page">
    <section class="page-header">
      <div>
        <p class="eyebrow">ADMIN · system operations</p>
        <h1>Administration Center</h1>
        <p>Review protected operations for data ingestion and user access control.</p>
      </div>
      <router-link to="/dashboard" class="back-link">← Back to Dashboard</router-link>
    </section>

    <section class="operations-grid">
      <article class="operation-card">
        <span class="operation-icon">⇪</span>
        <h2>Import data</h2>
        <p>Load validated PubMed source data into article, author, journal, and relationship tables.</p>
        <button @click="operate('Data import')">Start import</button>
      </article>
      <article class="operation-card">
        <span class="operation-icon">♙</span>
        <h2>User management</h2>
        <p>Create accounts and assign the USER, JOURNAL_ADMIN, or ADMIN role.</p>
        <button @click="operate('User management')">Manage users</button>
      </article>
      <article class="operation-card danger-card">
        <span class="operation-icon">!</span>
        <h2>Database maintenance</h2>
        <p>Reset imported data before a clean reload. This is restricted to system administrators.</p>
        <button class="danger-btn" @click="operate('Database maintenance')">Review maintenance</button>
      </article>
    </section>
    <p v-if="notice" class="notice">{{ notice }}</p>

    <section class="scope-card">
      <h2>Protected-operation rule</h2>
      <p>This page is only reachable by <code>ADMIN</code>. In a local deployment, every action must also be checked by the Spring Boot API and the PostgreSQL database role.</p>
    </section>
  </div>
</template>

<script>
export default {
  name: 'AdminOperations',
  data() { return { notice: '' }; },
  methods: {
    operate(operation) {
      this.notice = `${operation} is available in the demo workflow. The production action must be confirmed and authorized on the backend before it changes PostgreSQL data.`;
    }
  }
};
</script>

<style scoped>
.management-page { max-width: 1000px; margin: 0 auto; padding: 2rem; }.page-header { display: flex; justify-content: space-between; gap: 1rem; align-items: flex-start; margin-bottom: 1.5rem; }.eyebrow { color: #4f46e5; font-size: .75rem; font-weight: 700; letter-spacing: .08em; }h1 { margin: .35rem 0; color: #1a1a2e; }.page-header p:not(.eyebrow) { color: #64748b; }.back-link { color: #4f46e5; text-decoration: none; font-weight: 600; white-space: nowrap; }.operations-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 1rem; }.operation-card, .scope-card { background: white; border-radius: 16px; padding: 1.5rem; box-shadow: 0 4px 20px rgba(0,0,0,.08); }.operation-icon { align-items: center; background: #e0e7ff; border-radius: 10px; color: #4338ca; display: inline-flex; font-size: 1.35rem; font-weight: 700; height: 2.5rem; justify-content: center; width: 2.5rem; }h2 { color: #1e293b; font-size: 1.1rem; margin: 1rem 0 .5rem; }.operation-card p, .scope-card p { color: #64748b; font-size: .9rem; line-height: 1.55; min-height: 4.25rem; }.operation-card button { background: #4f46e5; border: 0; border-radius: 8px; color: white; cursor: pointer; font-weight: 600; margin-top: 1rem; padding: .65rem .85rem; }.danger-card { border: 1px solid #fecaca; }.danger-card .operation-icon { background: #fef2f2; color: #b91c1c; }.danger-btn { background: #b91c1c !important; }.notice { background: #ecfdf5; border-radius: 8px; color: #047857; margin-top: 1rem; padding: .8rem; }.scope-card { border-left: 4px solid #4f46e5; margin-top: 1.5rem; }.scope-card p { min-height: 0; }code { color: #4338ca; }@media (max-width: 720px) { .operations-grid { grid-template-columns: 1fr; }.page-header { flex-direction: column; } }
</style>
