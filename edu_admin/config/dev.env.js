// 定义全局常量值
// 在项目中的任意位置可以直接使用 process.env.BASE_API 常量表示后端接口的主机地址
'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  // 特殊常量,全局常量 用 process.env.BASE_API 可取到 BASE_API 的值
  NODE_ENV: '"development"',
  BASE_API: '"http://127.0.0.1:9110"',
  OSS_PATH: '"https://edu-file-2022-5-1.oss-cn-beijing.aliyuncs.com"'
})
