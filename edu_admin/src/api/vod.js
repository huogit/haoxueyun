import request from '@/utils/request'

// const URI = 'admin/vod/media'

export default {
  removeByVodId(id) {
    return request({
      baseURL: 'http://127.0.0.1:8130',
      url: `/admin/vod/media/remove/${id}`,
      method: 'delete'
    })
  }
}
