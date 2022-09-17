<template>

  <article class="col-7 fl">
    <section>
      <div>
        <section class="c-infor-tabTitle c-tab-title">
          <a href="javascript: void(0)" title="更改手机号" class="current">
            系统消息
          </a>
        </section>
      </div>
    </section>
    <div class="content_c6b378">
      <li v-for="item in messageList" :key="item.id">
        <a :href="'/ucenter/message/'+ item.id" class="list-item_3176d9" >
          <div>{{ item.title }}</div>
          <div class="time_18d71f">{{ item.gmtCreate }}</div>
        </a>
      </li>

      <!-- <a v-for="item in messageList" :key="item.id" :href="'/ucenter/messsage/'+ item.id" class="list-item_3176d9" ><div>{{ item.title }}</div><div class="time_18d71f">{{ item.gmtCreate }}</div></a> -->
      <!-- <a class="list-item_3176d9" href="https://notice.ibox.art/html/notice/detail/notice_detail_1434.html?1663066093364" target="_blank"><div>【iBox上新公告】「启明星」「天空之影」系列藏品《半臂衫》白名单</div><div class="time_18d71f">2022-09-11 14:52</div></a> -->
      <div class="btn-div_9d306f"><div class="more-btn_ed7148">
        <el-pagination
          :total="total"
          :page-size="limit"
          :current-page="page"
          background
          layout="prev, pager, next"
          @current-change="handleCurrentChange"/>
      </div>
      </div>
  </div></article>
</template>

<script>
import ucenterApi from '@/api/ucenter'

export default {

  data() {
    return {
      messageList: [],
      total: 0,
      page: 1,
      limit: 1
    }
  },

  created() {
    this.fetchData()
  },

  methods: {
    fetchData() {
      ucenterApi.getMessageList(this.page, this.limit).then((response) => {
        console.log('response', response)
        this.messageList = response.data.items
        console.log(this.messageList)
        this.total = response.data.total
      })
    },
    handleCurrentChange(page) {
      this.page = page
      this.fetchData()
    }
  }

}
</script>

<style scoped>
 .partingline {
        margin-top: 10px;
        border: 1px solid #D5D5D5;
        margin-bottom: 10px;
}
.content_c6b378 {
    width: 90%;
    margin-top: 30px;
}
.content_c6b378 .list-item_3176d9 {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    font-weight: 500;
    font-size: 13px;
    color: #303133;
    border-bottom: 1px solid #ebeef5;
    padding: 20px 20px 18px 20px;
}
.content_c6b378 .btn-div_9d306f {
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    padding: 20px 20px 18px 20px;
    width: 100%;
}
.content_c6b378 .list-item_3176d9 .time_18d71f {
    font-weight: 400;
    font-size: 14px;
    color: #999;
}
</style>
