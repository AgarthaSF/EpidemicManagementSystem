<template>

  <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">


    <a-table
        :columns="columns"
        :data-source="renderData"
        :row-key="record => record.title"
        :pagination="false"
        :loading="loading"
        size="small"/>
  </a-layout-content>


</template>

<script lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";

export default {
  name: "quick-info",

  setup() {
    const loading = ref(false);
    const renderData = ref();

    const columns = [
      {
        title: '数据类型',
        dataIndex: 'title',
        align: 'center',
        width: 140,
      },
      {
        title: '数量',
        dataIndex: 'content',
        align: 'center',
        padding: 140,
      },
    ];


    const queryData = () => {
      loading.value = true;
      axios.get("http://api.tianapi.com/ncov/index?key=536d11468d6b491fd6fcdb594a9a5e7d").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.code == 200) {
          const desc = data.newslist[0].desc;


          console.log("data: ", data);
          console.log("新增死亡人数", desc.deadIncr);

          let confirmedIncr = desc.currentConfirmedIncr == null ? "暂无数据" : desc.currentConfirmedIncr;
          let curedIncr = desc.curedIncr == null ? "暂无数据" : desc.curedIncr;
          let deadIncr = desc.deadIncr == null ? "暂无数据" : desc.deadIncr;

          renderData.value = [
            {
              title: "现存确诊人数",
              content: desc.currentConfirmedCount,
            },
            {
              title: "现存无症状人数",
              content: desc.seriousCount,
            },
            {
              title: "现存确诊人数趋势",
              content: confirmedIncr,
              // content: desc.currentConfirmedIncr,
            },
            {
              title: "新增治愈人数",
              content: curedIncr,
              // content: desc.curedIncr,
            },
            {
              title: "新增死亡人数",
              content: deadIncr,
              // content: desc.deadIncr,
            },
            {
              title: "累计确诊人数",
              content: desc.confirmedCount,
            },
            {
              title: "累计治愈人数",
              content: desc.curedCount,
            },
            {
              title: "累计死亡人数",
              content: desc.deadCount,
            },
            {
              title: "中风险地区个数",
              content: desc.midDangerCount,
            },
            {
              title: "高风险地区个数",
              content: desc.highDangerCount,
            },
          ];
          console.log("desc: ", desc);

        } else {
          message.error(data.message);
        }
      })
    }

    onMounted(() => {
      queryData();
    })

    return {
      renderData,
      columns,
      loading,
    }

  }
}
</script>

<style scoped>

</style>