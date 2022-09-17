import request from '@/utils/request'

const URI = '/admin/edu/course'

// get请求会把data数据放在 ?后面

export default {
  saveCourseInfo(courseInfo) {
    return request({
      url: URI + '/save-course-info',
      method: 'post',
      data: courseInfo
    })
  },
  getCourseInfoById(id) {
    return request({
      url: URI + `/course-info/${id}`,
      method: 'get'
    })
  },
  updateCourseInfoById(courseInfo) {
    return request({
      url: URI + `/update-course-info`,
      method: 'put',
      data: courseInfo
    })
  },
  getPageList(page, limit, searchObj) {
    return request({
      url: URI + `/list/${page}/${limit}`,
      method: 'get',
      params: searchObj // get请求下的data数据会放在 ？后面
    })
  },
  removeById(id) {
    return request({
      url: URI + `/remove/${id}`,
      method: 'delete'
    })
  },
  coursePublishInfoById(id) {
    return request({
      url: URI + `/course-publish-info/${id}`,
      method: 'get'
    })
  },
  publishCourseById(id) {
    return request({
      url: URI + `/publish-course/${id}`,
      method: 'post'
    })
  }
}
