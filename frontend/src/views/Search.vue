<template>
  <div>
    <h2>知识检索</h2>
    <el-input v-model="keyword" placeholder="输入关键词…" @keyup.enter="doSearch" class="mb-3">
      <template #append><el-button @click="doSearch">搜索</el-button></template>
    </el-input>
    <el-tag v-if="result" closable @close="result = null" class="mb-3">
      耗时 {{ result.tookMs }}ms · {{ result.count }} 条结果
    </el-tag>
    <el-empty v-if="result && result.gap" description="没有找到相关内容——已记入知识缺口" />
    <div v-if="result" class="hit-list">
      <div v-for="h in result.hits" :key="h.articleId" class="hit-item" @click="$router.push(`/article/${h.articleId}`)">
        <div class="hit-title">{{ h.title }}</div>
        <div class="hit-meta">{{ h.authorName }} · 赞同 {{ h.voteupCount }} · {{ h.tags }}</div>
        <div class="hit-excerpt">{{ h.excerpt }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import api from '../api'
const keyword = ref('')
const result = ref(null)
async function doSearch() {
  const r = await api.search({ keyword: keyword.value, tagId: null })
  result.value = r.data.data
}
</script>

<style scoped>
.mb-3 { margin-bottom: 12px; }
.hit-list { margin-top: 16px; }
.hit-item { padding: 14px; border: 1px solid #eee; border-radius: 8px; margin-bottom: 10px; cursor: pointer; }
.hit-item:hover { border-color: #c8563a; }
.hit-title { font-size: 15px; font-weight: 500; }
.hit-meta { font-size: 12px; color: #888; margin: 4px 0; }
.hit-excerpt { font-size: 13px; color: #555; }
</style>
