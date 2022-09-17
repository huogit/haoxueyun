<template>
  <div class="app-container">
    <!-- 输入表单 -->
    <el-form ref="form" v-model="adForm" label-width="120px">
      <el-form-item label="广告推荐名称">
        <el-input v-model="adForm.title"/>
      </el-form-item>
      <el-form-item label="推荐位">
        <el-select v-model="adForm.typeId" placeholder="请选择">
          <el-option v-for="adType in adTypeList" :label="adType.title" :value="adType.id" :key="adType.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="排序">
        <el-input-number :min="0" v-model="adForm.sort" />
      </el-form-item>
      <el-form-item label="广告图片">
        <el-upload
          ref="upload"
          :on-success="handleAvatarSuccess"
          :on-error="handleAvatarError"
          :on-exceed="handleUploadExceed"
          :before-upload="beforeAvatarUpload"
          :limit="1"
          :file-list="fileList"
          class="upload-demo"
          action="http://localhost:9110/admin/oss/file/upload?module=ad"
          list-type="picture">
          <el-button style="margin-left: 10px;" size="small" type="success">上传到服务器</el-button>
          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
        </el-upload>
      </el-form-item>
      <el-form-item label="背景颜色">
        <el-color-picker v-model="adForm.color"/>
      </el-form-item>
      <el-form-item label="链接地址">
        <el-input v-model="adForm.linkUrl" />
      </el-form-item>
      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate()">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import adApi from '@/api/ad'
import adTypeApi from '@/api/adType'
export default{
  data() {
    return {
      BASE_API: process.env.BASE_API,
      adForm: {
        sort: 0
      },
      adTypeList: [],
      saveBtnDisabled: false,
      fileList: [] // 上传文件列表
    }
  },
  created() {
    if (this.$route.params.id) {
      this.fetchDataById(this.$route.params.id)
    }
    // 获取推荐位列表
    this.fetchAdTypeList()
  },
  methods: {
    // 初始化推荐位列表
    fetchAdTypeList() {
      adTypeApi.list().then((response) => {
        this.adTypeList = response.data.items
      })
    },
    // 根据id查询记录
    fetchDataById(id) {
      adApi.getById(id).then((response) => {
        this.adForm = response.data.item
        this.fileList = [{ 'url': this.adForm.imageUrl }]
      })
    },
    // 根据id更新记录
    update() {
      // this.adForm.id = this.$route.params.id
      adApi.updatedById(this.adForm).then((response) => {
        this.$message.success(response.message)
        this.saveBtnDisabled = true
        this.$router.push({ path: '/ad/list' })
      })
    },
    // 新增
    save() {
      adApi.save(this.adForm).then((response) => {
        this.$message.success(response.message)
        this.saveBtnDisabled = true
        this.$router.push({ path: '/ad/list' })
      })
    },
    // 保存事件
    saveOrUpdate() {
      if (this.adForm.id) {
        this.update()
      } else {
        this.save()
      }
    },
    // 上传成功回调
    handleAvatarSuccess(res, file) {
      if (res.success) {
        this.adForm.imageUrl = res.data.url
      } else {
        this.$message.error('上传失败1')
      }
    },
    // 错误处理
    handleAvatarError() {
      this.$message.error('上传失败2')
    },
    // 上传多于一个文件
    handleUploadExceed(files, fileList) {
      this.$message.warning('想要重新上传图片，请先删除已上传的图片')
    },
    // 上传校验
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
    }
  }
}
</script>
