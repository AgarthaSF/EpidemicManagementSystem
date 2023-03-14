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

        <template v-slot:curedState="{text, record}">
          <a-tag color="#228B22" v-show="record.curedState == 1">已无风险</a-tag>
          <a-tag color="#B22222" v-show="record.curedState == 0">仍有风险</a-tag>
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
  name: 'AdminSurveyDetail',
  setup() {

    const pagination = ref({
      current: 1,
      pageSize: 7,
      total: 0
    });

    const loading = ref(false);
    const columns = [
      {
        title: '确诊编号',
        key: 'caseId',
        dataIndex: 'caseId',
        align: 'center',
      },
      {
        title: '确诊地名',
        key: 'addressName',
        dataIndex: 'addressName',
        align: 'center',
      },
      {
        title: '确诊位置',
        key: 'addressPosition',
        dataIndex: 'addressPosition',
        align: 'center',
      },
      {
        title: '途经时间',
        key: 'arrDate',
        dataIndex: 'arrDate',
        align: 'center',
      },
      {
        title: '是否已无风险',
        slots: {customRender: 'curedState'},
        align: 'center',
      },
    ];

    const param = ref();
    param.value = {};
    param.value.identity = "";
    const treeData = ref();

    // 数据查询
    const handleQuery = () => {
      loading.value = true;
      axios.get("/diagnosed-case/detail", {
        params: {
          page: 1,
          size: 1000,
          identity: param.value.identity,
        },
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          console.log("data: ", data.content);
          treeData.value = data.content;
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
      pagination,

      treeData,
      handleQuery,
    }
  }
});
</script>
