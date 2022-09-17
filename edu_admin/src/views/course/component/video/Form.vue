<template>
  <div id="app-container">

    <el-dialog :visible.sync="dialogFormVisible" title="添加课时" @close="close()">
      <el-form :model="video" label-width="120px">
        <el-form-item label="课时标题">
          <el-input v-model="video.title"/>
        </el-form-item>
        <el-form-item label="课时排序">
          <el-input-number v-model="video.sort" :min="0"/>
        </el-form-item>
        <el-form-item label="是否免费">
          <el-radio-group v-model="video.free">
            <el-radio :label="true">免费</el-radio>
            <el-radio :label="false">收费</el-radio>
          </el-radio-group>
        </el-form-item>
        <!--上传视频-->
        <el-form-item label="上传视频">
          <el-upload
            ref="upload"

            :before-remove="handleBeforeRemove"
            :on-remove="handleOnRemove"
            :auto-upload="false"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :on-exceed="handleUploadExceed"
            :file-list="fileList"
            :limit="1"
            action="http://127.0.0.1:9110/admin/vod/media/upload">

            <el-button slot="trigger" size="small" type="primary">选择视频</el-button>

            <el-button
              :disabled="uploadBtnDisabled"
              style="margin-left: 10px;"
              size="small"
              type="success"
              @click="submitUpload()">上传</el-button>

          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="close()">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import videoApi from '@/api/video'
import vodApi from '@/api/vod'
export default {
  data() {
    return {
      dialogFormVisible: false,
      video: {
        courseId: this.$parent.courseId,
        free: false,
        sort: 0

      },
      fileList: [],
      uploadBtnDisabled: false,
      videoUpSuccess: false
    }
  },

  watch: {
    'video.videoSourceId': {
      handler() {
        this.message.success('watch:上传视频成功')
      }
    }
  },
  methods: {
    // 回显课时
    fetchVideoById() {
      videoApi.getById(this.video.id).then((response) => {
        this.video = response.data.item
        // 回显
        if (this.video.videoOriginalName) {
          this.fileList = [{ 'name': this.video.videoOriginalName }]
        }
      })
    },
    // 保存课时
    save() {
      this.video.courseId = this.$parent.$parent.courseId
      videoApi.save(this.video).then((response) => {
        this.$message.success(response.message)
        this.close()
        this.$parent.fetchNestedChapterList(this.$parent.$parent.courseId)
      })
    },
    // 更新课时
    update() {
      videoApi.update(this.video).then((response) => {
        this.$message.success(response.message)
        this.close()
        this.$parent.fetchNestedChapterList(this.$parent.$parent.courseId)
      })
    },
    // 开启组件
    open(chapterId, videoId) {
      this.dialogFormVisible = true
      console.log('videoid', videoId)
      this.video.chapterId = chapterId // video对象要chapterId
      if (videoId) {
        this.video.id = videoId
        // console.log(chapterId, videoId)
        this.fetchVideoById()
      }
    },
    // 关闭组件
    close() {
      this.dialogFormVisible = false
      // 重置表单
      this.video = {
        free: false,
        sort: 0
      }
      this.fileList = [] // 重置视频上传列表
    },
    // 文件超出个数限制时的钩子
    handleUploadExceed(files, fileList) {
      this.$message.warning('想要重新上传视频，请先删除已上传的视频')
    },
    // 上传
    submitUpload() {
      this.uploadBtnDisabled = true
      this.$refs.upload.submit()
    },
    // 视频上传成功的回调
    handleUploadSuccess(response, file, fileList) {
      this.uploadBtnDisabled = false
      if (response.success) {
        console.log('videoSourceId', response.data.videoId)
        console.log('videoOriginalName', file.name)
        this.video.videoSourceId = response.data.videoId
        this.video.videoOriginalName = file.name
        this.message.success('上传成功')
      } else {
        this.$message.error('上传失败1')
      }
    },
    // 删除视频文件确认
    handleBeforeRemove(file, fileList) {
      return this.$confirm(`确定移除${file.name}`)
    },
    // 执行视频文件删除
    handleOnRemove(file, fileList) {
      if (!this.video.videoSourceId) {
        return
      }
      vodApi.removeByVodId(this.video.videoSourceId).then((response) => {
        this.video.videoSourceId = ''
        this.video.videoOriginalName = ''
        videoApi.updateById(this.video) // 更新数据
        this.$message.success(response.message)
      })
    },

    // 失败回调
    handleUploadError() {
      this.uploadBtnDisabled = false
      this.$message.error('上传失败2')
    },
    saveOrUpdate() {
      if (!this.videoUpSuccess && this.video.videoOriginalName == null) {
        window.alert('视频还没上传成功')
        return
      }
      if (!this.video.id) {
        this.save()
      } else {
        this.update()
      }
    }
  }
}
</script>
