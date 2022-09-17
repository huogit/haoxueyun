
<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- 课程详情 开始 -->
    <section class="container">

      <!-- 课程所属分类 开始 -->
      <section class="path-wrap txtOf hLh30">
        <a href="/" title class="c-999 fsize14">首页</a>
        \
        <a href="/course" title class="c-999 fsize14">课程列表</a>
        \
        <a :href="'./?subjectParentId=' + course.subjectLevelOneId" class="c-333 fsize14">{{ course.subjectLevelOne }}</a>
        \
        <a
          :href="'./?subjectParentId=' + course.subjectLevelOneId + '&subjectId='+course.subjectLevelTwoId"
          class="c-333 fsize14">{{ course.subjectLevelTwo }}
        </a>
      </section>
      <!-- /课程所属分类 结束 -->

      <!-- 课程基本信息 开始 -->
      <div>
        <article class="c-v-pic-wrap" style="height: 357px;">
          <section id="videoPlay" class="p-h-video-box">
            <img :src="course.cover" :alt="course.title" class="dis c-v-pic">
          </section>
        </article>
        <aside class="c-attr-wrap">
          <section class="ml20 mr15">
            <h2 class="hLh30 txtOf mt15">
              <span class="c-fff fsize24">{{ course.title }}</span>
            </h2>
            <section class="c-attr-jg">
              <span class="c-fff">价格：</span>
              <b class="c-yellow" style="font-size:24px;">￥{{ course.price }}</b>
            </section>
            <section class="c-attr-mt c-attr-undis">
              <span class="c-fff fsize14">主讲：{{ course.teacherName }}&nbsp;&nbsp;&nbsp;</span>
            </section>
            <section class="c-attr-mt of">
              <span v-if="!isCollect" class="ml10 vam">
                <em class="icon18 scIcon"/>
                <a class="c-fff vam" title="收藏" href="#" @click="addCollect(course.id)" >收藏</a>
              </span>
              <span v-if="isCollect" class="ml10 vam">
                <em class="icon18 scIcon"/>
                <a class="c-fff vam" title="取消收藏" href="#" @click="removeCollect(course.id)" >取消收藏</a>
              </span>
            </section>

            <section v-if="isBuy || course.price === 0" class="c-attr-mt">
              <a href="#" title="立即观看" class="comm-btn c-btn-3">立即观看</a>
            </section>

            <section v-else class="c-attr-mt">
              <a
                href="javascript:void(0);"
                title="立即购买"
                class="comm-btn c-btn-3"
                @click="createOrder()">立即购买</a>
            </section>
          </section>
        </aside>
        <aside class="thr-attr-box">
          <ol class="thr-attr-ol">
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">购买数</span>
                <br>
                <h6 class="c-fff f-fM mt10">{{ course.buyCount }}</h6>
              </aside>
            </li>
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">课时数</span>
                <br>
                <h6 class="c-fff f-fM mt10">{{ course.lessonNUm }}</h6>
              </aside>
            </li>
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">浏览数</span>
                <br>
                <h6 class="c-fff f-fM mt10">{{ course.viewCount }}</h6>
              </aside>
            </li>
          </ol>
        </aside>
        <div class="clear"/>
      </div>
      <!-- /课程基本信息 结束 -->

      <div class="mt20 c-infor-box">
        <article class="fl col-7">
          <section class="mr30">
            <div class="i-box">
              <div>
                <section id="c-i-tabTitle" class="c-infor-tabTitle c-tab-title">
                  <a name="c-i" class="current" title="课程详情">课程详情</a>
                </section>
              </div>
              <article class="ml10 mr10 pt20">

                <!-- 课程详情介绍 开始 -->
                <div>
                  <h6 class="c-i-content c-infor-title">
                    <span>课程介绍</span>
                  </h6>
                  <div class="course-txt-body-wrap">
                    <section class="course-txt-body" v-html="course.description">
                      <!-- 将内容中的html翻译过来 -->
                      <!-- <p>Java的发展历史，可追溯到1990年。当时Sun&nbsp;Microsystem公司为了发展消费性电子产品而进行了一个名为Green的项目计划。该计划
                      负责人是James&nbsp;Gosling。起初他以C++来写一种内嵌式软件，可以放在烤面包机或PAD等小型电子消费设备里，使得机器更聪明，具有人工智
                      能。但他发现C++并不适合完成这类任务！因为C++常会有使系统失效的程序错误，尤其是内存管理，需要程序设计师记录并管理内存资源。这给设计师们造成
                      极大的负担，并可能产生许多bugs。&nbsp;<br>
                        为了解决所遇到的问题，Gosling决定要发展一种新的语言，来解决C++的潜在性危险问题，这个语言名叫Oak。Oak是一种可移植性语言，也就是一种平台独立语言，能够在各种芯片上运行。<br>
                        1994年，Oak技术日趋成熟，这时网络正开始蓬勃发展。Oak研发小组发现Oak很适合作为一种网络程序语言。因此发展了一个能与Oak配合的浏
                        览器--WebRunner，后更名为HotJava，它证明了Oak是一种能在网络上发展的程序语言。由于Oak商标已被注册，工程师们便想到以自己常
                        享用的咖啡(Java)来重新命名，并于Sun&nbsp;World&nbsp;95中被发表出来。</p> -->
                    </section>
                  </div>
                </div>
                <!-- /课程详情介绍 结束 -->

                <!-- 课程大纲 开始-->
                <div class="mt50">
                  <h6 class="c-g-content c-infor-title">
                    <span>课程大纲</span>
                  </h6>
                  <section class="mt20">
                    <div class="lh-menu-wrap">
                      <menu id="lh-menu" class="lh-menu mt10 mr10">
                        <ul>
                          <!-- 课程章节目录 -->
                          <li v-for="chapter in chapterList" :key="chapter.id" class="lh-menu-stair">
                            <a :title="chapter.title" href="javascript: void(0)" class="current-1">
                              <em class="lh-menu-i-1 icon18 mr10"/>{{ chapter.title }}
                            </a>
                            <ol class="lh-menu-ol" style="display: block;">
                              <li v-for="video in chapter.children" :key="video.id" class="lh-menu-second ml30">
                                <a
                                  v-if="isBuy || Number(course.price) !== 0 "
                                  :title="video.title"
                                  :href="'/player/'+ video.videoSourceId"
                                  target="_blank">
                                  <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em>{{ video.title }}
                                </a>
                                <a
                                  v-if="Number(course.price) !== 0 && video.free===true"
                                  :title="video.title"
                                  :href="'/player/'+ video.videoSourceId"
                                  target="_blank">
                                  <span class="fr">
                                    <i class="free-icon vam mr10">免费试听</i>
                                  </span>
                                  <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em>{{ video.title }}
                                </a>
                                <a
                                  v-if="Number(course.price) === 0 && video.free===true"
                                  :title="video.title"
                                  :href="'/player/'+ video.videoSourceId"
                                  target="_blank">
                                  <span class="fr">
                                    <i class="free-icon vam mr10">免费观看</i>
                                  </span>
                                  <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em>{{ video.title }}
                                </a>

                                <a
                                  v-else
                                  :title="video.title"
                                >
                                  <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em>{{ video.title }}
                                </a>
                              </li>
                            </ol>
                          </li>
                          <!-- <li class="lh-menu-stair">
                            <a title="第二章" href="javascript: void(0)" class="current-1">
                              <em class="lh-menu-i-1 icon18 mr10"/>第二章
                            </a>
                            <ol class="lh-menu-ol" style="display: block;">
                              <li class="lh-menu-second ml30">
                                <a href="#" title>
                                  <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em>第二节
                                </a>
                              </li>
                              <li class="lh-menu-second ml30">
                                <a href="#" title>
                                  <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em>第二节
                                </a>
                              </li>
                            </ol>
                          </li> -->
                        </ul>
                      </menu>
                    </div>
                  </section>
                  <!-- /课程大纲 结束 -->
              </div></article>
            </div>
          </section>
        </article>
        <aside class="fl col-3">
          <div class="i-box">
            <!-- 主讲讲师 开始-->
            <div>
              <section class="c-infor-tabTitle c-tab-title">
                <a title href="javascript:void(0)">主讲讲师</a>
              </section>
              <section class="stud-act-list">
                <ul style="height: auto;">
                  <li>
                    <div class="u-face">
                      <a :href="'/teacher/'+ course.teacherId" target="_blank">
                        <img :src="course.avatar" width="50" height="50" alt>
                      </a>
                    </div>
                    <section class="hLh30 txtOf">
                      <a :href="'/teacher/'+ course.teacherId" class="c-333 fsize16 fl" target="_blank">{{ course.teacherName }}</a>
                    </section>
                    <section class="hLh20 txtOf">
                      <span class="c-999">{{ course.intro }}</span>
                    </section>
                  </li>
                </ul>
              </section>
            </div>
            <!-- /主讲讲师 结束 -->
          </div>
        </aside>
        <div class="clear"/>
      </div>
    </section>
    <!-- /课程详情 结束 -->
  </div>
