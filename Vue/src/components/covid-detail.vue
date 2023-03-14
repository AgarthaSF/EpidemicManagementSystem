<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">

      <p>
        <a-form layout="inline" :model="param">
          <a-form-item :style="{width: '80%'}">
            <a-input v-model:value="param.identity" placeholder="名称">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery()">
              查询
            </a-button>
          </a-form-item>
        </a-form>
      </p>


      <a-table
          :columns="columns"
          :row-key="(record,index)=> index"
          :data-source="treeData"
          :loading="loading"
          :pagination="false">

        <template v-slot:riskLevel="{text, record}">
          <a-tag color="#B22222" v-show="record.riskLevel == 3">高风险地区</a-tag>
          <a-tag color="#FF8000" v-show="record.riskLevel == 2">中风险地区</a-tag>
          <a-tag color="#228B22" v-show="record.riskLevel == 1">低风险地区</a-tag>
        </template>


      </a-table>

    </a-layout-content>
  </a-layout>


</template>


<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios'
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool"
import store from "@/store";


export default defineComponent({
  name: 'CovidDetail',
  setup() {
    const param = ref();
    param.value = {};
    const loading = ref(false);

    const pagination = ref({
      current: 1,
      pageSize: 7,
      total: 0
    });

    const columns = [
      {
        title: '行政区划代码',
        key: 'adcode',
        dataIndex: 'adcode',
        align: 'center',
      },
      {
        title: '名称',
        key: 'name',
        dataIndex: 'name',
        align: 'center',
      },
      {
        title: '当前确诊人数',
        key: 'currentConfirmedCount',
        dataIndex: 'currentConfirmedCount',
        align: 'center',
      },
      {
        title: '当前疑似人数',
        key: 'suspectedCount',
        dataIndex: 'suspectedCount',
        align: 'center',
      },
      {
        title: '当前风险等级',
        slots: {customRender: 'riskLevel'},
        align: 'center',
      },
    ];


    // 表单
    const level1 = ref();

    const treeData = ref();

    // 数据查询
    const handleQuery = () => {
      loading.value = true;
      axios.get("/district-epidemic/list", {
        params: {
          page: 1,
          size: 1000,
          identity: param.value.identity
        },
        headers:{
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        loading.value = false;

        const data = response.data;
        console.log("raw: ", data);

        if (data.success) {
          treeData.value = data.content;
          console.log("Tree data: ", treeData.value);
        } else {
          message.error(data.message);
        }
      });
    };


    onMounted(() => {
      handleQuery();
    });

    return {
      param,
      columns,
      loading,


      treeData,
      pagination,

      handleQuery,
    }
  }
});
</script>
