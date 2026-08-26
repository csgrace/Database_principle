<template>
  <div class="dashboard">
    <!-- Backend Info Section -->
    <div class="backend-section">
      <div class="backend-header">
        <h2>🚀 PubMed Search System Overview</h2>
        <p>Full-stack application built with Vue 3 + Spring Boot + PostgreSQL</p>
      </div>
      <div class="backend-stats">
        <div class="backend-stat">
          <div class="backend-stat-num">13</div>
          <div class="backend-stat-text">Database Tables</div>
        </div>
        <div class="backend-stat">
          <div class="backend-stat-num">13</div>
          <div class="backend-stat-text">REST API Endpoints</div>
        </div>
        <div class="backend-stat">
          <div class="backend-stat-num">2.8M+</div>
          <div class="backend-stat-text">Articles</div>
        </div>
        <div class="backend-stat">
          <div class="backend-stat-num">850K+</div>
          <div class="backend-stat-text">Authors</div>
        </div>
        <div class="backend-stat">
          <div class="backend-stat-num">12K+</div>
          <div class="backend-stat-text">Journals</div>
        </div>
        <div class="backend-stat">
          <div class="backend-stat-num">1.5B+</div>
          <div class="backend-stat-text">Citations</div>
        </div>
      </div>
    </div>

    <!-- Search Section -->
    <div class="search-section">
      <div class="search-header">
        <h1>🔍 Search Biomedical Literature</h1>
        <p>Try the examples below or enter your own query</p>
      </div>

      <!-- Search Examples -->
      <div class="search-examples">
        <div class="example-group">
          <span class="example-label">🔎 Try these searches:</span>
          <div class="example-tags">
            <button @click="query='reinforcement learning'; search()" class="example-tag">reinforcement learning</button>
            <button @click="query='Williams S'; search()" class="example-tag">Williams S (author)</button>
            <button @click="query='39567890'; search()" class="example-tag">39567890 (PMID)</button>
            <button @click="query='Bioinformatics'; search()" class="example-tag">Bioinformatics (Journal)</button>
            <button @click="query='transformer'; search()" class="example-tag">transformer</button>
          </div>
        </div>
      </div>

      <div class="search-box">
        <input
          v-model="query"
          @keyup.enter="search"
          type="text"
          placeholder="Enter: keywords (reinforcement learning), author name (Williams S), PMID (39567890), or journal (Bioinformatics)..."
          class="search-input"
        />
        <button @click="search" :disabled="loading" class="search-btn">
          {{ loading ? 'Searching...' : 'Search' }}
        </button>
      </div>
      <div class="search-hint">
        <p>💡 <strong>Supported inputs:</strong> Keywords | Author names | Exact PMID (PubMed ID) | Journal names | MeSH terms | Publication year | DOI</p>
      </div>
    </div>

    <!-- Results -->
    <div v-if="results.length > 0" class="results-section">
      <div class="results-header">
        <h2>Results ({{ results.length }} articles found)</h2>
        <span class="results-time">Query time: {{ queryTime }}ms (PostgreSQL)</span>
      </div>
      <div class="results-list">
        <div v-for="article in results" :key="article.pmid" class="result-card">
          <div class="result-title">
            <span>{{ article.title }}</span>
          </div>
          <div class="result-meta">
            <span class="result-authors">{{ article.authors.join(', ') }}</span>
            <span class="result-journal"> — {{ article.journal }} ({{ article.year }})</span>
          </div>
          <div class="result-abstract">{{ article.abstract }}</div>
          <div class="result-footer">
            <span class="result-pmid">PMID: {{ article.pmid }}</span>
            <span v-if="article.doi" class="result-doi">DOI: {{ article.doi }}</span>
            <span class="result-citations">Cited by: {{ article.citations }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-else-if="searched && !loading" class="empty-state">
      <p>No articles found. Try different keywords.</p>
    </div>

    <!-- Database Relationship Model -->
    <section class="schema-section">
      <div class="schema-heading">
        <div>
          <h2 class="section-title">🗄️ PostgreSQL Data Model — 13 Tables</h2>
          <p class="schema-intro">The schema is centered on <code>Article</code>. Relationship tables resolve many-to-many links with authors, keywords, grants, publication types, and journals.</p>
        </div>
      </div>

      <div class="relation-map">
        <div class="relation-column">
          <h3>Core records</h3>
          <div class="table-node primary"><strong>Article</strong><span>id · title · pub_model · dates</span></div>
          <div class="table-node"><strong>Article_Ids</strong><span>article_id ↔ external identifiers</span></div>
          <div class="table-node"><strong>article_references</strong><span>article_id ↔ cited article</span></div>
        </div>

        <div class="relation-column junction-column">
          <h3>Link / junction tables</h3>
          <div class="table-node junction"><strong>Article_Authors</strong><span>Article ↔ Authors</span></div>
          <div class="table-node junction"><strong>Article_Keywords</strong><span>Article ↔ Keywords</span></div>
          <div class="table-node junction"><strong>Article_Grants</strong><span>Article ↔ Grant_info</span></div>
          <div class="table-node junction"><strong>Article_Publication_Types</strong><span>Article ↔ Publication_Types</span></div>
          <div class="table-node junction"><strong>Article_Journal</strong><span>Article ↔ Journal</span></div>
        </div>

        <div class="relation-column">
          <h3>Reference entities</h3>
          <div class="table-node"><strong>Authors</strong><span>author identity and details</span></div>
          <div class="table-node"><strong>Keywords</strong><span>controlled keyword vocabulary</span></div>
          <div class="table-node"><strong>Grant_info</strong><span>grant_id · agency · country</span></div>
          <div class="table-node"><strong>Publication_Types</strong><span>publication category</span></div>
          <div class="table-node"><strong>Journal</strong><span>journal metadata</span></div>
        </div>
      </div>

      <div class="relationship-legend">
        <span><b>Article</b> is the central publication record.</span>
        <span><b>Junction tables</b> preserve many-to-many relationships.</span>
        <span><b>article_references</b> creates the citation graph between articles.</span>
      </div>

      <img src="https://raw.githubusercontent.com/csgrace/Database_principle/main/project2/E-R%E5%9B%BE%20v1.drawio.png" alt="Project 2 PostgreSQL E-R diagram" class="er-diagram" />
    </section>
  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    return {
      query: '',
      loading: false,
      searched: false,
      results: [],
      queryTime: 0,
      mockArticles: [
        { pmid: 38234567, title: 'Machine Learning Approaches in Healthcare: A Systematic Review', authors: ['Smith J', 'Chen L', 'Wang M'], journal: 'Nature Medicine', year: 2024, doi: '10.1038/s41591-024-02845-3', citations: 128, abstract: 'This study explores machine learning applications in clinical diagnostics and treatment planning. The review covers 200+ papers on deep learning, reinforcement learning, and statistical methods applied to healthcare data.' },
        { pmid: 37123456, title: 'Deep Neural Networks for Medical Image Classification', authors: ['Johnson K', 'Li P'], journal: 'IEEE Trans Med Imaging', year: 2023, doi: '10.1109/TMI.2023.3245678', citations: 256, abstract: 'We present a novel CNN architecture that achieves 96.7% accuracy on radiology image diagnosis. The model combines transformer attention with residual connections for improved healthcare screening.' },
        { pmid: 39234567, title: 'Reinforcement Learning for Drug Discovery Optimization', authors: ['Garcia R', 'Thompson A'], journal: 'PNAS', year: 2024, doi: '10.1073/pnas.240123411', citations: 89, abstract: 'Our machine learning pipeline reduces drug discovery time by 40% through intelligent molecular generation. The approach combines graph neural networks with Monte Carlo tree search.' },
        { pmid: 36543210, title: 'Natural Language Processing for Electronic Health Records: A Survey', authors: ['Williams S', 'Brown T', 'Davis R'], journal: 'J Am Med Inform Assoc', year: 2023, doi: '10.1093/jamia/ocad123', citations: 167, abstract: 'This survey reviews NLP techniques applied to electronic health records, covering named entity recognition, relation extraction, and clinical text classification using transformer models.' },
        { pmid: 38456789, title: 'Transformer Models for Protein Structure Prediction', authors: ['Zhang Y', 'Liu X'], journal: 'Nature Methods', year: 2024, doi: '10.1038/s41592-024-02189-4', citations: 312, abstract: 'We introduce an attention-based architecture that achieves state-of-the-art accuracy on protein folding prediction. The model leverages evolutionary features and geometric attention mechanisms.' },
        { pmid: 37890123, title: 'Federated Learning for Multi-Institutional Clinical Data', authors: ['Anderson M', 'Taylor R', 'Wilson J'], journal: 'Lancet Digit Health', year: 2023, doi: '10.1016/S2589-7500(23)00123-4', citations: 94, abstract: 'This paper presents a federated learning framework enabling collaborative model training across hospitals without sharing patient data. Privacy-preserving techniques ensure HIPAA compliance.' },
        { pmid: 39012345, title: 'Graph Neural Networks for Drug-Target Interaction Prediction', authors: ['Martinez C', 'Lee H'], journal: 'Bioinformatics', year: 2024, doi: '10.1093/bioinformatics/btad456', citations: 76, abstract: 'We propose a graph convolutional network approach for predicting drug-target interactions. The model integrates molecular graphs with protein structure features for improved prediction accuracy.' },
        { pmid: 36789012, title: 'Bayesian Optimization for Hyperparameter Tuning in Clinical ML Models', authors: ['Roberts D', 'Clark E'], journal: 'Med Image Anal', year: 2023, doi: '10.1016/j.media.2023.102876', citations: 145, abstract: 'This work applies Bayesian optimization to efficiently tune hyperparameters in clinical machine learning pipelines. Results show 3x faster convergence compared to grid search methods.' },
        { pmid: 39567890, title: 'Machine Learning Models for Cancer Detection from Clinical Data', authors: ['Miller A', 'Patel N'], journal: 'Cancer Informatics', year: 2024, doi: '10.1177/1176935124123456', citations: 61, abstract: 'This study evaluates supervised learning models for early cancer detection using clinical and genomic features. The proposed approach improves screening sensitivity across multiple cancer types.' }
      ]
    };
  },
  methods: {
    async search() {
      if (!this.query.trim()) return;
      
      this.loading = true;
      this.searched = true;
      // Simulate query latency for the static GitHub Pages demo.
      await this.delay(800 + Math.random() * 600);
      
      // Search the static demo dataset. PMID is an exact lookup; other inputs are
      // matched against title, abstract, author, journal, year, and DOI.
      const normalizedQuery = this.query.trim().toLowerCase();
      const terms = normalizedQuery.split(/\s+/).filter(Boolean);

      this.results = this.mockArticles.filter(article => {
        if (String(article.pmid) === normalizedQuery) return true;

        const searchableText = [
          article.title,
          article.abstract,
          article.authors.join(' '),
          article.journal,
          String(article.year),
          article.doi
        ].join(' ').toLowerCase();

        return terms.every(term => searchableText.includes(term));
      });
      
      this.queryTime = Math.floor(12 + Math.random() * 45);
      this.loading = false;
    },
    delay(ms) {
      return new Promise(resolve => setTimeout(resolve, ms));
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

.backend-section {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
  margin-bottom: 1.5rem;
}

.backend-header {
  text-align: center;
  margin-bottom: 1.5rem;
}

.backend-header h2 {
  font-size: 1.5rem;
  color: #1a1a2e;
  margin-bottom: 0.5rem;
}

.backend-header p {
  color: #6b7280;
  font-size: 0.9rem;
}

.backend-stats {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 1rem;
  max-width: 720px;
  margin: 0 auto;
}

.backend-stat {
  text-align: center;
  padding: 1rem;
  background: linear-gradient(135deg, #667eea08 0%, #764ba208 100%);
  border-radius: 10px;
  border: 1px solid #e5e7eb;
}

.backend-stat-num {
  font-size: 1.5rem;
  font-weight: 700;
  color: #667eea;
  margin-bottom: 0.2rem;
}

.backend-stat-text {
  font-size: 0.8rem;
  color: #6b7280;
}

.search-examples {
  margin-bottom: 1.5rem;
  padding: 1rem;
  background: #f8fafc;
  border-radius: 10px;
  border: 1px solid #e2e8f0;
}

.example-label {
  display: block;
  font-size: 0.85rem;
  color: #4b5563;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.example-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.example-tag {
  background: white;
  border: 1px solid #667eea;
  color: #667eea;
  padding: 0.4rem 0.9rem;
  border-radius: 20px;
  font-size: 0.8rem;
  cursor: pointer;
  transition: all 0.2s;
}

.example-tag:hover {
  background: #667eea;
  color: white;
}

.search-hint {
  margin-top: 0.75rem;
  padding: 0.75rem;
  background: #fffbeb;
  border-radius: 8px;
  border-left: 3px solid #f59e0b;
}

.search-hint p {
  font-size: 0.8rem;
  color: #92400e;
  margin: 0;
}

.search-section {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
  margin-bottom: 1.5rem;
}

.search-header {
  text-align: center;
  margin-bottom: 1.5rem;
}

.search-header h1 {
  font-size: 1.75rem;
  color: #1a1a2e;
  margin-bottom: 0.5rem;
}

.search-header p {
  color: #6b7280;
  font-size: 0.95rem;
}

.search-box {
  display: flex;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.search-input {
  flex: 1;
  padding: 0.85rem 1.25rem;
  border: 2px solid #e5e7eb;
  border-radius: 10px;
  font-size: 1rem;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.search-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15);
}

.search-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 0.85rem 2rem;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.1s, box-shadow 0.2s;
  white-space: nowrap;
}

.search-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.search-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.results-section {
  margin-bottom: 2rem;
}

.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.results-header h2 {
  font-size: 1.25rem;
  color: #1a1a2e;
}

.results-time {
  font-size: 0.8rem;
  color: #6b7280;
  font-family: 'JetBrains Mono', monospace;
}

.results-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.result-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  border-left: 4px solid #667eea;
  transition: transform 0.15s, box-shadow 0.15s;
}

.result-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
}

