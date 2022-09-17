import request from '~/utils/request'

// const BASEURL = 'http://localhost:8170'
const URI = '/api/trade/order'

export default {
  saveData(courseId) {
    return request({
      url: URI + `/auth/save/${courseId}`,
      method: 'post'
    })
  },
  getById(orderId) {
    return request({
      url: URI + `/auth/get/${orderId}`,
      method: 'get'
    })
  },
  isBuy(courseId) {
    return request({
      url: URI + `/auth/is-buy/${courseId}`,
      method: 'get'
    })
  },
  removeById(orderId) {
    return request({
      url: URI + `/auth/remove/${orderId}`,
      method: 'delete'
    })
  },
  getList() {
    return request({
      url: URI + `/auth/list`,
      method: 'get'
    })
  },
  queryPayStatus(orderNo) {
    return request({
      url: URI + `/query-pay-status/${orderNo}`,
      method: 'get'
    })
  }
}
