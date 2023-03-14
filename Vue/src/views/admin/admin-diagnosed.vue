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
          <span>流调地图</span>
        </a-menu-item>

        <a-menu-item key="2">
          <AreaChartOutlined/>
          <span>确诊详情</span>
        </a-menu-item>

      </a-menu>
    </a-layout-sider>


    <a-layout-content
        :style="{ background: '#fff', padding: '0px', margin: 0, minHeight: '300px' }">


      <div id="diagnosed_map" v-show="currentSelected === 1">
        <admin-diagnosed-map></admin-diagnosed-map>
      </div>

      <div class="diagnosed_survey" v-show="currentSelected === 2">
        <admin-survey-detail></admin-survey-detail>
      </div>



    </a-layout-content>

  </a-layout>

</template>

<script lang="ts">


import AdminDiagnosedMap from "@/views/admin/admin-diagnosed-map.vue";
import AdminSurveyDetail from '@/views/admin/admin-survey-detail.vue';
import {ref} from "vue";
export default {
  name: "AdminDiagnosed",
  components: {
    AdminDiagnosedMap,
    AdminSurveyDetail,
  },

  setup(){

    const currentSelected = ref(1);

    const handleClick = (value: any) => {
      if (value.key == 1) {
        currentSelected.value = 1;
      } else if (value.key == 2) {
        currentSelected.value = 2;
      }
    }

    return{
      currentSelected,
      handleClick,
    }

  }

}
</script>

<style scoped>

</style>