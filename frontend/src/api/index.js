import axios from 'axios'

const api = axios.create({ baseURL: '/api', timeout: 10000 })

export default {
  search(params) { return api.get('/articles', { params }) },
  getArticle(id) { return api.get(`/articles/${id}`) },
  getVersions(id) { return api.get(`/articles/${id}/versions`) },
  getVersion(id, v) { return api.get(`/articles/${id}/versions/${v}`) },
  diff(id, from, to) { return api.get(`/articles/${id}/diff`, { params: { from, to } }) },
  getTags() { return api.get('/tags/tree') },
  getAuthors() { return api.get('/authors/rank') },
  getFollows() { return api.get('/authors/follows') },
  follow(id) { return api.post(`/authors/follows/${id}`) },
  unfollow(id) { return api.delete(`/authors/follows/${id}`) },
  getNotes() { return api.get('/notes') },
  addNote(data) { return api.post('/notes', data) },
  getUnread() { return api.get('/unread') },
  getSearchHistory(onlyGap) { return api.get('/search-history', { params: { onlyGap } }) },
  getOverview() { return api.get('/stats/overview') },
  getBatches() { return api.get('/stats/batches') },
  importData(data) { return api.post('/import', data) }
}
