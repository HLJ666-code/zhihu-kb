<template>
  <div v-if="article">
    <h2>{{ article.title }}</h2>
    <div class="meta">{{ article.authorName }} · 发布 {{ article.publishTime }} · 赞同 {{ article.voteupCount }}</div>
    <el-tabs v-model="tab">
      <el-tab-pane label="正文" name="content">
        <div class="content">{{ article.content }}</div>
      </el-tab-pane>
      <el-tab-pane :label="`版本历史 (${versions.length})`" name="versions">
        <el-table :data="versions" size="small">
          <el-table-column prop="versionNo" label="版本" width="60" />
          <el-table-column prop="capturedAt" label="采集时间" />
          <el-table-column prop="voteupCount" label="赞同" width="80" />
          <el-table-column prop="voteupDelta" label="增量" width="80" />
          <el-table-column label="操作">
            <template #default="scope"><el-button size="small" @click="viewVersion(scope.row.versionNo)">查看</el-button></template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="我的批注" name="notes">
        <el-input v-model="noteText" type="textarea" :rows="3" placeholder="写批注…" />
        <el-button @click="addNote" class="mt-2">添加批注</el-button>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import api from '../api'
const route = useRoute()
const article = ref(null)
const versions = ref([])
const tab = ref('content')
const noteText = ref('')
async function load() {
  const id = route.params.id
  const [a, v] = await Promise.all([api.getArticle(id), api.getVersions(id)])
  article.value = a.data.data
  versions.value = v.data.data || []
}
async function viewVersion(v) {
  const r = await api.getVersion(route.params.id, v)
  article.value.content = r.data.data.content
}
async function addNote() {
  await api.addNote({ articleId: route.params.id, noteText: noteText.value })
  noteText.value = ''
}
onMounted(load)
watch(() => route.params.id, load)
</script>

<style scoped>
.meta { color: #888; font-size: 13px; margin: 8px 0 16px; }
.content { line-height: 1.8; white-space: pre-wrap; padding: 12px 0; }
.mt-2 { margin-top: 8px; }
</style>
