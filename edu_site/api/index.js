import request from '~/utils/request'

// const BASE_URL = 'http://127.0.0.1:8140'

const URI = '/api/cms/ad'

export default{
  getTopBannerAdList() {
    return request({
      url: URI + '/list',
      method: 'get'
    })
  },
  getIndexData() {
    return request({
      url: '/api/edu/index',
      method: 'get'
    })
  }
}
