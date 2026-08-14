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
      const role = credentials.role || 'USER';
      const permissionMap = {
        USER: ['search', 'analytics'],
        JOURNAL_ADMIN: ['search', 'analytics', 'journal:update'],
        ADMIN: ['search', 'analytics', 'journal:update', 'import', 'truncate', 'user:manage']
      };

      const user = {
        name: credentials.username || 'Demo User',
        role,
        username: credentials.username || 'demo',
        permissions: permissionMap[role] || permissionMap.USER,
        demoMode: true
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
    userRole: state => state.userRole,
    permissions: state => state.user?.permissions || []
  }
});
