import request from '@/utils/request'

const URI = '/admin/cms/ad'

export default {
  list() {
    return request({

      url: URI + '/list',
      method: 'get'
    })
  },
  pageList(page, limit) {
    return request({

      url: URI + `/list/${page}/${limit}`,
      method: 'get'
    })
  },
  getById(id) {
    return request({

      url: URI + `/get/${id}`,
      method: 'get'
    })
  },
  save(AdObj) {
    return request({

      url: URI + '/save',
      method: 'post',
      data: AdObj
    })
  },
  updatedById(AdObj) {
    return request({

      url: URI + '/update',
      method: 'put',
      data: AdObj
    })
  },
  removeById(id) {
    return request({

      url: URI + `/remove/${id}`,
      method: 'delete'

    })
  }
}
