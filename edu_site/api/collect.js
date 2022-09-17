import request from '~/utils/request'

const URI = '/api/edu/course-collect'

export default{
  saveData(courseId) {
    return request({
      url: URI + `/auth/save/${courseId}`,
      method: 'post'
    })
  },
  getCollectList() {
    return request({
      url: URI + '/auth/list',
      method: 'get'
    })
  },
  isCollect(courseId) {
    return request({
      url: URI + `/auth/is-collect/${courseId}`,
      method: 'get'
    })
  },
  removeById(courseId) {
    return request({
      url: URI + `/auth/remove/${courseId}`,
      method: 'delete'
    })
  }
}
