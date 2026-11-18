<template>
  <div>
    <h2>知识缺口</h2>
    <el-alert title="以下搜索没有返回任何结果，说明你的知识库还没有覆盖这些主题。" type="info" :closable="false" class="mb-3" />
    <el-table :data="history" size="small">
      <el-table-column prop="keyword" label="搜索词" />
      <el-table-column prop="executedAt" label="搜索时间" width="180" />
      <el-table-column prop="durationMs" label="耗时(ms)" width="100" />
      <el-table-column prop="resultCnt" label="结果数" width="80">
        <template #default="scope"><el-tag :type="scope.row.resultCnt === 0 ? 'danger' : 'success'" size="small">{{ scope.row.resultCnt }}</el-tag></template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import api from '../api'
const history = ref([])
onMounted(async () => { const r = await api.getSearchHistory(true); history.value = r.data.data || [] })
</script>

<style scoped>
.mb-3 { margin-bottom: 12px; }
</style>
