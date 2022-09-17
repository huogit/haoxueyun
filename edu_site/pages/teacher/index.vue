<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- 讲师列表 开始 -->
    <section class="container">
      <header class="comm-title">
        <h2 class="fl tac">
          <span class="c-333">名师团队</span>
        </h2>
      </header>
      <section class="c-sort-box unBr">
        <div>
          <!-- /无数据提示 开始-->
          <section v-if="items.length === 0" class="no-data-wrap">
            <em class="icon30 no-data-ico">&nbsp;</em>
            <span class="c-666 fsize14 ml10 vam">没有相关数据，小编正在努力整理中...</span>
          </section>
          <!-- /无数据提示 结束-->

          <!-- /数据列表 开始-->
          <article v-if="items.length > 0" class="i-teacher-list">
            <ul class="of">
              <!-- <div v-for="(item,index) in items" :key="index">
                <a :href="'/teacher/' + item.id">{{ item.name }}</a>
              </div> -->

              <li v-for="item in items" :key="item.id">
                <section class="i-teach-wrap">
                  <div class="i-teach-pic">
                    <a :href="'/teacher/'+item.id" :title="item.name">
                      <img :alt="item.name" :src="item.avatar" width="142">
                    </a>
                  </div>
                  <div class="mt10 hLh30 txtOf tac">
                    <a :href="'/teacher/'+item.id" :title="item.name" class="fsize18 c-666">{{ item.name }}</a>
                  </div>
                  <div class="hLh30 txtOf tac">
                    <span class="fsize14 c-999" >{{ item.intro }}</span>
                  </div>
                  <div class="mt15 i-q-txt">
                  <p class="c-999 f-fA">{{ item.career }}</p></div>
                </section>
              </li>
              <!-- <li>
                <section class="i-teach-wrap">
                  <div class="i-teach-pic">
                    <a href="/teacher/2" title="韩老师">
                      <img alt="韩老师" src="~/assets/photo/teacher/02.jpg" width="142">
                    </a>
                  </div>
                  <div class="mt10 hLh30 txtOf tac">
                    <a href="/teacher/2" title="韩老师" class="fsize18 c-666">韩老师</a>
                  </div>
                  <div class="hLh30 txtOf tac">
                    <span class="fsize14 c-999" >尚硅谷高级讲师</span>
                  </div>
                  <div class="mt15 i-q-txt">
                    <p class="c-999 f-fA">行业老兵，IT教育风云人物。清华毕业，拥有九年大型项目开发和管理经验，曾任新浪网软件项目经理，使用PHP与JavaEE技术开发新浪网系统，北京点击科技公司高级软件工程师，用友软件股份有限公司U8事业部软件工程师。主持或参与《新浪邮件系统》、《橙红sns网站》、《点击科技协同软件群组服务器端(Linux/solaris平台)》、《总参语音监控系统》、《英语学习机系统》、《用友erp(u8产品)系统》等项目。实战经验丰富，授课通俗易懂，多年来培训的学员已成功在用友、搜狐、淘宝、中软等知名企业就职。 代表作品《循序渐进学Java》《尚硅谷韩顺平Linux》《尚硅谷韩顺平Go语言》。</p>
                  </div>
                </section>
              </li>
              -->
            </ul>
            <div class="clear"/>
          </article>
          <!-- /数据列表 结束-->
        </div>
      </section>
    </section>

    <!-- /讲师列表 结束 -->
  </div>
</template>

<script>
import teacherApi from '~/api/teacher'

export default {
  // 同步获取数据
  // async  asyncData() {
  //   console.log('asyncData')
  //   const response = await teacherApi.getList()
  //   console.log(response)
  //   return {
  //     items: response.data.items
  //   }
  // },

  // 异步获取数据
  asyncData() {
    // 将对象返回
    return teacherApi.getList().then((response) => {
      // 这个回调方法要返回一个对象
      return {
        items: response.data.items
      }
    })
  },

  data() {
    return {
      teacherList: [] // 这里的数据最后会和asyncData中的数据定义合并
    }
  }
}
</script>