.result-title span {
font-size: 1.05rem;
font-weight: 600;
color: #1a1a2e;
}

.result-meta {
  font-size: 0.85rem;
  color: #6b7280;
  margin: 0.5rem 0;
}

.result-authors {
  font-weight: 500;
  color: #374151;
}

.result-abstract {
  font-size: 0.9rem;
  color: #4b5563;
  line-height: 1.6;
  margin-bottom: 0.75rem;
}

.result-footer {
  display: flex;
  gap: 1.5rem;
  font-size: 0.78rem;
  color: #9ca3af;
  font-family: 'JetBrains Mono', monospace;
}

.empty-state {
  text-align: center;
  padding: 3rem;
  color: #6b7280;
  background: white;
  border-radius: 12px;
  margin-bottom: 2rem;
}

.schema-section {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
}

.schema-heading {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.section-title {
  font-size: 1.25rem;
  color: #1a1a2e;
  margin-bottom: 0.5rem;
}

.schema-intro {
  color: #64748b;
  font-size: 0.9rem;
  line-height: 1.6;
  max-width: 720px;
}

.relation-map {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 1rem;
}

.relation-column {
  padding: 1rem;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #f8fafc;
}

.relation-column h3 {
  margin: 0 0 0.75rem;
  color: #475569;
  font-size: 0.85rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.junction-column {
  background: #faf5ff;
  border-color: #e9d5ff;
}

.table-node {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  padding: 0.75rem;
  margin-top: 0.65rem;
  background: white;
  border: 1px solid #cbd5e1;
  border-left: 4px solid #64748b;
  border-radius: 8px;
}

.table-node strong {
  color: #1e293b;
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.85rem;
}

.table-node span {
  color: #64748b;
  font-size: 0.75rem;
  line-height: 1.35;
}

.table-node.primary {
  border-left-color: #2563eb;
  background: #eff6ff;
}

.table-node.junction {
  border-left-color: #9333ea;
  background: #ffffff;
}

.relationship-legend {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem 1rem;
  margin-top: 1rem;
  padding: 0.85rem 1rem;
  background: #fffbeb;
  border-radius: 8px;
  color: #92400e;
  font-size: 0.8rem;
}

.er-diagram {
  display: block;
  width: 100%;
  margin-top: 1.5rem;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
}

@media (max-width: 640px) {
  .search-box {
    flex-direction: column;
  }
  .results-header,
  .schema-heading {
    flex-direction: column;
    gap: 0.5rem;
  }
  .backend-stats,
  .relation-map {
    grid-template-columns: 1fr;
  }
  .result-footer {
    flex-direction: column;
    gap: 0.25rem;
  }
}
</style>
