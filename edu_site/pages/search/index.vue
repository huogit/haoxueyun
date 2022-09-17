<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- 课程列表 开始 -->
    <section class="container">
      <header class="comm-title">
        <h2 class="fl tac">
          <span class="c-333">课程搜索结果</span>
        </h2>
      </header>
      <section class="c-sort-box">

        <div class="mt40">
          <!-- /无数据提示 开始-->
          <section v-if="courseList.length === 0" class="no-data-wrap">
            <em class="icon30 no-data-ico">&nbsp;</em>
            <span class="c-666 fsize14 ml10 vam">没有相关数据，小编正在努力整理中...</span>
          </section>
          <!-- /无数据提示 结束-->
          <!-- 数据列表 开始-->
          <article class="comm-course-list">
            <ul id="bna" class="of">
              <li v-for="item in courseList" :key="item.id">
                <div class="cc-l-wrap">
                  <section class="course-img">
                    <img :alt="item.title" :src="item.cover" class="img-responsive">
                    <div class="cc-mask">
                      <a :href="'/course/'+item.id" title="开始学习" class="comm-btn c-btn-1">开始学习</a>
                    </div>
                  </section>
                  <h3 class="hLh30 txtOf mt10">
                    <a :href="'/course/'+item.id" :title="item.title" class="course-title fsize18 c-333">{{ item.title }}</a>
                  </h3>
                  <section class="mt10 hLh20 of">
                    <span v-if="item.price === 0" class="fr jgTag bg-green">
                      <i class="c-fff fsize12 f-fA">免费</i>
                    </span>
                    <span v-else class="fr jgTag ">
                      <i class="c-orange fsize12 f-fA"> ￥{{ item.price }}</i>
                    </span>
                    <span class="fl jgAttr c-ccc f-fA">
                      <i class="c-999 f-fA">{{ item.viewCount }}人学习</i>
                      |
                      <i class="c-999 f-fA">{{ item.buyCount }}人购买</i>
                    </span>
                  </section>
                </div>
              </li>
              <!-- <li>
                <div class="cc-l-wrap">
                  <section class="course-img">
                    <img alt="Linux集群教程" src="~/assets/photo/course/02.jpg" class="img-responsive">
                    <div class="cc-mask">
                      <a href="/course/1" title="开始学习" class="comm-btn c-btn-1">开始学习</a>
                    </div>
                  </section>
                  <h3 class="hLh30 txtOf mt10">
                    <a href="/course/1" title="Linux集群教程" class="course-title fsize18 c-333">Linux集群教程</a>
                  </h3>
                  <section class="mt10 hLh20 of">
                    <span class="fr jgTag ">
                      <i class="c-orange fsize12 f-fA"> ￥99</i>
                    </span>
                    <span class="fl jgAttr c-ccc f-fA">
                      <i class="c-999 f-fA">100人学习</i>
                      |
                      <i class="c-999 f-fA">100人购买</i>
                    </span>
                  </section>
                </div>
              </li> -->
            </ul>
            <div class="clear"/>
          </article>
        </div>
        <!-- /数据列表 结束-->
      </section>
    </section>
  </div>
</template>
<script>
import courseApi from '~/api/course'

export default {

  data() {
    return {
      courseList: [],
      keyword: ''
    }
  },

  mounted() {
    this.keyword = this.$route.query.keyword
    if (!this.keyword) {
      window.alert('请输入搜索关键字')
      history.go(-1)
      return
    }
    this.fetchData()
  },

  methods: {
    fetchData() {
      courseApi.searchCourse(this.keyword).then((response) => {
        this.courseList = response.data.items
      })
    }
  }
}
</script>

