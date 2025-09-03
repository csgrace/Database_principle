import { createStore } from 'vuex';
import axios from 'axios';

export default createStore({
  state: {
    user: null,
    userRole: null
  },
  mutations: {
    setUser(state, user) {
      state.user = user;
    },
    setUserRole(state, role) {
      state.userRole = role;
    },
    logout(state) {
      state.user = null;
      state.userRole = null;
    }
  },
  actions: {
    async loginUser({ commit }, credentials) {
      try {
        console.log('Login credentials:', credentials); // 添加调试信息
        const response = await axios.post('/api/login', credentials);
        console.log('API response:', response); // 添加调试信息
        const user = response.data.user;
        commit('setUser', user);
        commit('setUserRole', user.role);
      } catch (error) {
        console.error('API error:', error); // 添加调试信息
        throw new Error('Invalid username or password');
      }
    },
    logout({ commit }) {
      commit('logout');
    }
  },
  getters: {
    isAuthenticated: state => !!state.user,
    user: state => state.user,
    userRole: state => state.userRole
  }
});