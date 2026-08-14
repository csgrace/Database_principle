<template>
  <div class="permissions-panel">
    <h3>🔐 Your Permissions</h3>
    <div class="permissions-list">
      <div class="perm-item" v-for="perm in userPermissions" :key="perm">
        <span class="perm-check">✓</span>
        <span class="perm-name">{{ perm }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'PermissionControl',
  computed: {
    ...mapGetters(['user']),
    userPermissions() {
      return this.user?.permissions || [];
    }
  }
};
</script>

<style scoped>
.permissions-panel {
  background: #f0fdf4;
  border: 1px solid #bbf7d0;
  border-radius: 8px;
  padding: 1rem;
  margin-bottom: 1.5rem;
}

.permissions-panel h3 {
  font-size: 0.95rem;
  color: #166534;
  margin-bottom: 0.75rem;
}

.permissions-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.perm-item {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  background: white;
  padding: 0.35rem 0.75rem;
  border-radius: 50px;
  border: 1px solid #bbf7d0;
}

.perm-check {
  color: #16a34a;
  font-weight: 700;
}

.perm-name {
  font-size: 0.8rem;
  color: #166534;
  text-transform: capitalize;
}
</style>
