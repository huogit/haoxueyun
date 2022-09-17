import request from '~/utils/request'

const URI = '/api/edu/subject'

export default {
  nestedList() {
    return request({
      url: URI + '/nested-list',
      method: 'get'
    })
  }
}
