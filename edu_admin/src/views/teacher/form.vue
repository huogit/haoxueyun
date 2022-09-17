<template>
  <div class="app-container">
    <!-- 输入表单 -->
    <el-form label-width="120px">
      <el-form-item label="讲师名称" >
        <el-input v-model="teacher.name" />
      </el-form-item>
      <el-form-item label="入驻时间">
        <el-date-picker v-model="teacher.joinDate" value-format="yyyy-MM-dd" />
      </el-form-item>
      <el-form-item label="讲师排序">
        <el-input-number v-model="teacher.sort" :min="0"/>
      </el-form-item>
      <el-form-item label="讲师头衔">
        <el-select v-model="teacher.level">
          <!--
            数据类型一定要和取出的json中的一致，否则没法回填
            因此，这里value使用动态绑定的值，保证其数据类型是number
            -->
          <el-option :value="1" label="高级讲师"/>
          <el-option :value="2" label="首席讲师"/>
        </el-select>
      </el-form-item>
      <el-form-item label="讲师资历">
        <el-input v-model="teacher.career"/>
      </el-form-item>
      <el-form-item label="讲师简介">
        <el-input v-model="teacher.intro" :rows="10" type="textarea"/>
      </el-form-item>

      <!-- 讲师头像：TODO '
        on-success	文件上传成功时的钩子	function(response, file, fileList)
        before-upload	上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传。	function(file)
      -->

      <el-form-item label="讲师头像">
        <el-upload
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :on-error="handleAvatarError"
          class="avatar-uploader"
          action="http://localhost:9110/admin/oss/file/upload?module=avatar">
          <img v-if="imageUrl" :src="imageUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"/>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate()">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
// 引入api
import teacherApi from '@/api/teacher'
export default {
  // 定义数据模型
  data() {
    return {
      teacher: {
        sort: 0,
        level: 1
      },
      // 保存按钮是否禁用，防止表单重复提交
      saveBtnDisabled: false,
      imageUrl: ''
    }
  },
  // 一般可以在created函数中调用ajax获取页面初始化所需的数据。
  created() {
    if (this.$route.params.id) {
      this.getData(this.$route.params.id)
    }
  },
  // 定义方法
  methods: {
    saveOrUpdate() {
      // 禁用保存按钮
      this.saveBtnDisabled = true
      if (this.teacher.id) {
        this.updateData()
      } else {
        this.saveData()
      }
    },
    // 新增讲师
    saveData() {
      teacherApi.save(this.teacher).then((response) => {
        console.log(response)
        // 消息提示$message
        this.$message({
          message: response.message,
          type: 'success'
          // 这个response 是经过响应拦截器处理之后的结果

        })
        // 跳转页面
        this.$router.push({ path: '/teacher' })
      })
    },
    // 查看讲师
    getData(id) {
      teacherApi.getById(id).then(response => {
        this.teacher = response.data.item
      })
    },
    // 更新讲师
    updateData() {
      teacherApi.updateById(this.teacher).then(response => {
        this.$message({
          type: 'success',
          message: response.message
        })
        // 跳转
        this.$router.push({
          path: '/teacher'
        })
      })
    },

    // 上传成功回调
    handleAvatarSuccess(response, file) {
      if (response.success) {
        this.imageUrl = response.data.url

        // this.teacher
        // 强制重新渲染
        this.$forceUpdate()
      } else {
        this.$message.error('上传失败 （非20000）')
      }
    },
    // 上传校验
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      // 限制不能大于2m
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传图片只能是JPG格式！')
      }

      if (!isLt2M) {
        this.$message.error('上传图片只能小于2M')
      }

      return isJPG && isLt2M
    },
    // 上传错误处理
    handleAvatarError() {
      console.log('error')
      this.$message.error('上传失败（http失败）')
    }
  }
}
</script>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 130px;
    height: 130px;
    line-height: 130px;
    text-align: center;
  }
  .avatar {
    width: 130px;
    height: 130px;
    display: block;
  }
</style>
