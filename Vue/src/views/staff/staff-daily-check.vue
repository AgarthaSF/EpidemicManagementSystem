<template>
  <a-layout>

    <a-layout-content
        :style="{ background: '#fff', padding: '10px', margin: 0, minHeight: '280px' }">

      <p>
        <a-form layout="inline" :style="{float: 'right', marginRight: '85px', marginBottom: '15px'}">
          <a-button type="primary" @click="add()">
            新增
          </a-button>
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
          <a-tag color="#f50" v-show="record.consultationState===1">是</a-tag>
          <a-tag v-show="record.consultationState===0">否</a-tag>
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

  <a-modal
      title="每日打卡"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOK"
  >
    <a-form :model="current_check" :label-col="{span: 6}" :wrapper-col="{span: 18}">

      <a-form-item label="当前体温">
        <a-input v-model:value="current_check.bodyTemperature"></a-input>
      </a-form-item>

      <a-form-item label="是否发烧">
        <a-radio-group name="radioGroup" v-model:value="current_check.feverState">
          <a-radio value=1 :style="{marginLeft: '80px'}">是</a-radio>
          <a-radio value=0 :style="{marginLeft: '80px'}">否</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="住院隔离">
        <a-radio-group name="radioGroup" v-model:value="current_check.consultationState">
          <a-radio value=1 :style="{marginLeft: '80px'}">是</a-radio>
          <a-radio value=0 :style="{marginLeft: '80px'}">否</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="到达高风险地区">
        <a-radio-group name="radioGroup" v-model:value="current_check.highRiskAccess">
          <a-radio value=1 :style="{marginLeft: '80px'}">是</a-radio>
          <a-radio value=0 :style="{marginLeft: '80px'}">否</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="为高风险人员">
        <a-radio-group name="radioGroup" v-model:value="current_check.highRiskMember">
          <a-radio value=1 :style="{marginLeft: '80px'}">是</a-radio>
          <a-radio value=0 :style="{marginLeft: '80px'}">否</a-radio>
        </a-radio-group>
      </a-form-item>


      <a-form-item label="接触高风险人员">
        <a-radio-group name="radioGroup" v-model:value="current_check.contactHighRiskPeople">
          <a-radio value=1 :style="{marginLeft: '80px'}">是</a-radio>
          <a-radio value=0 :style="{marginLeft: '80px'}">否</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="离校学习">
        <a-radio-group name="radioGroup" v-model:value="current_check.remoteStudy">
          <a-radio value=1 :style="{marginLeft: '80px'}">是</a-radio>
          <a-radio value=0 :style="{marginLeft: '80px'}">否</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="供应充足">
        <a-radio-group name="radioGroup" v-model:value="current_check.supplyEnough">
          <a-radio value=1 :style="{marginLeft: '80px'}">是</a-radio>
          <a-radio value=0 :style="{marginLeft: '80px'}">否</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="口罩数量">
        <a-input v-model:value="current_check.maskNumber"></a-input>
      </a-form-item>


    </a-form>
  </a-modal>


</template>

<script lang="ts">
import {computed, defineComponent, onMounted, ref} from 'vue';
import axios from 'axios'
import {message} from 'ant-design-vue';
import store from "@/store";
import moment from 'moment';

export default defineComponent({
  name: 'StaffDailyCheck',
  setup() {
    const users = ref();
    const loading = ref(false);

    const pagination = ref({
      current: 1,
      pageSize: 8,
      total: 0
    });


    const columns = [
      {
        title: '打卡编号',
        dataIndex: 'checkId',
        align: 'center'
      },
      {
        title: '体温',
        slots: {customRender: 'temperature'},
        align: 'center'
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
      axios.get("/daily-check/list", {
        params: {
          page: params.page,
          size: params.size,
          identity: store.getters.getUser().identity,
        },
        headers:{
          'type': store.getters.getUser().type}
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

    const postData = () => {
      let userIdentity = store.getters.getUser().identity;
      current_check.value.identity = userIdentity;
      current_check.value.checkDate = moment().locale('zh-cn').format('YYYY-MM-DD HH:mm:ss');

      axios.post("/daily-check/save", current_check.value, {
        headers:{
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        const data = response.data;
        modalLoading.value = false;
        if (data.success) {
          modalVisible.value = false;
          handleQuery({
            // 此处的属性名需要和后端对应
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    }


    const handleModalOK = () => {
      modalLoading.value = true;
      postData();
    };

    // 新增
    const add = () => {
      modalVisible.value = true;
      current_check.value = {};
    }

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

      modalVisible,
      modalLoading,
      current_check,

      handleModalOK,
      handleTableChange,
      add,
      handleQuery,
    }
  }
});
</script>
