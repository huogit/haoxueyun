import request from '@/utils/request'

const URI = 'admin/edu/video'

export default {
  save(video) {
    return request({
      url: URI + '/save',
      method: 'post',
      data: video
    })
  },
  getById(id) {
    return request({
      url: URI + `/get/${id}`,
      method: 'get'
    })
  },
  update(video) {
    return request({
      url: URI + '/update',
      method: 'put',
      data: video
    })
  },
  removeById(id) {
    return request({
      url: URI + `/remove/${id}`,
      method: 'delete'
    })
  }
}
