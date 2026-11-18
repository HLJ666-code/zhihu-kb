<template>
  <div class="shell">
    <aside class="side">
      <div class="brand">
        <div class="brand-mark">知</div>
        <div class="brand-text">
          <div class="brand-name">知乎知识库</div>
          <div class="brand-sub">个人知识时序库</div>
        </div>
      </div>
      <nav class="nav">
        <div class="nav-group" v-for="g in groups" :key="g.title">
          <div class="nav-label">{{ g.title }}</div>
          <router-link v-for="item in g.items" :key="item.path" :to="item.path" class="nav-item" :class="{ active: isActive(item.path) }">
            <span>{{ item.label }}</span>
          </router-link>
        </div>
      </nav>
      <div class="side-foot">
        <span class="dot" :class="{ off: !backendOk }"></span>
        {{ backendOk ? '数据库已连接' : '数据库未连接' }}
      </div>
    </aside>
    <main class="main"><router-view /></main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
const route = useRoute()
const backendOk = ref(true)
const groups = [
  { title: '浏览', items: [
    { path: '/dashboard', label: '概览' },
    { path: '/search', label: '知识检索' },
    { path: '/authors', label: '博主分析' },
    { path: '/gaps', label: '知识缺口' }
  ]},
  { title: '维护', items: [{ path: '/import', label: '数据导入' }]}
]
function isActive(p) { return p === '/search' ? route.path.startsWith('/search') || route.path.startsWith('/article') : route.path === p }
onMounted(async () => {
  try { const r = await fetch('/api/stats/overview'); backendOk.value = r.ok } catch { backendOk.value = false }
})
</script>

<style scoped>
.shell { display: flex; height: 100%; }
.side { width: 220px; background: #1b1916; color: #f4f1ea; padding: 20px; display: flex; flex-direction: column; }
.brand { display: flex; gap: 10px; align-items: center; margin-bottom: 24px; }
.brand-mark { width: 36px; height: 36px; border-radius: 8px; background: rgba(255,255,255,0.1); display: flex; align-items: center; justify-content: center; }
.nav { flex: 1; }
.nav-group { margin-bottom: 20px; }
.nav-label { font-size: 11px; color: rgba(244,241,234,0.4); margin-bottom: 8px; }
.nav-item { display: block; padding: 8px 10px; border-radius: 6px; color: rgba(244,241,234,0.7); text-decoration: none; margin-bottom: 2px; }
.nav-item:hover { background: rgba(255,255,255,0.06); color: #fff; }
.nav-item.active { background: rgba(255,255,255,0.1); color: #fff; font-weight: 500; }
.side-foot { font-size: 12px; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 12px; }
.dot { display: inline-block; width: 7px; height: 7px; border-radius: 50%; background: #4c9a72; margin-right: 6px; }
.dot.off { background: #c8563a; }
.main { flex: 1; padding: 24px; overflow-y: auto; }
</style>
