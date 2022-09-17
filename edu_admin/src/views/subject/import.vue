<template>
  <div class="app-container">

    <el-form ref="form" label-width="120px">
      <el-form-item label="信息描述">
        <el-tag type="info" plain>excel模板说明</el-tag>
        <el-tag>
          <i class="el-icon-download"/>
          <a :href="defaultExcelTemplate" >点击下载模版</a>
        </el-tag>
      </el-form-item>
      <el-form-item label="选择excel">
        <el-upload
          ref="upload"
          :on-exceed="fileUploadExceed"
          :limit="1"
          :auto-upload="false"
          :on-error="fileUploadError"
          :on-success="fileUploadSuccess"
          class="upload-demo"
          accept="application/vnd.ms-excel"
          action="http://127.0.0.1:9110/admin/edu/subject/import">
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <el-button :disabled="importBtnDisabled" style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
          <div slot="tip" class="el-upload__tip">只能上传excel文件，且不超过500kb</div>
        </el-upload>
      </el-form-item>
    </el-form>

  </div>
</template>

<script>

export default {
  data() {
    return {
      defaultExcelTemplate: process.env.OSS_PATH + '/excel/课程分类列表模板.xls', // 默认Excel模板
      importBtnDisabled: false
    }
  },
  methods: {
    // 限制文件上传个数
    fileUploadExceed(files) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件`)
    },
    // 文件上传失败回调
    fileUploadError(error) {
      console.log('fileUploadError')
      this.importBtnDisabled = false
      this.$message({
        'type': 'error',
        'message': error
      })
    },
    // 文件上传成功回调
    fileUploadSuccess(response) {
      if (response.success) {
        this.importBtnDisabled = false
        this.$message.success(response.message)
        this.$refs.upload.clearFiles() // 清空文件列表
      } else {
        this.$message.error(response.message)
      }
    },
    // 执行上传
    submitUpload() {
      this.importBtnDisabled = true
      this.$refs.upload.submit() // 提交上传请求
    }
  }
}
</script>

<style scoped>

</style>

