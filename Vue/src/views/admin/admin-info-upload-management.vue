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

      </a-table>

    </a-layout-content>
  </a-layout>

  <a-modal
      title="信息上报"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOK"
  >
    <a-form :model="current_upload" :label-col="{span: 6}" :wrapper-col="{span: 18}">

      <a-form-item label="上次核酸时间">
        <a-date-picker show-time placeholder="选择上次核酸时间"
                       v-model:value="current_upload.lastCovidDate" :style="{width: '100%'}" format="YYYY-MM-DD HH:mm:ss"/>
      </a-form-item>

      <a-form-item label="疫苗接种次数">
        <a-input v-model:value="current_upload.vaccineCount"></a-input>
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
  name: 'AdminInfoUploadManagement',
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
        dataIndex: 'infoId',
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
        title: '疫苗接种数量',
        dataIndex: 'vaccineCount',
        align: 'center'
      },
      {
        title: '上次核酸检测日期',
        dataIndex: 'lastCovidDate',
        align: 'center'
      },
      {
        title: '上报日期',
        dataIndex: 'infoDate',
        align: 'center'
      },

    ];

    const param = ref();
    param.value = {};
    param.value.identity = "";

    // 数据查询
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/info-upload/all", {
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
