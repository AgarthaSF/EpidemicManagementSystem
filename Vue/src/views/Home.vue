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
          <span>防疫通知</span>
        </a-menu-item>

        <a-menu-item key="2">
          <AreaChartOutlined/>
          <span>疫情速览</span>
        </a-menu-item>

        <a-menu-item key="3">
          <MonitorOutlined/>
          <span>疫情详细</span>
        </a-menu-item>

        <a-menu-item key="4">
          <ExclamationOutlined/>
          <span>风险地区</span>
        </a-menu-item>


      </a-menu>
    </a-layout-sider>


    <a-layout-content
        :style="{ background: '#fff', padding: '20px', margin: 0, minHeight: '280px' }">


      <div id="announcement" v-show="currentSelected === 1">
        <front-page-announcement></front-page-announcement>
      </div>


      <div class="quick_browse " v-show="currentSelected === 2">

        <a-row :gutter="24">
          <a-col :span="18">
            <covid-map :style="{ height: '545px', width: '970px'}"></covid-map>
          </a-col>

          <a-col :span="6">

            <quick-info :style="{marginTop : '20px'}"></quick-info>
          </a-col>
        </a-row>
      </div>


      <div class="detailed_info" v-show="currentSelected === 3">
        <covid-detail></covid-detail>
      </div>

      <div class="risk_detail" v-show="currentSelected === 4">
        <risk-detail></risk-detail>
      </div>


    </a-layout-content>

  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {StarOutlined, LikeOutlined, MessageOutlined, MailOutlined} from '@ant-design/icons-vue';
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";
import E from 'wangeditor'

import covidMap from '@/components/covid_map.vue';
import FrontPageAnnouncement from '@/components/front-page-announcement.vue';
import QuickInfo from '@/components/quick-info.vue';
import CovidDetail from "@/components/covid-detail.vue";
import RiskDetail from "@/components/risk-detail.vue";

export default defineComponent({
  name: 'Home',
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
    MailOutlined,
    covidMap,
    FrontPageAnnouncement,
    QuickInfo,
    CovidDetail,
    RiskDetail,
  },
  setup() {
    console.log("setup");
    const ebooks = ref();

    const pagination = {
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 3,
    };
    const actions: Record<string, string>[] = [
      {type: 'StarOutlined', text: '156'},
      {type: 'LikeOutlined', text: '156'},
      {type: 'MessageOutlined', text: '2'},
    ];


    const currentSelected = ref();

    const handleClick = (value: any) => {
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
      ebooks,
      pagination,
      actions,
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
