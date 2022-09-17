import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie

// 这是因为permission.js中写了namespaced: true

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/auth-redirect'] // no redirect whitelist

// 在路由变化之前需要执行src下面的permission.js中的router.beforeEach(async(to, from, next) => {})方法吗
router.beforeEach(async(to, from, next) => {
  console.log('beforeEach')
  // start progress bar
  NProgress.start()

  // determine whether the user has logged in
  const hasToken = getToken()
  console.log('hasToken', hasToken)
  if (hasToken) {
    console.log('hasToken', hasToken)
    // 之前的路径 to.path，next：下一个路径
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done()
    } else {
      // determine whether the user has obtained his permission roles through getInfo
      // 这行代码是要判断当前用户是否具有角色，我们来看src》store》index.js中的getters：
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      console.log('getters', hasRoles)
      if (hasRoles) {
        next()
      } else {
        try {
          // get user info
          // note: roles must be a object array! such as: ['admin'] or ,['developer','editor']
          const { roles } = await store.dispatch('GetInfo')
          console.log('GetInfo', roles)
          // generate accessible routes map based on roles
          const accessRoutes = await store.dispatch('permission/generateRoutes', roles)
          console.log('generateRoutes', accessRoutes)
          // dynamically add accessible routes
          // 把创造的动态路由放入到已经有的路由中，这样动态路由和普通路由使用起来就没有区别了
          router.addRoutes(accessRoutes)

          next({ ...to, replace: true })
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('FedLogOut')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* has no token*/
    // next()
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
      // next()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
