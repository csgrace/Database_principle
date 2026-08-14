<template>
  <div>
    <div v-if="!isLoggedIn" class="login">
      <h2>Log in</h2>
      <form @submit.prevent="login">
        <div>
          <label for="username">Username:</label>
          <input type="text" v-model="username" required />
        </div>
        <div class="input-group">
          <label for="password" class="password-label">Password:</label>
          <input type="password" v-model="password" required class="password-input" />
        </div>
        <button type="submit" class="login-button">commit</button>
        <div class="message-container">
          <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
        </div>
      </form>
    </div>
    <div v-else class="welcome-container">
      <h2>Welcome, {{ username }}</h2>
      <div v-if="username === 'postgres' && password === 'huarui66'">
        <div class="service-button">
          <button class="main-button" @click="services.ArticleService = !services.ArticleService">ArticleService</button>
          <div v-if="services.ArticleService">
            <button class="detail-button" @click="openArticleCitationsByYearModal">get Article Citations By Year</button>
            <div v-if="showArticleCitationsByYearModal" class="modal">
              <div class="modal-content">
                <span class="close" @click="closeArticleCitationsByYearModal">&times;</span>
                <label>Article ID:</label>
                <input type="number" v-model="articleId">
                <label>Year:</label>
                <input type="number" v-model="year">
                <button @click="getArticleCitationsByYear">Submit</button>
              </div>
            </div>
            <button class="detail-button" @click="openaddArticleAndUpdateIFModal">add Article And Update IF</button>
            <div v-if="showaddArticleAndUpdateIFModal" class="modal">
              <div class="modal-content">
                <span class="close" @click="closeaddArticleAndUpdateIF">&times;</span>
                <label>id:</label>
                <input type="number" v-model="id">
                <label>title:</label>
                <input type="text" v-model="title">
                <label>pubModel:</label>
                <input type="text" v-model="pubModel">
                <label>Date created:</label>
                <input type="date" v-model="Date_created">
                <label>Date completed:</label>
                <input type="date" v-model="Date_completed">
                <button @click="addArticleAndUpdateIF">Submit</button>
              </div>
            </div>
          </div>
        </div>
        <div class="service-button">
          <button class="main-button" @click="services.AuthorService = !services.AuthorService">AuthorService</button>
          <div v-if="services.AuthorService">
            <button class="detail-button" @click="openArticlesByAuthorSortedByCitationsModal">get Articles By Author Sorted By Citations</button>
            <div v-if="showArticlesByAuthorSortedByCitationsModal" class="modal">
              <div class="modal-content">
                <span class="close" @click="closeArticlesByAuthorSortedByCitationsModal">&times;</span>
                <label>Last Name:</label>
                <input type="text" v-model="last_name">
                <label>Fore Name:</label>
                <input type="text" v-model="fore_name">
                <label>Initials:</label>
                <input type="text" v-model="initials">
                <label>Collective name:</label>
                <input type="text" v-model="collective_name">
                <button @click="getArticlesByAuthorSortedByCitations">Submit</button>
              </div>
            </div>
            <button class="detail-button" @click="openJournalWithMostArticlesByAuthorModal">get Journal With Most Articles By Author</button>
            <div v-if="showJournalWithMostArticlesByAuthorModal" class="modal">
              <div class="modal-content">
                <span class="close" @click="closeJournalWithMostArticlesByAuthorModal">&times;</span>
                <label>Last Name:</label>
                <input type="text" v-model="last_name">
                <label>Fore Name:</label>
                <input type="text" v-model="fore_name">
                <label>Initials:</label>
                <input type="text" v-model="initials">
                <label>Collective name:</label>
                <input type="text" v-model="collective_name">
                <button @click="getJournalWithMostArticlesByAuthor">Submit</button>
              </div>
            </div>
            <button class="detail-button" @click="opengetMinArticlesToLinkAuthorsModal">get Min Articles To Link Authors</button>
            <div v-if="showgetMinArticlesToLinkAuthorsModal" class="modal">
            <div class="modal-content">
            <span class="close" @click="closegetMinArticlesToLinkAuthorsModal">&times;</span>
            <label>Author A Last Name:</label>
            <input type="text" v-model="last_name_A">
            <label>Author A Fore Name:</label>
            <input type="text" v-model="fore_name_A">
            <label>Author A Initials:</label>
            <input type="text" v-model="initials_A">
            <label>Author A Collective Name:</label>
            <input type="text" v-model="collective_name_A">
            <label>Author E Last Name:</label>
            <input type="text" v-model="last_name_E">
            <label>Author E Fore Name:</label>
            <input type="text" v-model="fore_name_E">
            <label>Author E Initials:</label>
            <input type="text" v-model="initials_E">
            <label>Author E Collective Name:</label>
            <input type="text" v-model="collective_name_E">
            <button @click="getMinArticlesToLinkAuthors">Submit</button>
            </div>
            </div>
          </div>
        </div>
        <div class="service-button">
          <button class="main-button" @click="services.DatabaseService = !services.DatabaseService">DatabaseService</button>
          <div v-if="services.DatabaseService">
            <button class="detail-button" @click="getGroupMembers">get Group Members</button>
            <button class="detail-button" @click="importData">import Data</button>
            <button class="detail-button" @click="truncate">truncate</button>
            <button class="detail-button" @click="opensumModal">sum</button>
            <div v-if="showsumModal" class="modal">
              <div class="modal-content">
                <span class="close" @click="closesumModal">&times;</span>
                <label>a:</label>
                <input type="text" v-model="a">
                <label>b:</label>
                <input type="text" v-model="b">
                <button @click="sum">Submit</button>
              </div>
            </div>
          </div>
        </div>
        <div class="service-button">
          <button class="main-button" @click="services.GrantService = !services.GrantService">GrantService</button>
          <div v-if="services.GrantService">
            <button class="detail-button" @click="opengetCountryFundPapersModal">get Country Fund Papers</button>
            <div v-if="showgetCountryFundPapersModal" class="modal">
              <div class="modal-content">
                <span class="close" @click="closegetCountryFundPapersModal">&times;</span>
                <label> Country:</label>
                <input type="text" v-model="country">
                <button @click="getCountryFundPapers">Submit</button>
              </div>
            </div>
          </div>
        </div>
        <div class="service-button">
          <button class="main-button" @click="services.JournalService = !services.JournalService">JournalService</button>
          <div v-if="services.JournalService">
            <button class="detail-button" @click="opengetImpactFactorModal">Get Impact Factor</button>
            <div v-if="showgetImpactFactorModal" class="modal">
              <div class="modal-content">
                <span class="close" @click="closegetImpactFactorModal">&times;</span>
                <label> Journal title:</label>
                <input type="text" v-model="journal_title">
                <label> Year:</label>
                <input type="text" v-model="year">
                <button @click="getImpactFactor">Submit</button>
              </div>
            </div>
            <button class="detail-button" @click="openupdateJournalNameModal">update Journal Name</button>
            <div v-if="showupdateJournalNameModal" class="modal">
              <div class="modal-content">
                <span class="close" @click="closeupdateJournalNameModal">&times;</span>
                <label> Journal Id :</label>
                <input type="text" v-model="journalId">
                <label> Journal Title :</label>
                <input type="text" v-model="journalTitle">
                <label> Year:</label>
                <input type="text" v-model="year">
                <label> New name:</label>
                <input type="text" v-model="newName">
                <label> New Id:</label>
                <input type="text" v-model="newId">
                <button @click="updateJournalName">Submit</button>
              </div>
            </div>
          </div>
        </div>
        <div class="service-button">
          <button class="main-button" @click="services.KeywordService = !services.KeywordService">KeywordService</button>
          <div v-if="services.KeywordService">
            <button class="detail-button" @click="opengetArticleCountByKeywordInPastYearsModal">get Article Count By Keyword In Past Years</button>
            <div v-if="showgetArticleCountByKeywordInPastYearsModal" class="modal">
              <div class="modal-content">
                <span class="close" @click="closegetArticleCountByKeywordInPastYearsModal">&times;</span>
                <label> Keyword:</label>
                <input type="text" v-model="keyword">
                <button @click="getArticleCountByKeywordInPastYears">Submit</button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <button @click="logout" class="logout-button">Log out</button>
    </div>
  </div>
