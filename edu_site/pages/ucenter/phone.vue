<template>
  <article class="col-7 fl">
    <div class="u-r-cont">
      <section>
        <div>
          <section class="c-infor-tabTitle c-tab-title">
            <a href="javascript: void(0)" title="更改手机号" class="current">
              更改手机号
            </a>
          </section>
        </div>
      </section>

      <el-form :model="phoneObj" label-width="100px" class="demo-dynamic">
        <el-form-item
          :rules="[
            { required: true, message: '请输入新的手机号', trigger: 'green' },
            { type: 'mobile', message: '请输入正确的手机号', trigger: ['green', 'change'] }
          ]"
          label="手机号">
          <el-input v-model="phoneObj.mobile" placeholder="请输入新的手机号"/>
        </el-form-item>

        <el-form-item
          :rules="[
            { required: true, message: '请输入验证码', trigger: 'green' },
          ]"
          label="验证码"
        >
          <el-input v-model="phoneObj.code" size="medium" style="width:200px" placeholder="请输入验证码" />
          <el-button size="medium" @click="getCodeFun()">{{ codeText }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="updateData()">立即修改</el-button>
        </el-form-item>
      </el-form>
    </div>
  </article>
</template>

<script>
import ucenterApi from '~/api/ucenter'

export default {
  data() {
    return {
      phoneObj: {
        mobile: '',
        code: ''
      },
      sending: false, // 是否发送验证码
      second: 60, // 倒计时间
      codeText: '获取验证码' // 控制字体显示
    }
  },
  methods: {
    // 获取验证码
    getCodeFun() {
      // this.sending 原为false
      // 点击后立即 使 this.sending = true,防止多次点击
      if (this.sending) {
        return
      }
      this.sending = true
      ucenterApi.getCode(this.phoneObj.mobile).then((response) => {
        // 开始倒计时
        this.timeDawn()
        this.$message.success(response.message)
      })
    },
    // 倒计时
    timeDawn() {
      const result = setInterval(() => {
        this.codeText = this.second
        this.second--
        if (this.second < 0) {
          // 倒计时结束，清除倒计时
          clearInterval(result)
          this.sending = false
          this.second = 60
          this.codeText = '获取验证码'
        }
      }, 1000)
    },

    // 修改数据
    update() {
      ucenterApi.updatePhone(this.phoneObj).then((response) => {
        this.$message.success(response.message)
      })
    },
    // 按钮事件
    updateData() {
      this.update()
    }
  }
}
</script>
