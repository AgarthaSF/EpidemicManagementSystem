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
          <span>学生管理</span>
        </a-menu-item>

        <a-menu-item key="2">
          <AreaChartOutlined/>
          <span>教工管理</span>
        </a-menu-item>

        <a-menu-item key="3">
          <MonitorOutlined/>
          <span>辅导员管理</span>
        </a-menu-item>


        <a-menu-item key="4">
          <MonitorOutlined/>
          <span>校外人员管理</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>


    <a-layout-content
        :style="{ background: '#fff', padding: '0px', margin: 0, minHeight: '300px' }">


      <div id="student_management" v-show="currentSelected === 1">
        <admin-student></admin-student>
      </div>

      <div class="teacher_management " v-show="currentSelected === 2">
        <admin-teacher></admin-teacher>
      </div>

      <div class="counselor_management" v-show="currentSelected === 3">
        <admin-counselor></admin-counselor>
      </div>

      <div class="foreigner_management" v-show="currentSelected === 4">
        <admin-foreigner></admin-foreigner>
      </div>


    </a-layout-content>

  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import {StarOutlined, LikeOutlined, MessageOutlined, MailOutlined} from '@ant-design/icons-vue';

import AdminStudent from "@/views/admin/admin-student.vue";
import AdminTeacher from "@/views/admin/admin-teacher.vue";
import AdminCounselor from "@/views/admin/admin-counselor.vue";
import AdminForeigner from "@/views/admin/admin-foreigner.vue";


export default defineComponent({
  name: 'Home',
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
    MailOutlined,
    AdminStudent,
    AdminTeacher,
    AdminCounselor,
    AdminForeigner
  },
  setup() {
    const currentSelected = ref();

    const handleClick = (value: any) => {
      console.log(value.key);
      if (value.key == 1) {
        currentSelected.value = 1;
      } else if (value.key == 2) {
        currentSelected.value = 2;
      } else if (value.key == 3) {
        currentSelected.value = 3;
      } else if (value.key == 4) {
        currentSelected.value = 4;
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
