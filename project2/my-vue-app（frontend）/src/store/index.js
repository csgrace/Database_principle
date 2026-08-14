import { createStore } from 'vuex';

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
    loginUser({ commit }, credentials) {
      const role = credentials.role || 'admin';
      const permissions = role === 'admin'
        ? ['read', 'write', 'delete', 'admin', 'import', 'truncate']
        : ['read'];

      const user = {
        name: credentials.username || 'Demo User',
        role: role,
        username: credentials.username || 'demo',
        permissions: permissions
      };
      commit('setUser', user);
      commit('setUserRole', role);
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