</template>

<script>

export default {
  data() {
    return {
      username: '',
      password: '',
      errorMessage: '',
      isLoggedIn: false,
      result: null,
      groupMembers: [],
      importResult: '',
      truncateResult: '',
      sumResult: null,
      impactFactorResult: '',
      updateJournalNameResult: false,
      articleCountByKeyword: [],
      articlesByAuthor: [],
      journalWithMostArticles: '',
      minArticlesToLinkAuthors: null,
      countryFundPapers: [],
      articleCitationsByYear: null,
      addArticleAndUpdateIFResult: null,
      showArticleCitationsByYearModal: false,
      showaddArticleAndUpdateIFModal: false,
      showArticlesByAuthorSortedByCitationsModal: false,
      showJournalWithMostArticlesByAuthorModal: false,
      showgetMinArticlesToLinkAuthorsModal: false,
      showgetCountryFundPapersModal: false,
      showsumModal: false,
      showgetImpactFactorModal: false,
      showupdateJournalNameModal: false,
      showgetArticleCountByKeywordInPastYearsModal: false,
      articleId: null,
      year: null,
      id: null,
      title: '',
      pubModel: '',
      Date_created: '',
      Date_completed: '',
      journalTitle: '',
      authorName: '',
      services: {
        ArticleService: false,
        AuthorService: false,
        DatabaseService: false,
        GrantService: false,
        JournalService: false,
        KeywordService: false,
      },
    };
  },
  methods: {
    openArticleCitationsByYearModal() {
      this.showArticleCitationsByYearModal = true;
    },
    closeArticleCitationsByYearModal() {
      this.showArticleCitationsByYearModal = false;
    },
    getArticleCitationsByYear() {
      if (this.articleId && this.year) {
        fetch(
          `http://localhost:8082/api/getArticleCitationsByYear?articleId=${this.articleId}&year=${this.year}`
        )
          .then((response) => response.text())
          .then((data) => {
            console.log(data);
            this.closeArticleCitationsByYearModal();
          })
          .catch((error) => {
            console.error('Error:', error);
          });
      }
    },
    openaddArticleAndUpdateIFModal() {
      this.showaddArticleAndUpdateIFModal = true;
    },
    closeaddArticleAndUpdateIF() {
      this.showaddArticleAndUpdateIFModal = false;
    },
    addArticleAndUpdateIF() {
    if (this.id && this.title && this.pubModel && this.Date_created && this.Date_completed) {
    fetch(
      `http://localhost:8082/api/addArticleAndUpdateIF?id=${this.id}&title=${this.title}&pubModel=${this.pubModel}&created=${this.Date_created}&completed=${this.Date_completed}`
    )
      .then(response => response.text())
      .then(data => {
        console.log(data);
        this.closeaddArticleAndUpdateIF();
      })
      .catch(error => {
        console.error('Error:', error);
      });
      }
    },
    
    openArticlesByAuthorSortedByCitationsModal() {
      this.showArticlesByAuthorSortedByCitationsModal = true;
    },
    closeArticlesByAuthorSortedByCitationsModal() {
      this.showArticlesByAuthorSortedByCitationsModal = false;
    },
    getArticlesByAuthorSortedByCitations() {
      if ((this.last_name ||this.fore_name || this.initials) || this.collective_name) {
        fetch(
          `http://localhost:8082/api/getArticlesByAuthorSortedByCitations?last_name=${encodeURIComponent(this.last_name)}&fore_name=${encodeURIComponent(this.fore_name)}&initials=${encodeURIComponent(this.initials)}&collective_name=${encodeURIComponent(this.collective_name)}`
        )
          .then((response) => response.text())
          .then((data) => {
            console.log(data);
            this.closeArticlesByAuthorSortedByCitationsModal();
          })
          .catch((error) => {
            console.error('Error:', error);
          });
      }
    },
    openJournalWithMostArticlesByAuthorModal() {
      this.showJournalWithMostArticlesByAuthorModal = true;
    },
    closeJournalWithMostArticlesByAuthorModal() {
      this.showJournalWithMostArticlesByAuthorModal = false;
    },
    getJournalWithMostArticlesByAuthor() {
      if ((this.last_name ||this.fore_name || this.initials) || this.collective_name) {
        fetch(
          `http://localhost:8082/api/getJournalWithMostArticlesByAuthor?last_name=${encodeURIComponent(this.last_name)}&fore_name=${encodeURIComponent(this.fore_name)}&initials=${encodeURIComponent(this.initials)}&collective_name=${encodeURIComponent(this.collective_name)}`
        )
          .then((response) => response.text())
          .then((data) => {
            console.log(data);
            this.closeJournalWithMostArticlesByAuthorModal();
          })
          .catch((error) => {
            console.error('Error:', error);
          });
      }
    },
    opengetMinArticlesToLinkAuthorsModal() {
    this.showgetMinArticlesToLinkAuthorsModal = true;
    },
    closegetMinArticlesToLinkAuthorsModal() {
    this.showgetMinArticlesToLinkAuthorsModal = false;
    },
    getMinArticlesToLinkAuthors() {
    if ((this.last_name_A || this.fore_name_A || this.initials_A || this.collective_name_A) &&
      (this.last_name_E || this.fore_name_E || this.initials_E || this.collective_name_E)) {
    fetch(
      `http://localhost:8082/api/getMinArticlesToLinkAuthors?last_name_A=${encodeURIComponent(this.last_name_A)}&fore_name_A=${encodeURIComponent(this.fore_name_A)}&initials_A=${encodeURIComponent(this.initials_A)}&collective_name_A=${encodeURIComponent(this.collective_name_A)}&last_name_E=${encodeURIComponent(this.last_name_E)}&fore_name_E=${encodeURIComponent(this.fore_name_E)}&initials_E=${encodeURIComponent(this.initials_E)}&collective_name_E=${encodeURIComponent(this.collective_name_E)}`
    )
      .then((response) => response.text())
      .then((data) => {
        console.log(data);
        this.closegetMinArticlesToLinkAuthorsModal();
      })
      .catch((error) => {
        console.error('Error:', error);
      });
      }
    },
    getGroupMembers() {
      fetch('http://localhost:8082/api/getGroupMembers')
        .then(response => response.text())
        .then(data => {
          console.log(data);
        })
        .catch(error => {
          console.error('Error:', error);
        });
    },
    importData() {
      const dataPath = 'path/to/data'; // Replace with actual data path
      fetch(`http://localhost:8082/api/importData?dataPath=${encodeURIComponent(dataPath)}`)
        .then(response => response.text())
        .then(data => {
          console.log(data);
        })
        .catch(error => {
          console.error('Error:', error);
        });
    },
    truncate() {
      fetch('http://localhost:8082/api/truncate')
        .then(response => response.text())
        .then(data => {
          console.log(data);
        })
        .catch(error => {
          console.error('Error:', error);
        });
    },
    opensumModal() {
    this.showsumModal = true;
    },
    closesumModal() {
    this.showsumModal = false;
    },
    sum() {
    if (this.a && this.b) {
      fetch(`http://localhost:8082/api/sum?a=${this.a}&b=${this.b}`)
        .then(response => response.text())
        .then(data => {
          console.log(data);
          this.closesumModal();
        })
        .catch(error => {
          console.error('Error:', error);
        });
      }
    },
    opengetCountryFundPapersModal() {
    this.showgetCountryFundPapersModal = true;
    },
    closegetCountryFundPapersModal() {
    this.showgetCountryFundPapersModal = false;
    },
    getCountryFundPapers() {
    if(this.country){
      fetch(`http://localhost:8082/api/getCountryFundPapers?country=${encodeURIComponent(this.country)}`)
        .then(response => response.text())
        .then(data => {
          console.log(data);
          this.closegetCountryFundPapersModal();
        })
        .catch(error => {
          console.error('Error:', error);
        });
      }
    },
    opengetImpactFactorModal() {
    this.showgetImpactFactorModal = true;
    },
    closegetImpactFactorModal() {
    this.showgetImpactFactorModal = false;
    },
    getImpactFactor() {
      if (this.journal_title && this.year) {
        fetch(`http://localhost:8082/api/getImpactFactor?journal_title=${encodeURIComponent(this.journal_title)}&year=${this.year}`)
          .then(response => response.text())
          .then(data => {
            console.log(data);
            this.closegetImpactFactorModal();
          })
          .catch(error => {
            console.error('Error:', error);
          });
      }
    },
    openupdateJournalNameModal() {
    this.showupdateJournalNameModal = true;
    },
    closeupdateJournalNameModal() {
    this.showupdateJournalNameModal = false;
    },
    updateJournalName() {
    if (this.journalId && this.journalTitle && this.year && this.newName && this.newId) {
      fetch(`http://localhost:8082/api/updateJournalName?journalId=${encodeURIComponent(this.journaId)}&journalTitle=${encodeURIComponent(this.journalTitle)}&year=${this.year}&newName=${encodeURIComponent(this.newName)}&newId=${encodeURIComponent(this.newId)}`)
        .then(response => response.text())
        .then(data => {
          console.log(data);
          this.closeupdateJournalNameModal();
        })
        .catch(error => {
          console.error('Error:', error);
        });
      }
    },
    opengetArticleCountByKeywordInPastYearsModal() {
    this.showgetArticleCountByKeywordInPastYearsModal = true;
    },
    closegetArticleCountByKeywordInPastYearsModal() {
    this.showgetArticleCountByKeywordInPastYearsModal = false;
    },
    getArticleCountByKeywordInPastYears() {
      if (this.keyword) {
      fetch(`http://localhost:8082/api/getArticleCountByKeywordInPastYears?keyword=${encodeURIComponent(this.keyword)}`)
        .then(response => response.text())
        .then(data => {
          console.log(data);
          this.closegetArticleCountByKeywordInPastYearsModal();
        })
        .catch(error => {
          console.error('Error:', error);
        });
      }
    },
    
    
    login() {
      const users = [
        { username: 'postgres', password: 'huarui66' },
        { username: 'user2', password: 'password2' },
      ];

      const user = users.find(
        (u) => u.username === this.username && u.password === this.password
      );

      if (user) {
        this.isLoggedIn = true;
        this.errorMessage = '';
      } else {
        this.errorMessage = 'Invalid username or password';
      }
    },
    logout() {
      this.username = '';
      this.password = '';
      this.isLoggedIn = false;
      this.errorMessage = '';
    },
  },
};
</script>

