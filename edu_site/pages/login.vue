<template>
  <div class="main">
    <div class="title">
      <a class="active" href="/login">登录</a>
      <span>·</span>
      <a href="/register">注册</a>
    </div>

    <div class="sign-up-container">
      <form action="register">
        <div class="input-prepend restyle">
          <input
            v-model="user.mobile"
            type="text"
            placeholder="手机号">
          <i class="iconfont icon-phone"/>
        </div>
        <div class="input-prepend">
          <input
            v-model="user.password"
            type="password"
            placeholder="密码">
          <i class="iconfont icon-password"/>
        </div>
        <div class="btn">
          <input
            type="button"
            class="sign-in-button"
            value="登录"
            @click="submitLogin()">
        </div>
      </form>
      <!-- 更多登录方式 -->
      <div class="more-sign">
        <h6>社交帐号登录</h6>
        <ul>
          <li><a id="weixin" class="weixin" href="#" @click="unOpen()"><i class="iconfont icon-weixin"/></a></li>
          <li><a id="qq" class="qq" href="#" @click="unOpen()"><i class="iconfont icon-qq"/></a></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import '~/assets/css/sign.css'
import '~/assets/css/iconfont.css'

import loginApi from '~/api/login'
import cookie from 'js-cookie'
export default {
  layout: 'sign',

  data() {
    return {
      user: {
        mobile: '',
        password: ''
      }
    }
  },

  methods: {
    // 登录
    submitLogin() {
      // console.log('login')
      // 执行登录
      loginApi.submitLogin(this.user).then((response) => {
        // 登录成功后将jwtToken写入cookie
        cookie.set('edu_jwt_token', response.data.jwtToken, { domain: 'localhost' })
        // 跳转到首页
        if (document.referrer.indexOf('register' !== -1)) {
          window.location.href = '/'
        } else {
          // 登录后回到原来的页面
          history.go(-1)
        }
        // window.location.href = '/'
      })
    },
    // 暂未开放弹框
    unOpen() {
      window.alert('暂未开放')
    }
  }
}
</script>
