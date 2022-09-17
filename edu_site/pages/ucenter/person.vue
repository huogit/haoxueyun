<template>
  <article class="col-7 fl">
    <div class="u-r-cont">
      <section>
        <div>
          <section class="c-infor-tabTitle c-tab-title">
            <a href="javascript: void(0)" title="个人资料" class="current">
              基本资料
            </a>
          </section>
        </div>
      </section>

      <el-form ref="form" :model="member" label-width="80px">
        <el-form-item label="头像：" style="text-align: center" class="changeImg">
          <el-upload
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            class="avatar-uploader"
            action="http://localhost:9110/admin/oss/file/upload?module=member_avatar">
            <img v-if="member.avatar" :src="member.avatar" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"/>
          </el-upload>
        </el-form-item>
        <el-form-item label="昵称：">
          <el-input v-model="member.nickname" placeholder="请输入昵称"/>
        </el-form-item>

        <el-form-item label="性别：">
          <el-select v-model="member.sex" placeholder="请选择性别">
            <el-option :value="1" label="男"/>
            <el-option :value="2" label="女"/>
          </el-select>
        </el-form-item>

        <el-form-item
          :rules="[
            { type: 'number', message: '年龄必须为数字值'}
          ]"
          label="年龄："
        >
          <el-input-number v-model.number="member.age" :min="0" :max="150" placeholder="请输入年龄"/>
        </el-form-item>

        <el-form-item label="签名：" >
          <el-input v-model="member.sign" type="textarea" placeholder="请输入签名"/>
        </el-form-item>

        <el-form-item>
          <el-button type="success" @click="updateData()" >立即修改</el-button>
        </el-form-item>

      </el-form>
      <!-- 更多登录方式 -->
    </div>
  </article>
</template>

<script>
import ucenterApi from '~/api/ucenter'

export default {
  data() {
    return {
      member: {
        sex: 2,
        avatar: ''
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 拉取数据
    fetchData() {
      ucenterApi.getById().then((response) => {
        this.member = response.data.member
      })
    },
    // 修改数据
    update() {
      ucenterApi.updateData(this.member).then((response) => {
        this.$message.success(response.message)
      })
    },
    // 上传成功事件
    handleAvatarSuccess(response, file) {
      if (response.success) {
        console.log(response)
        this.member.avatar = response.data.url
        console.log('avatar', this.member.avatar)
        // 强制重新渲染
        this.$forceUpdate()
      } else {
        this.$message.error('上传失败 （非20000）')
      }
    },
    // 上传之前事件
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    // 按钮事件
    updateData() {
      this.update()
    }
  }
}
</script>

<style scoped>
  .changeImg .avatar-uploader .el-upload{
    border-radius: 50%;
  }
  .avatar-uploader {
    border-style: dashed;
    border-width: 5px;
    border: 2px dashed #68cb9b;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 178px;
    height: 178px;
    border-radius: 50%;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #68cb9b;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;

  }
  .avatar {

    width: 178px;
    height: 178px;
    display: block;

  }
</style>
