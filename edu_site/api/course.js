import request from '~/utils/request'

const URI = '/api/edu/course'

export default {
  list(queryObj) {
    return request({
      url: URI + '/list',
      method: 'get',
      params: queryObj // data 适用于 post put等请求，不适用于 get请求，data 用responseBody接收，param 不需要注解
    })
  },
  getById(id) {
    return request({
      url: URI + `/get/${id}`,
      method: 'get'
    })
  },
  getPlayAuth(videoSourceId) {
    return request({
      url: `/api/vod/media/get-play-auth/${videoSourceId}`,
      method: 'get'
    })
  },
  // 搜索课程
  searchCourse(keyWord) {
    return request({
      url: URI + `/search/${keyWord}`,
      method: 'get'
    })
  }
}
