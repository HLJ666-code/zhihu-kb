<template>
  <div>
    <h2>博主分析</h2>
    <el-card>
      <template #header>博主排名（按总赞同）</template>
      <el-table :data="ranks" size="small">
        <el-table-column prop="voteupRank" label="#" width="60" />
        <el-table-column prop="name" label="博主" />
        <el-table-column prop="articleCnt" label="文章数" width="80" />
        <el-table-column prop="totalVoteup" label="总赞同" width="100" />
        <el-table-column prop="avgVoteup" label="平均赞同" width="100" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click="toggleFollow(scope.row)">{{ followed[scope.row.authorId] ? '取关' : '关注' }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import api from '../api'
const ranks = ref([])
const followed = ref({})
async function load() {
  const r = await api.getAuthors()
  ranks.value = r.data.data || []
  const f = await api.getFollows()
  const map = {}
  ;(f.data.data || []).forEach(a => { if (!a.unfollowAt) map[a.authorId] = true })
  followed.value = map
}
async function toggleFollow(row) {
  if (followed.value[row.authorId]) { await api.unfollow(row.authorId) } else { await api.follow(row.authorId) }
  await load()
}
onMounted(load)
</script>
