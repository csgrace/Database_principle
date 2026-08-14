import { createRouter, createWebHashHistory } from 'vue-router';
import Login from '@/views/Login.vue';
import Dashboard from '@/views/Dashboard.vue';
import Logout from '@/views/Logout.vue';
import JournalManagement from '@/views/JournalManagement.vue';
import AdminOperations from '@/views/AdminOperations.vue';
import store from '@/store';

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/logout',
    name: 'Logout',
    component: Logout
  },
  {
    path: '/journals',
    name: 'JournalManagement',
    component: JournalManagement,
    meta: { requiresAuth: true, permissions: ['journal:update'] }
  },
  {
    path: '/admin',
    name: 'AdminOperations',
    component: AdminOperations,
    meta: { requiresAuth: true, roles: ['ADMIN'] }
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

router.beforeEach((to) => {
  if (to.meta.requiresAuth && !store.getters.isAuthenticated) {
    return { name: 'Login' };
  }

  const requiredRoles = to.meta.roles;
  if (requiredRoles && !requiredRoles.includes(store.getters.userRole)) {
    return { name: 'Dashboard' };
  }

  const requiredPermissions = to.meta.permissions;
  if (requiredPermissions && !requiredPermissions.every(permission => store.getters.permissions.includes(permission))) {
    return { name: 'Dashboard' };
  }

  return true;
});

export default router;