<style scoped>
.login {
  max-width: 600px;
  margin: auto;
  text-align: center;
  background-color: #e0f7fa;
  padding: 40px;
  border-radius: 10px;
}

.input-group {
  margin-bottom: 40px;
}

.password-label {
  margin-top: 40px;
  display: block;
}

.password-input {
  margin-top: 10px;
}

input {
  width: 100%;
  height: 40px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 20px;
  box-sizing: border-box;
}

.login-button {
  width: 60%;
  padding: 10px;
  margin-top: 30px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  text-align: center;
}

.login-button:hover {
  background-color: #45a049;
}

.message-container {
  height: 20px;
  margin-top: 50px;
}

.error {
  color: red;
}

.welcome-container {
  text-align: center;
  margin-top: 50px;
  max-width: 600px;
  margin: auto;
  padding: 40px;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  background-color: #e0f7fa;
}

.logout-button {
  width: auto;
  padding: 10px 20px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  text-align: center;
}

.logout-button:hover {
  background-color: #45a049;
}

h3 {
  cursor: pointer;
  color: #42b983;
}

p {
  margin-left: 20px;
}

.small-text {
  font-size: 0.9em;
}

.service-button {
  margin: 10px;
  margin-bottom: 10px;
}

.main-button {
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 10px;
  cursor: pointer;
  width: 100%;
  text-align: center;
}

.main-button:hover {
  background-color: #369f6b;
}

.detail-button {
  background-color: #e0f7fa;
  color: #42b983;
  border: 1px solid #42b983;
  border-radius: 5px;
  padding: 5px;
  cursor: pointer;
  width: 100%;
  text-align: center;
  margin-top: 5px;
}

.detail-button:hover {
  background-color: #cfe9e5;
}
.modal {
  display: block;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgb(0,0,0,0.4);
}
.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
}
.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}
.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}
</style>