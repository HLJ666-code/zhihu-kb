<template>
  <div>
    <h2>概览</h2>
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="4" v-for="k in kpis" :key="k.label">
        <el-card><div class="kpi-num">{{ overview[k.key] ?? 0 }}</div><div class="kpi-label">{{ k.label }}</div></el-card>
      </el-col>
    </el-row>
    <el-card class="mt-4">
      <template #header>最近采集批次</template>
      <el-table :data="batches" size="small">
        <el-table-column prop="batchId" label="#" width="60" />
        <el-table-column prop="sourceNote" label="来源" />
        <el-table-column prop="status" label="状态" width="90" />
        <el-table-column prop="insertedCnt" label="入库" width="80" />
        <el-table-column prop="startedAt" label="开始时间" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import api from '../api'
const overview = ref({})
const batches = ref([])
const kpis = [
  { key: 'articleCount', label: '文章数' },
  { key: 'snapshotCount', label: '快照数' },
  { key: 'authorCount', label: '博主数' },
  { key: 'tableCount', label: '基表数' },
  { key: 'viewCount', label: '视图数' },
  { key: 'triggerCount', label: '触发器数' }
]
onMounted(async () => {
  const [o, b] = await Promise.all([api.getOverview(), api.getBatches()])
  overview.value = o.data.data || {}
  batches.value = b.data.data || []
})
</script>

<style scoped>
.kpi-row { margin-bottom: 16px; }
.kpi-num { font-size: 28px; font-weight: 600; }
.kpi-label { font-size: 12px; color: #888; margin-top: 4px; }
.mt-4 { margin-top: 16px; }
</style>
