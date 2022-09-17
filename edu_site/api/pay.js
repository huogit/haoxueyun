import request from '~/utils/request'

// const BASEURL = 'http://localhost:8170'
const URI = '/api/trade/weixin-pay'

export default {
  createNative(orderNo) {
    return request({
      url: URI + `/create-native/${orderNo}`,
      method: 'get'
    })
  }
}
