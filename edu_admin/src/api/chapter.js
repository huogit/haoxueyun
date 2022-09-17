import request from '@/utils/request'

const URI = '/admin/edu/chapter'

export default {
  save(chapter) {
    return request({
      url: URI + '/save',
      method: 'post',
      data: chapter
    })
  },
  update(chapter) {
    return request({
      url: URI + '/update',
      method: 'post',
      data: chapter
    })
  },
  removeById(id) {
    return request({
      url: URI + `/delete/${id}`,
      method: 'delete'
    })
  },
  getById(id) {
    return request({
      url: URI + `/get/${id}`,
      method: 'get'
    })
  },
  nestedListByCourseId(courseId) {
    return request({
      url: URI + `/nested-list/${courseId}`,
      method: 'get'
    })
  }
}
