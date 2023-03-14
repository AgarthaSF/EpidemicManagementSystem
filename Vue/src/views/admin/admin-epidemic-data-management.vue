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
          <span>疫情数据处理</span>
        </a-menu-item>

        <a-menu-item key="2">
          <AreaChartOutlined/>
          <span>确诊病例处理</span>
        </a-menu-item>

        <a-menu-item key="3">
          <AreaChartOutlined/>
          <span>流调信息处理</span>
        </a-menu-item>

      </a-menu>
    </a-layout-sider>


    <a-layout-content
        :style="{ background: '#fff', padding: '0px', margin: 0, minHeight: '300px' }">


      <div id="covid-data" v-show="currentSelected === 1">
        <admin-covid-data></admin-covid-data>
      </div>

      <div class="diagnosed-case" v-show="currentSelected === 2">
        <admin-diagnosed-case-management></admin-diagnosed-case-management>
      </div>

      <div class="diagnosed-survey" v-show="currentSelected === 3">
        <admin-diagnosed-survey-management></admin-diagnosed-survey-management>
      </div>


    </a-layout-content>

  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import {StarOutlined, LikeOutlined, MessageOutlined, MailOutlined} from '@ant-design/icons-vue';

import AdminCovidData from "@/views/admin/admin-covid-data.vue";
import AdminDiagnosedCaseManagement from "@/views/admin/admin-diagnosed-case-management.vue";
import AdminDiagnosedSurveyManagement from "@/views/admin/admin-diagnosed-survey-management.vue";

export default defineComponent({
  name: 'AdminEpidemicDataManagementManagement',
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
    MailOutlined,
    AdminCovidData,
    AdminDiagnosedCaseManagement,
    AdminDiagnosedSurveyManagement,
  },
  setup() {
    const currentSelected = ref();

    const handleClick = (value: any) => {
      if (value.key == 1) {
        currentSelected.value = 1;
      } else if (value.key == 2) {
        currentSelected.value = 2;
      }
      else if (value.key == 3) {
        currentSelected.value = 3;
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
