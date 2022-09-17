
// 首先，从index.js中引入已经定义过的2个router数组
import { constantRoutes } from '@/router'
import { getMenu } from '@/api/login'
import Layout from '@/views/layout/Layout'

function filterAsyncRouter(asyncRouterMap) { // 遍历后台传来的路由字符串，转换为组件对象
  try {
    const accessedRouters = asyncRouterMap.filter(route => {
      if (route.component) {
        if (route.component === 'Layout') { // Layout组件特殊处理
          route.component = Layout
        } else {
          const component = route.component
          route.component = resolve => {
            require(['@/views' + component + '.vue'], resolve)
          }
          // route.meta = { title: route.name, icon: 'example' }
        }
      }
      if (route.children && route.children.length) {
        route.children = filterAsyncRouter(route.children)
      }
      return true
    })
    return accessedRouters
  } catch (e) {
    console.log(e)
  }
}

// 全局变量state，routes和addRoutes数组
const state = {
  routes: [],
  addRoutes: []
}
//
// mutations 是唯一可以更改state的函数，使用SET_ROUTES定义更改方法，SET_ROUTES(state, routes)的入参routes赋值给addRoutes数组，
// 将constantRoutes静态路由数组增加routes；
const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
    console.log('state.routes', state.routes)
  }
}
// vue中store状态管理，通过actions调用 mutations 中封装的方法来实现对于state更改

// 这里是vue-element-admin中动态路由的主要判断逻辑发生地方，首先判定用户角色是否包含admin（可能多角色），
// 是则将所有asyncRoutes 加入到constantRoutes，若用户角色没有包含admin，则调用filterAsyncRoutes函数，
// 递归地判定asyncRoutes.roles属性是否有该角色，即是否有权限，将有权限的router赋值accessedRoutes 后加入到constantRoutes；
const actions = {
  async generateRoutes({ commit }, roles) {
    // 取后台路由
    const asyncRouter = await getMenu()
    return new Promise(resolve => {
    // permissionList中放着所有的一级父路由，一级父路由中有children，children中放着二级菜单和三级按钮变化的子路由，二级菜单和三级按钮变化的子路由中都没有children
      const tmp = asyncRouter.data.permissionList
      // 对一级父路由列表进行处理，生成动态路由

      const accessedRoutes = filterAsyncRouter(tmp)
      // 拼接动态路由和静态路由，搜索SET_ROUTES就可以看到上面的方法，它是把动态路由和静态路由拼在一起放在state中的routes里面
      commit('SET_ROUTES', accessedRoutes)
      console.log('accessedRoutes', accessedRoutes)
      // 把动态路由返回
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
