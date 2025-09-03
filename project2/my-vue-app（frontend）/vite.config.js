import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 8080, // 设置 Vue.js 应用使用的端口
    proxy: {
      '/api': {
        target: 'http://localhost:8081', // 代理到 Spring Boot 应用
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
