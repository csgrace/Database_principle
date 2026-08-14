import { createStore } from 'vuex';

export default createStore({
  state: {
    user: {
      name: 'Demo User',
      role: 'admin',
      username: 'demo',
      password: 'demo',
      permissions: ['read', 'write', 'admin']
    },
    userRole: 'admin',
    isDemoMode: true
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
    loginUser({ commit }, credentials) {
      // Demo mode: accept any credentials
      const user = {
        name: credentials.username || 'Demo User',
        role: 'admin',
        username: credentials.username || 'demo',
        permissions: ['read', 'write', 'admin']
      };
      commit('setUser', user);
      commit('setUserRole', user.role);
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
