<template>
  <div class="management-page">
    <section class="page-header">
      <div>
        <p class="eyebrow">JOURNAL_ADMIN · journal:update</p>
        <h1>Journal Management</h1>
        <p>Maintain journal metadata and review article-to-journal assignments.</p>
      </div>
      <router-link to="/dashboard" class="back-link">← Back to Dashboard</router-link>
    </section>

    <section class="management-card">
      <div class="card-heading">
        <div>
          <h2>Update journal metadata</h2>
          <p>Changes below demonstrate the fields that a Journal Admin is allowed to edit.</p>
        </div>
        <span class="mode-badge">Demo workspace</span>
      </div>

      <form class="journal-form" @submit.prevent="saveJournal">
        <label>
          Journal name
          <input v-model.trim="journal.name" required />
        </label>
        <label>
          ISSN
          <input v-model.trim="journal.issn" placeholder="e.g. 1367-4803" />
        </label>
        <label>
          Publisher
          <input v-model.trim="journal.publisher" />
        </label>
        <label>
          Country
          <input v-model.trim="journal.country" />
        </label>
        <label class="wide-field">
          Article assignment to review
          <select v-model="journal.articlePmid">
            <option value="39012345">PMID 39012345 — Graph Neural Networks for Drug-Target Interaction Prediction</option>
            <option value="39567890">PMID 39567890 — Machine Learning Models for Cancer Detection</option>
          </select>
        </label>
        <div class="form-actions wide-field">
          <button type="submit" class="primary-btn">Save journal update</button>
          <button type="button" class="secondary-btn" @click="resetForm">Reset</button>
        </div>
      </form>
      <p v-if="notice" class="notice">{{ notice }}</p>
    </section>

    <section class="scope-card">
      <h2>Role boundary</h2>
      <p>A Journal Admin can update <code>Journal</code> records and <code>Article_Journal</code> relationships, but cannot import data, truncate tables, or manage users.</p>
    </section>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';

const initialJournal = () => ({
  name: 'Bioinformatics',
  issn: '1367-4803',
  publisher: 'Oxford University Press',
  country: 'United Kingdom',
  articlePmid: '39012345'
});

export default {
  name: 'JournalManagement',
  data() {
    return { journal: initialJournal(), notice: '' };
  },
  computed: {
    ...mapGetters(['userRole'])
  },
  methods: {
    saveJournal() {
      this.notice = `Saved a demo update for ${this.journal.name}. In the local deployment, this submits a journal:update request that the Spring Boot API must authorize.`;
    },
    resetForm() {
      this.journal = initialJournal();
      this.notice = '';
    }
  }
};
</script>

<style scoped>
.management-page { max-width: 1000px; margin: 0 auto; padding: 2rem; }
.page-header { display: flex; justify-content: space-between; gap: 1rem; align-items: flex-start; margin-bottom: 1.5rem; }
.eyebrow { color: #4f46e5; font-size: .75rem; font-weight: 700; letter-spacing: .08em; }
h1 { margin: .35rem 0; color: #1a1a2e; } .page-header p:not(.eyebrow) { color: #64748b; }
.back-link { color: #4f46e5; text-decoration: none; font-weight: 600; white-space: nowrap; }
.management-card, .scope-card { background: white; border-radius: 16px; padding: 1.75rem; box-shadow: 0 4px 20px rgba(0,0,0,.08); }
.card-heading { display: flex; justify-content: space-between; gap: 1rem; margin-bottom: 1.25rem; } h2 { color: #1e293b; font-size: 1.15rem; } .card-heading p, .scope-card p { color: #64748b; margin-top: .35rem; line-height: 1.55; }
.mode-badge { height: fit-content; color: #4338ca; background: #e0e7ff; padding: .3rem .6rem; border-radius: 99px; font-size: .75rem; font-weight: 600; }
.journal-form { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 1rem; }
label { display: flex; flex-direction: column; gap: .4rem; color: #374151; font-size: .85rem; font-weight: 600; } input, select { padding: .7rem .8rem; border: 1px solid #cbd5e1; border-radius: 8px; font: inherit; color: #1e293b; background: white; } input:focus, select:focus { outline: 2px solid #c7d2fe; border-color: #667eea; }
.wide-field { grid-column: 1 / -1; }.form-actions { display: flex; gap: .75rem; }.primary-btn, .secondary-btn { border-radius: 8px; padding: .7rem 1rem; cursor: pointer; font-weight: 600; }.primary-btn { border: 0; background: #4f46e5; color: white; }.secondary-btn { border: 1px solid #cbd5e1; background: white; color: #475569; }.notice { margin-top: 1rem; padding: .75rem; border-radius: 8px; background: #ecfdf5; color: #047857; font-size: .85rem; }.scope-card { margin-top: 1.5rem; border-left: 4px solid #4f46e5; } code { color: #4338ca; }
@media (max-width: 640px) { .page-header, .card-heading { flex-direction: column; }.journal-form { grid-template-columns: 1fr; }.wide-field { grid-column: auto; } }
</style>
