import { createApp } from 'vue'; // 导入 Vue 的 createApp 函数
import App from './App.vue'; // 导入根组件 App
import router from './router'; // 导入路由配置
import store from './store'; // 导入 Vuex store 配置

createApp(App).use(router).use(store).mount('#app'); // 创建 Vue 实例，使用路由和 Vuex，并挂载到 #app 元素