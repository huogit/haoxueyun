<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- 讲师介绍 开始 -->
    <section class="container">
      <header class="comm-title">
        <h2 class="fl tac">
          <span class="c-333">讲师介绍</span>
        </h2>
      </header>
      <div class="t-infor-wrap">
        <!-- 讲师基本信息 开始 -->
        <section class="fl t-infor-box c-desc-content">
          <div class="mt20 ml20">
            <section class="t-infor-pic">
              <img :alt="teacher.name" src="~/assets/photo/teacher/01.jpg" width="288" height="288">
            </section>
            <h3 class="hLh30">
              <span class="fsize24 c-333">{{ teacher.name }}
                &nbsp;
                {{ teacher.level===1?'高级讲师':'首席讲师' }}
              </span>
            </h3>
            <section class="mt10">
              <span class="t-tag-bg">{{ teacher.intro }}  </span>
            </section>
            <section class="t-infor-txt">
              <p class="mt20">{{ teacher.career }}</p>
            </section>
            <div class="clear"/>
          </div>
        </section>
        <!-- /讲师基本信息 结束 -->
        <div class="clear"/>
      </div>
      <section class="mt30">
        <div>
          <header class="comm-title all-teacher-title c-course-content">
            <h2 class="fl tac">
              <span class="c-333">主讲课程</span>
            </h2>
            <section class="c-tab-title">
              <a href="javascript: void(0)">&nbsp;</a>
            </section>
          </header>
          <!-- 无数据提示 开始-->
          <section v-if="courseList.length === 0" class="no-data-wrap">
            <em class="icon30 no-data-ico">&nbsp;</em>
            <span class="c-666 fsize14 ml10 vam">没有相关数据，小编正在努力整理中...</span>
          </section>
          <!-- /无数据提示 结束-->

          <!-- 课程列表 开始-->
          <article class="comm-course-list">
            <ul class="of">
              <li v-for="item in courseList" :key="item.id">
                <div class="cc-l-wrap">
                  <section class="course-img">
                    <img :alt="item.title" :src="item.cover" class="img-responsive">
                    <div class="cc-mask">
                      <a :href="'/course/'+ item.id" title="开始学习" class="comm-btn c-btn-1">开始学习</a>
                    </div>
                  </section>
                  <h3 class="hLh30 txtOf mt10">
                    <a :href="'/course/'+ item.id" :title="item.title" class="course-title fsize18 c-333">{{ item.title }}</a>
                  </h3>
                </div>
              </li>
              <!-- <li>
                <div class="cc-l-wrap">
                  <section class="course-img">
                    <img alt="Linux集群教程" src="~/assets/photo/course/02.jpg" class="img-responsive">
                    <div class="cc-mask">
                      <a href="/course/2" title="开始学习" class="comm-btn c-btn-1">开始学习</a>
                    </div>
                  </section>
                  <h3 class="hLh30 txtOf mt10">
                    <a href="/course/2" title="Linux集群教程" class="course-title fsize18 c-333">Linux集群教程</a>
                  </h3>
                </div>
              </li> -->

            </ul>
            <div class="clear"/>
          </article>
          <!-- /课程列表 结束-->
        </div>
      </section>
    </section>
    <!-- /讲师介绍 结束 -->
  </div>
</template>

<script>
import teacherApi from '~/api/teacher'
export default {
  // 在这个方法被调用的时候，第一个参数被设定为当前页面的 上下文对象，
  async asyncData(page) {
    console.log(page)
    const response = await teacherApi.getById(page.route.params.id)
    if (response.data.teacher == null) {
      window.alert('该教师不存在')
      history.go(-1)
    }
    return {
      teacher: response.data.teacher,
      courseList: response.data.courseList
    }
  }
}
</script>
