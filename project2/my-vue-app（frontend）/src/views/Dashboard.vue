<template>
  <div>
    <h1>Dashboard</h1> <!-- 仪表盘标题 -->
    <div v-if="user">
      <p>Welcome, {{ user.name }}!</p> <!-- 欢迎用户 -->
      <p>Your role: {{ user.role }}</p> <!-- 显示用户角色 -->
      <PermissionControl :permissions="user.permissions" /> <!-- 权限控制组件 -->
      <!-- Additional dashboard content based on user permissions can be added here -->
      <div v-if="user.username === 'postgres' && user.password === 'huarui66'">
        <div class="service-button">
          <button @click="services.ArticleService = !services.ArticleService">ArticleService</button>
          <div v-if="services.ArticleService">
            <p>getArticletArticleCitationsByYear</p>
            <p>addArticleAndUpdateIF</p>
          </div>
        </div>
        <div class="service-button">
          <button @click="services.AuthorService = !services.AuthorService">AuthorService</button>
          <div v-if="services.AuthorService">
            <p>getArticlesByAuthorSortedByCitations</p>
            <p>getJournalWithMostArticlesByAuthor</p>
            <p>getMinArticlesToLinkAuthors</p>
          </div>
        </div>
        <div class="service-button">
          <button @click="services.DatabaseService = !services.DatabaseService">DatabaseService</button>
          <div v-if="services.DatabaseService">
            <p>getGroupMembers</p>
            <p>importData</p>
            <p>truncate</p>
            <p>sum</p>
          </div>
        </div>
        <div class="service-button">
          <button @click="services.GrantService = !services.GrantService">GrantService</button>
          <div v-if="services.GrantService">
            <p>getCountryFundPapers</p>
          </div>
        </div>
        <div class="service-button">
          <button @click="services.JournalService = !services.JournalService">JournalService</button>
          <div v-if="services.JournalService">
            <p>getImpactFactor</p>
            <p>updateJournalName</p>
          </div>
        </div>
        <div class="service-button">
          <button @click="services.KeywordService = !services.KeywordService">KeywordService</button>
          <div v-if="services.KeywordService">
            <p>getArticleCountByKeywordInPastYears</p>
          </div>
        </div>
      </div>
      <button @click="logout">Logout</button> <!-- 注销按钮 -->
    </div>
    <div v-else>
      <p>Please log in to access the dashboard.</p> <!-- 提示用户登录 -->
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'; // 导入 Vuex 的 mapState 和 mapActions
import PermissionControl from '../components/PermissionControl.vue'; // 导入权限控制组件

export default {
  components: {
    PermissionControl, // 注册权限控制组件
  },
  data() {
    return {
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
  computed: {
    ...mapState(['user']), // 映射 Vuex 的 user 状态
  },
  methods: {
    ...mapActions(['logout']), // 映射 Vuex 的 logout 动作
    logout() {
      this.logout(); // 调用 logout 动作
      this.$router.push({ name: 'Login' }); // 跳转到登录页面
    },
    toggleService(service) {
      this.$set(this.services, service, !this.services[service]);
    },
  },
};
</script>

<style scoped>
h1 {
  color: #42b983; /*标题颜色*/
}

button {
  cursor: pointer;
  color: #42b983;
}

p {
  margin-left: 20px;
}
</style>