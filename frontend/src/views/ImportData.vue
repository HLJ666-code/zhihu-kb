<template>
  <div>
    <h2>数据导入</h2>
    <el-upload :auto-upload="false" :on-change="onFile" accept=".json">
      <el-button>选择 JSON 文件</el-button>
    </el-upload>
    <div v-if="fileName" class="mt-3">{{ fileName }}</div>
    <el-button type="primary" @click="doImport" :disabled="!items.length" class="mt-3">导入 {{ items.length }} 条</el-button>
    <el-result v-if="result" :icon="result.failed === 0 ? 'success' : 'warning'" :title="`入库 ${result.inserted} / 跳过 ${result.skipped}`" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import api from '../api'
const items = ref([])
const fileName = ref('')
const result = ref(null)
function onFile(e) {
  const reader = new FileReader()
  reader.onload = () => { items.value = JSON.parse(reader.target.result); fileName.value = e.name }
  reader.readAsText(e.raw)
}
async function doImport() {
  const r = await api.importData({ items: items.value, sourceNote: '手动导入' })
  result.value = r.data.data
}
</script>

<style scoped>
.mt-3 { margin-top: 12px; }
</style>
