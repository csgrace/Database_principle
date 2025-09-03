<template>
  <div class="login-container" v-if="!isLoggedIn">
    <h1>Login</h1> <!-- 登录标题 -->
    <form @submit.prevent="login">
      <div class="input-group">
        <input v-model="username" placeholder="Username" /> <!-- 用户名输入框 -->
      </div>
      <div class="input-group">
        <input v-model="password" type="password" placeholder="Password" /> <!-- 密码输入框 -->
      </div>
      <button type="submit">Login</button> <!-- 登录按钮 -->
      <div class="message-container">
        <div v-if="errorMessage" class="error">{{ errorMessage }}</div> <!-- 错误信息 -->
        <div v-if="successMessage" class="success">{{ successMessage }}</div> <!-- 成功信息 -->
      </div>
    </form>
  </div>
  <div v-else>
    <button @click="logout">Logout</button> <!-- 注销按钮 -->
  </div>
</template>

<script>
import { mapActions } from 'vuex'; // 导入 Vuex 的 mapActions

export default {
  data() {
    return {
      username: '', // 用户名
      password: '', // 密码
      errorMessage: '', // 错误信息
      successMessage: '', // 成功信息
      isLoggedIn: false // 登录状态
    };
  },
  methods: {
    ...mapActions(['loginUser', 'logout']), // 映射 Vuex 的 loginUser 和 logout 动作
    async login() {
      try {
        await this.loginUser({ username: this.username, password: this.password }); // 调用 loginUser 动作
        this.successMessage = 'Valid user!'; // 设置成功信息
        this.isLoggedIn = true; // 设置登录状态
      } catch (error) {
        console.error('Login error:', error); // 添加调试信息
        this.errorMessage = 'Invalid username or password'; // 设置错误信息
        this.username = ''; // 清空用户名输入框
        this.password = ''; // 清空密码输入框
      }
    },
    logout() {
      this.logout(); // 调用 logout 动作
      this.isLoggedIn = false; // 重置登录状态
      this.username = ''; // 清空用户名输入框
      this.password = ''; // 清空密码输入框
      this.errorMessage = ''; // 清空错误信息
      this.successMessage = ''; // 清空成功信息
    }
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  max-width: 400px;
  margin: auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h1 {
  margin-bottom: 20px;
}

.input-group {
  margin-bottom: 15px;
  width: 100%;
}

input {
  width: 100%;
  height: 40px; /* 设置输入框固定高度 */
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box; /* 确保一致的尺寸 */
}

button {
  width: 100%;
  padding: 10px;
  background-color: #42b983;
  color: rgb(225, 217, 217);
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #369f6b;
}

.message-container {
  height: 20px; /* 设置消息容器固定高度 */
  margin-top: 10px;
}

.error {
  color: red;
}

.success {
  color: green;
}
</style>