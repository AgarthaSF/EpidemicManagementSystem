<template>
  <a-layout>

    <a-layout-content
        :style="{ background: '#fff', padding: '10px', margin: 0, minHeight: '280px' }">

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
          :row-key="record => record.infoId"
          :data-source="users"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >


        <template v-slot:type="{text, record}">
          <a-tag v-show="record.type==='student'">学生</a-tag>
          <a-tag v-show="record.type==='teacher'">教工</a-tag>
        </template>

        <template v-slot:travel="{text, record}">
          <a-tag color="#f50" v-for="c in record.cityList" :key="c" :value="c">
            {{ c }}
          </a-tag>
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
  name: 'StaffTravelAnomaly',
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
        title: '人员类型',
        slots: {customRender: 'type'},
        align: 'center'
      },
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
        title: '手机号码',
        dataIndex: 'phoneNumber',
        align: 'center'
      },
      {
        title: '旅居风险地区',
        slots: {customRender: 'travel'},
        align: 'center'
      },
    ];

    const param = ref();
    param.value = {};
    param.value.identity = "";

    // 数据查询
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/travel-info/anomaly/school", {
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
    const current_upload = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      })
    });

    return {
      users,
      pagination,
      columns,
      loading,
      param,

      modalVisible,
      modalLoading,
      current_upload,

      handleTableChange,
      handleQuery,
    }
  }
});
</script>
