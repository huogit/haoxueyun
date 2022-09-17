import request from '~/utils/request'

const URI = '/api/edu/teacher'

export default {
  getList() {
    return request({
      url: URI + '/list',
      method: 'get'
    })
  },
  getById(id) {
    return request({
      url: URI + `/get/${id}`,
      method: 'get'
    })
  }
}
