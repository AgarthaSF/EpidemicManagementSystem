<template>
  <a-layout>

    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0}"
          @click="handleClick"
      >

        <a-menu-item key="1">
          <MailOutlined/>
          <span>待办处理</span>
        </a-menu-item>

        <a-menu-item key="2">

          <AreaChartOutlined/>
          <span>历史申请</span>
<!--          <router-link to="/staff/daily-check">每日打卡</router-link>-->

        </a-menu-item>

<!--        <a-menu-item key="/staff/daily-check">-->
<!--          <router-link to="/staff/daily-check">每日打卡</router-link>-->
<!--        </a-menu-item>-->

      </a-menu>
    </a-layout-sider>


    <a-layout-content
        :style="{ background: '#fff', padding: '0px', margin: 0, minHeight: '300px' }">


      <div id="application_todo" v-show="currentSelected === 1">
        <counselor-out-todo></counselor-out-todo>
      </div>

      <div class="application_processed " v-show="currentSelected === 2">
        <counselor-out-processed></counselor-out-processed>
      </div>


    </a-layout-content>

  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import {StarOutlined, LikeOutlined, MessageOutlined, MailOutlined} from '@ant-design/icons-vue';

import CounselorOutProcessed from "@/views/counselor/counselor-out-processed.vue";
import CounselorOutTodo from "@/views/counselor/counselor-out-todo.vue";

export default defineComponent({
  name: 'CounselorOutManagement',
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
    MailOutlined,
    CounselorOutProcessed,
    CounselorOutTodo,
  },
  setup() {
    const currentSelected = ref();

    const handleClick = (value: any) => {
      if (value.key == 1) {
        currentSelected.value = 1;
      } else if (value.key == 2) {
        currentSelected.value = 2;
      }
    }

    onMounted(() => {
      currentSelected.value = 1;
    })


    return {
      currentSelected,
      handleClick
    }

  }
});
</script>


<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}

.taskPlan {
  width: 1000px;
  height: 600px;
}


</style>
