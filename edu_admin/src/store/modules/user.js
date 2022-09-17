import { login, logout, getInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  // 用于存数据 和 取数据，相当于 repository
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    buttons: [],
    roles: []
  },

  // 提供方法给 action 调用，将数据存入state ，相当于service
  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_BUTTONS: (state, buttons) => {
      state.buttons = buttons
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    }
  },

  // 给外部调用 ，以达成将数据存入 state，相当于 controller
  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        login(username, userInfo.password).then(response => {
          // debugger
          const data = response.data
          setToken(data.token)
          commit('SET_TOKEN', data.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    // Login({ commit }) {
    //   debugger
    //   const data = {
    //     'token': 'helen'
    //   }
    //   setToken(data.token)// 将token存储在cookie中
    //   commit('SET_TOKEN', data.token)
    // },

    // 获取用户信息
    async GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        console.log('token', state.token)
        getInfo(state.token).then(response => {
          // debugger
          // response会返回name、avatar、roles、permissionValueList
          const data = response.data
          console.log('data', response.data)
          // 验证返回的roles是否是一个非空数组
          if (data.roles && data.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            // 把用户的所有角色存在state中的roles属性中，直接看mutations中的SET_ROLES()方法
            commit('SET_ROLES', data.roles)
          } else {
            // 如果没有角色会报错
            reject('getInfo: roles must be a non-null array !')
          }

          // 把所有权限值都放在buttonAuthList数组中
          const buttonAuthList = []
          data.permissionValueList.forEach(button => {
            buttonAuthList.push(button)
          })

          // 把用户名称、头像、权限数组都放在state中，
          commit('SET_NAME', data.name)
          commit('SET_AVATAR', data.avatar)
          commit('SET_BUTTONS', buttonAuthList)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // GetInfo({ commit }) {
    //   debugger
    //   const data = {
    //     'roles': [
    //       'admin'
    //     ],
    //     'name': 'helen',
    //     'avatar': 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-5670helen3b4acafe.gif'
    //   }
    //   if (data.roles && data.roles.length > 0) { // 验证返回的roles是否是一个非空数组
    //     commit('SET_ROLES', data.roles)
    //   }
    //   commit('SET_NAME', data.name)
    //   commit('SET_AVATAR', data.avatar)
    // },

    // 登出
    // LogOut({ commit, state }) {
    //   return new Promise((resolve, reject) => {
    //     logout(state.token).then(() => {
    //       commit('SET_TOKEN', '')
    //       commit('SET_ROLES', [])
    //       removeToken()
    //       resolve()
    //     }).catch(error => {
    //       reject(error)
    //     })
    //   })
    // },
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          debugger
          commit('SET_TOKEN', '')// 清空前端vuex中存储的数据
          commit('SET_ROLES', [])// 清空前端vuex中存储的数据
          commit('SET_BUTTONS', [])
          removeToken()// 清空cookie
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        debugger
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