</template>

<script>
import courseApi from '~/api/course'
import orderApi from '~/api/order'
import cookie from 'js-cookie'
import collectApi from '~/api/collect'

export default {
  async asyncData(page) {
    const response = await courseApi.getById(page.route.params.id)
    console.log('data', response.data)
    return {
      course: response.data.course,
      chapterList: response.data.chapterList
    }
  },
  data() {
    return {
      isBuy: false,
      isCollect: false
    }
  },

  created() {
    // 如果未登录，则isBuy=false
    // 如果已登录，则判断是否已购买
    var token = cookie.get('edu_jwt_token')
    if (token) {
      this.checkIsBuy()
      this.checkIsCollect()
    }
  },

  methods: {
    createOrder() {
      orderApi.saveData(this.course.id).then((response) => {
        // 携带orderId 跳转到订单页

        this.$router.push({ path: '/order/' + response.data.orderId })
      })
    },
    checkIsBuy() {
      orderApi.isBuy(this.course.id).then((response) => {
        this.isBuy = response.data.isBuy
      })
    },
    // 添加收藏
    addCollect(id) {
      collectApi.saveData(id).then((response) => {
        if (response) {
          this.isCollect = true
          this.$message.success('收藏成功')
        }
      })
    },
    // 取消收藏
    removeCollect(id) {
      collectApi.removeById(id).then((response) => {
        if (response) {
          this.isCollect = false
          this.$message.success(response.message)
        }
      })
    },
    // 判断是否收藏
    checkIsCollect() {
      collectApi.isCollect(this.course.id).then((response) => {
        this.isCollect = response.data.isCollect
        console.log('iscollect', this.isCollect)
      })
    }
  }
}
</script>

<style>
.course-txt-body ol, .course-txt-body ul{
    padding-left: 40px;
    margin: 16px 0;
}
.course-txt-body ol li {
  list-style: decimal;
}
.course-txt-body ul li{
  list-style: disc;
}
</style>
