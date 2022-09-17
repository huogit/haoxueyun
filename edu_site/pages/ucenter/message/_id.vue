<template>

  <article class="col-7 fl">
    <section>
      <div>
        <section class="c-infor-tabTitle c-tab-title">
          <a href="javascript: void(0)" title="消息详情" class="current">
            消息详情
          </a>
        </section>
      </div>
    </section>
    <div class="title-bar_3587e0"><div class="title_4f1f61">{{ message.title }}</div><div class="time_9c9135">时间：{{ message.gmtCreate }}   浏览人数：{{ message.viewCount }}</div><div class="line_410a12"/></div>
    <div class="ql-snow ql-editor detail_c7ff1a">
      <span v-html="message.description" />
    </div>
  </article>
</template>

<script>
import ucenterApi from '@/api/ucenter'

export default {

  data() {
    return {
      message: {
        id: '',
        title: '',
        viewCount: 0,
        description: '',
        gmtCreate: ''
      }

    }
  },

  created() {
    this.message.id = this.$route.params.id
    this.fetchData()
  },

  methods: {
    fetchData() {
      ucenterApi.getMessageById(this.message.id).then((response) => {
        console.log(response)
        this.message = response.data.item
      })
    }
  }

}
</script>

<style scoped>
 .title-bar_3587e0 {
    text-align: left;
    padding: 5px 4px 0 5px;
    /* background: #fff;*/
}
.title-bar_3587e0 .title_4f1f61{
    text-align:center;
    font-weight: 600;
    font-size: 20px;
    line-height: 50px;
    color: #191c1e;
}
.title-bar_3587e0 .time_9c9135 {
    text-align:center;
    font-weight: 400;
    font-size: 20px;
    line-height: 28px;
    color: #5e697a;
}
.title-bar_3587e0 .line_410a12 {
    height: 1px;
    width: 100%;
    background: #dfdcf4;
    margin-top: 19px;
}
.detail_c7ff1a {
    padding: 40px 41px 30px 40px;

}
.ql-editor {
    box-sizing: border-box;
    line-height: 1.42;
    height: 100%;
    outline: none;
    overflow-y: auto;
    padding: 20px 17px;
    -o-tab-size: 4;
    tab-size: 4;
    -moz-tab-size: 4;
    text-align: left;
    white-space: pre-wrap;
    word-wrap: break-word;
}
</style>
