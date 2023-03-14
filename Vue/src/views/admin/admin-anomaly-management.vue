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
          <span>每日打卡异常</span>
        </a-menu-item>

        <a-menu-item key="2">
          <MailOutlined/>
          <span>信息上报异常</span>
        </a-menu-item>

        <a-menu-item key="3">
          <AreaChartOutlined/>
          <span>校内人员旅居史异常</span>
        </a-menu-item>

        <a-menu-item key="4">
          <AreaChartOutlined/>
          <span>校外人员旅居史异常</span>
        </a-menu-item>

        <a-menu-item key="5">
          <AreaChartOutlined/>
          <span>密接异常</span>
        </a-menu-item>

      </a-menu>
    </a-layout-sider>


    <a-layout-content
        :style="{ background: '#fff', padding: '0px', margin: 0, minHeight: '300px' }">

      <div id="daily-check-anomaly" v-show="currentSelected === 1">
        <daily-check-anomaly></daily-check-anomaly>
      </div>

      <div id="info_upload-anomaly" v-show="currentSelected === 2">
        <info-upload-anomaly></info-upload-anomaly>
      </div>

      <div class="staff-travel-anomaly" v-show="currentSelected === 3">
        <staff-travel-anomaly></staff-travel-anomaly>
      </div>
      <div class="foreigner-travel-anomaly" v-show="currentSelected === 4">
        <foreigner-travel-anomaly></foreigner-travel-anomaly>
      </div>

      <div class="access_anomaly" v-show="currentSelected === 5">
        <access-anomaly></access-anomaly>
      </div>


    </a-layout-content>

  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import {StarOutlined, LikeOutlined, MessageOutlined, MailOutlined} from '@ant-design/icons-vue';
import InfoUploadAnomaly from "@/views/anomaly/info-upload-anomaly.vue";
import DailyCheckAnomaly from "@/views/anomaly/daily-check-anomaly.vue";
import StaffTravelAnomaly from "@/views/anomaly/staff-travel-anomaly.vue";
import ForeignerTravelAnomaly from "@/views/anomaly/foreigner-travel-anomaly.vue";
import AccessAnomaly from "@/views/anomaly/access-anomaly.vue";


export default defineComponent({
  name: 'AdminAnomalyManagement',
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
    MailOutlined,
    InfoUploadAnomaly,
    DailyCheckAnomaly,
    StaffTravelAnomaly,
    ForeignerTravelAnomaly,
    AccessAnomaly,
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
      else if (value.key == 4) {
        currentSelected.value = 4;
      }
      else if (value.key == 5) {
        currentSelected.value = 5;
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
