/*
@ 符号在build/webpack.base.conf.js 中配置 表示 'src' 路径
 */

import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
**/

/**
 * children下的path不能带有“/”
 */
export const constantRoutes = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },
  {
    path: '/',
    component: Layout,
    hidden: true,
    redirect: '/dashboard',
    meta: { title: '在线教育后台', icon: 'dashboard' },
    name: 'Dashboard',
    children: [
      {
        path: '/dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: '首页', icon: 'dashboard' }
      },
      {
        path: '/test',
        name: 'Test',
        component: () => import('@/views/test/index'),
        meta: { title: '测试', icon: 'test' }
      }
    ]
  }
]
/**
 * 动态路由: 是从数据库取出来，在store里permission进行拼接，然后addRoute添加拼接好的路由
 */
export const asyncRoutes = [
// 讲师管理
  {
    path: '/teacher',
    component: Layout,
    redirect: '/teacher/list',
    name: 'teacher',
    meta: { title: '讲师管理', icon: 'example' },
    children: [
      {
        path: 'list',
        name: 'TeacherList',
        component: () => import('@/views/teacher/list'),
        meta: { title: '讲师列表', icon: 'table' }
      },
      {
        path: 'create',
        name: 'TeacherCreate',
        component: () => import('@/views/teacher/form'),
        meta: { title: '添加讲师', icon: 'tree' }
      },
      {
        path: 'edit/:id',
        name: 'TeacherEdit',
        component: () => import('@/views/teacher/form'),
        meta: { title: '编辑讲师', icon: 'form' },
        hidden: true // 隐藏该子栏目
      }
    ]
  },
  // 课程分类管理
  {
    path: '/subject',
    name: 'Subject',
    component: Layout,
    redirect: '/subject/list',
    meta: { title: '课程分类管理', icon: 'example' },
    children: [
      {
        path: 'list',
        name: 'SubjectList',
        component: () => import('@/views/subject/list'),
        meta: { title: '课程分类列表', icon: 'table' }
      },
      {
        path: 'import',
        name: 'SubjectImport',
        component: () => import('@/views/subject/import'),
        meta: { title: '导入课程分类', icon: 'tree' }
      }
    ]

  },
  // 课程管理
  {
    path: '/course',
    name: 'Course',
    component: Layout,
    redirect: '/course/list',
    meta: { title: '课程管理', icon: 'example' },
    children: [
      {
        path: 'list',
        name: 'CourseList',
        component: () => import('@/views/course/list'),
        meta: { title: '课程列表', icon: 'table' }
      },
      {
        path: 'info',
        name: 'CourseForm',
        component: () => import('@/views/course/form'),
        meta: { title: '发布课程', icon: 'tree' }
      },
      {
        path: 'info/:id', // id为路由参数
        name: 'CourseInfoEdit',
        component: () => import('@/views/course/form'),
        meta: { title: '编辑课程' },
        hidden: true
      },
      {
        path: 'chapter/:id',
        name: 'CourseChapterEdit',
        component: () => import('@/views/course/form'),
        meta: { title: '编辑课程大纲' },
        hidden: true
      }
    ]

  },
  {
    path: '/ad',
    component: Layout,
    redirect: '/ad/list',
    name: 'Ad',
    meta: { title: '内容管理', icon: 'example' },
    children: [
      {
        path: 'list',
        name: 'AdList',
        component: () => import('@/views/ad/list'),
        meta: { title: '广告推荐', icon: 'table' }
      },
      {
        path: 'create',
        name: 'AdCreate',
        component: () => import('@/views/ad/form'),
        meta: { title: '添加广告推荐', icon: 'tree' }
      },
      {
        path: 'edit/:id',
        name: 'AdEdit',
        component: () => import('@/views/ad/form'),
        meta: { title: '修改广告推荐' },
        hidden: true
      },

      // 广告推荐分类
      {
        path: 'type-list',
        name: 'AdTypeList',
        component: () => import('@/views/adType/list'),
        meta: { title: '推荐位', icon: 'table' }
      },
      {
        path: 'type-create',
        name: 'AdTypeCreate',
        component: () => import('@/views/adType/form'),
        meta: { title: '添加推荐位', icon: 'tree' }
      },
      {
        path: 'type-edit/:id',
        name: 'AdTypeEdit',
        component: () => import('@/views/adType/form'),
        meta: { title: '修改推荐位' },
        hidden: true
      }
    ]
  },
  // 统计管理
  {
    path: '/statistics',
    component: Layout, // 布局组件
    redirect: '/statistics/create',
    meta: { title: '统计分析', icon: 'example' },
    name: 'statistics',
    children: [
      {
        path: 'create',
        name: 'StatisticsCreate',
        component: () => import('@/views/statistics/create'),
        meta: { title: '生成统计' }
      },
      {
        path: 'chart',
        name: 'StatisticsChart',
        component: () => import('@/views/statistics/chart'),
        meta: { title: '统计图表' }
      }
    ]
  },
  {
    path: '/acl',
    redirect: '/acl/user/list',
    component: Layout, // 布局组件
    name: 'ACL',
    meta: { title: '权限管理', icon: 'example' },
    children: [
      // 用户管理
      {
        path: 'user/list', // '/user/list' 访问地址是 user/list ,'user/list' 访问地址是 acl/user/list
        name: 'UserList',
        component: () => import('@/views/acl/user/list'),
        meta: { title: '用户管理', icon: 'table' }
      },
      {
        path: 'user/create',
        name: 'UserForm',
        component: () => import('@/views/acl/user/form'),
        meta: { title: '用户添加' },
        hidden: true
      },
      {
        path: 'user/update/:id',
        name: 'UserUpdate',
        component: () => import('@/views/acl/user/form'),
        meta: { title: '用户修改' },
        hidden: true
      },
      {
        path: 'user/role/:id',
        name: 'UserRole',
        component: () => import('@/views/acl/user/userRole'),
        meta: { title: '用户角色' },
        hidden: true
      },
      // 菜单管理
      {
        path: 'menu/list',
        name: 'MenuList',
        component: () => import('@/views/acl/menu/list'),
        meta: { title: '菜单管理', icon: 'table' }
      },
      // 角色管理
      {
        path: 'role/list',
        name: 'RoleList',
        component: () => import('@/views/acl/role/list'),
        meta: { title: '角色管理', icon: 'table' }
      },
      {
        path: 'role/create',
        name: 'RoleCreate',
        component: () => import('@/views/acl/role/form'),
        meta: { title: '角色添加' },
        hidden: true
      },
      {
        path: 'role/edit:id',
        name: 'RoleUpdate',
        component: () => import('@/views/acl/role/form'),
        meta: { title: '角色修改' },
        hidden: true
      },
      {
        path: 'role/distribution/:id',
        name: '角色权限',
        component: () => import('@/views/acl/role/rolePermission'),
        meta: { title: '角色权限' },
        hidden: true
      }
    ]
  },
  // 消息管理
  {
    path: '/message',
    component: Layout,
    redirect: '/message/list',
    name: 'message',
    meta: { title: '消息管理', icon: 'example' },
    children: [
      {
        path: 'list',
        name: 'MessageList',
        component: () => import('@/views/message/list'),
        meta: { title: '消息列表', icon: 'table' }
      },
      {
        path: 'create',
        name: 'MessageCreate',
        component: () => import('@/views/message/form'),
        meta: { title: '添加消息', icon: 'tree' }
      },
      {
        path: 'edit/:id',
        name: 'MessageEdit',
        component: () => import('@/views/message/form'),
        meta: { title: '编辑消息', icon: 'form' },
        hidden: true // 隐藏该子栏目
      }
    ]
  }
]

const createRouter = () => new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// export default new Router({
//   // mode: 'history', //后端支持可开
//   scrollBehavior: () => ({ y: 0 }),
//   routes: constantRouterMap
// })

export default router
