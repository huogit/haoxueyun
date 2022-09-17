<template>
  <article class="col-7 fl">
    <div class="u-r-cont">
      <section>
        <div>
          <section class="c-infor-tabTitle c-tab-title">
            <a href="javascript: void(0)" title="更改密码" class="current">
              更改密码
            </a>
          </section>
        </div>
      </section>

      <el-form ref="passwordObj" :model="passwordObj" :rules="rules" status-icon label-width="100px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordObj.oldPassword" type="password" placeholder="请输入原密码" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordObj.newPassword" type="password" placeholder="请输入新密码" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="确认密码" prop="newPassword">
          <el-input v-model="passwordObj.checkPass" type="password" placeholder="请确认新密码" autocomplete="off"/>
        </el-form-item>

        <el-form-item>
          <el-button type="success" @click="submitForm('passwordObj')">立即更改</el-button>
        </el-form-item>
      </el-form>
    </div>
  </article>
</template>

<script>
import ucenterApi from '~/api/ucenter'
import cookie from 'js-cookie'

export default {
  data() {
    // 定义校验规则
    var validatePass = (rules, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        callback()
      }
    }
    // 校验确认密码
    var validatePass2 = (rules, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码'))
      } else {
        if (value !== this.passwordObj.newPassword) {
          callback(new Error('两次输入密码不一致!'))
        } else {
          callback()
        }
      }
    }
    return {
      passwordObj: {
        oldPassword: '',
        newPassword: '',
        checkPass: ''
      },
      // 进行校验
      rules: {
        oldPasswordword: [
          { validator: validatePass, trigger: 'green' }
        ],
        newPassword: [
          { validator: validatePass, trigger: 'green' }
        ],
        checkPass: [
          { validator: validatePass2, trigger: 'green' }
        ]
      }
    }
  },
  methods: {
    submitForm(formName) {
      var result = true
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          result = false
        }
      })
      if (!result) {
        return
      }
      // console.log('11')
      ucenterApi.updatePassword(this.passwordObj).then((response) => {
        this.$message.success('修改成功，请重新登录')
        cookie.set('edu_jwt_token', '', { 'domain': 'localhost' })
        // 跳转页面
        window.location.href = '/login'
      })
    }
  }
}
</script>
