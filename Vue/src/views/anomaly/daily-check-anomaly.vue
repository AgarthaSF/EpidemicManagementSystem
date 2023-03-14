<template>
  <a-layout>

    <a-layout-content
        :style="{ background: '#fff', padding: '20px', margin: 0, minHeight: '280px' }">

      <p>
        <a-form layout="inline" :model="param">
          <a-form-item :style="{width: '90%'}">
            <a-input v-model:value="param.identity" placeholder="名称">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
              查询
            </a-button>
          </a-form-item>
        </a-form>
      </p>

      <a-table
          :columns="columns"
          :row-key="record => record.checkId"
          :data-source="users"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >

        <template v-slot:temperature="{text, record}">
          <a-tag color="#f50" v-show="record.bodyTemperature > 37.2">{{record.bodyTemperature}}</a-tag>
          <a-tag v-show="record.bodyTemperature <= 37.2">{{record.bodyTemperature}}</a-tag>
        </template>


        <template v-slot:fever="{text, record}">
          <a-tag color="#f50" v-show="record.feverState===1">是</a-tag>
          <a-tag v-show="record.feverState===0">否</a-tag>
        </template>

        <template v-slot:consultation="{text, record}">
          <a-tag>{{ record.consultationState === 1 ? "是" : "否" }}</a-tag>
        </template>

        <template v-slot:access="{text, record}">
          <a-tag color="#f50" v-show="record.highRiskAccess===1">是</a-tag>
          <a-tag v-show="record.highRiskAccess===0">否</a-tag>
        </template>

        <template v-slot:member="{text, record}">
          <a-tag color="#f50" v-show="record.highRiskMember===1">是</a-tag>
          <a-tag v-show="record.highRiskMember===0">否</a-tag>
        </template>

        <template v-slot:contact="{text, record}">
          <a-tag color="#f50" v-show="record.contactHighRiskPeople===1">是</a-tag>
          <a-tag v-show="record.contactHighRiskPeople===0">否</a-tag>
        </template>

        <template v-slot:remote="{text, record}">
          <a-tag>{{ record.remoteStudy === 1 ? "是" : "否" }}</a-tag>
        </template>

        <template v-slot:supply="{text, record}">
          <a-tag color="#f50" v-show="record.supplyEnough===0">否</a-tag>
          <a-tag v-show="record.supplyEnough===1">是</a-tag>
        </template>

      </a-table>

    </a-layout-content>
  </a-layout>

</template>

<script lang="ts">
import {computed, defineComponent, onMounted, ref} from 'vue';
import axios from 'axios'
import {message} from 'ant-design-vue';
import store from "@/store";
import moment from 'moment';

export default defineComponent({
  name: 'DailyCheckAnomaly',
  setup() {
    const users = ref();
    const loading = ref(false);

    const pagination = ref({
      current: 1,
      pageSize: 8,
      total: 0
    });

    const param = ref();
    param.value = {};
    param.value.identity="";

    const columns = [
      {
        title: '人员编号',
        dataIndex: 'identity',
        align: 'center'
      },
      {
        title: '姓名',
        dataIndex: 'name',
        align: 'center'
      },
      {
        title: '号码',
        dataIndex: 'phoneNumber',
        align: 'center'
      },
      {
        title: '体温',
        slots: {customRender: 'temperature'},
        align: 'center',
      },
      {
        title: '是否发烧',
        slots: {customRender: 'fever'},
        align: 'center',
      },
      {
        title: '住院隔离',
        slots: {customRender: 'consultation'},
        align: 'center'
      },
      {
        title: '访问高风险地区',
        slots: {customRender: 'access'},
        align: 'center'
      },
      {
        title: '高风险地区人员',
        slots: {customRender: 'member'},
        align: 'center'
      },
      {
        title: '接触高风险人员',
        slots: {customRender: 'contact'},
        align: 'center'
      },
      {
        title: '离校学习',
        slots: {customRender: 'remote'},
        align: 'center'
      },
      {
        title: '供应充足',
        slots: {customRender: 'supply'},
        align: 'center'
      },
      {
        title: '口罩数量',
        dataIndex: 'maskNumber',
        align: 'center'
      },
      {
        title: '打卡日期',
        dataIndex: 'checkDate',
        align: 'center'
      },

    ];

    const user = computed(() => store.state.user);


    // 数据查询
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/daily-check/anomaly", {
        params: {
          page: params.page,
          size: params.size,
          identity: param.value.identity,
        },
        headers:{
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          users.value = data.content.list;
          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }
      });
    };

    // 表格点击页码时触发
    const handleTableChange = (pagination: any) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize,
      });
    };

    // 表单
    const current_check = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      })
    });

    return {
      user,
      users,
      pagination,
      columns,
      loading,
      param,

      modalVisible,
      modalLoading,
      current_check,
      handleTableChange,
      handleQuery,
    }
  }
});
</script>
