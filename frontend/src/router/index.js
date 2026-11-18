import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../views/Layout.vue'

const routes = [
  {
    path: '/',
    component: Layout,
    children: [
      { path: '', redirect: '/dashboard' },
      { path: 'dashboard', component: () => import('../views/Dashboard.vue') },
      { path: 'search', component: () => import('../views/Search.vue') },
      { path: 'article/:id', component: () => import('../views/ArticleDetail.vue') },
      { path: 'authors', component: () => import('../views/Authors.vue') },
      { path: 'gaps', component: () => import('../views/Gaps.vue') },
      { path: 'import', component: () => import('../views/ImportData.vue') }
    ]
  }
]

export default createRouter({
  history: createWebHistory(),
  routes
})
