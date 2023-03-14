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
          <a-form-item>
            <a-button type="primary" @click="add()">
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>

      <a-table
          :columns="columns"
          :row-key="(record,index)=> index"
          :data-source="treeData"
          :loading="loading"
          :pagination="pagination"
          @change="handleTableChange"
      >

        <template v-slot:curedState="{text, record}">
          <a-tag color="#228B22" v-show="record.curedState == 1">已无风险</a-tag>
          <a-tag color="#B22222" v-show="record.curedState == 0">仍有风险</a-tag>
        </template>

        <template v-slot:action="{text, record}">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="确认删除？"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.caseId)" >
              <a-button type="danger" >
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>

      </a-table>


    </a-layout-content>
  </a-layout>


  <a-modal
      title="新增确诊病例"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOK"
  >
  <a-form :model="current_case" :label-col="{span: 6}" :wrapper-col="{span: 18}">
    <a-form-item label="确诊地名" :style="{width: '100%'}">
      <a-select
          v-model:value="selectedAddress"
          show-search
          placeholder="选择确诊地名"
          :default-active-first-option="false"
          :show-arrow="false"
          :filter-option="false"
          :not-found-content="null"
          :options="selectData"
          @search="handleSearch"
          @change="handleChange"
          :style="{width: '100%'}"
      >
      </a-select>

    </a-form-item>
    <a-form-item label="确诊时间">
      <a-date-picker show-time placeholder="选择确诊时间"
                     v-model:value="current_case.diagnosedDate" :style="{width: '100%'}"
                     format="YYYY-MM-DD HH:mm:ss"/>
    </a-form-item>


    <a-form-item label="是否已无风险">
      <a-select ref="select"
                v-model:value="current_case.curedState">
        <a-select-option v-for="c in riskLevels"
                         :key="c.riskLevel"
                         :value="c.riskLevel">
          {{ c.content }}
        </a-select-option>
      </a-select>
    </a-form-item>
    

  </a-form>
  </a-modal>


</template>


<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios'
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool"
import store from "@/store";
import moment from "moment-timezone";


export default defineComponent({
  name: 'AdminDiagnosedCaseManagement',
  setup() {

    const pagination = ref({
      current: 1,
      pageSize: 7,
      total: 0
    });

    const param = ref();
    param.value = {};
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
        title: '确诊时间',
        key: 'diagnosedDate',
        dataIndex: 'diagnosedDate',
        align: 'center',
      },
      {
        title: '是否已无风险',
        slots: {customRender: 'curedState'},
        align: 'center',
      },
      {
        title: '操作',
        key: 'action',
        slots: {customRender: 'action'},
        align: 'center'
      }
    ];


    const treeData = ref();

    // param.value = "";

    // 数据查询
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/diagnosed-case/all", {
        params: {
          page: params.page,
          size: params.size,
          identity: param.value.identity,
        },
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        loading.value = false;
        const data = response.data;

        if (data.success) {
          console.log("data: ", data.content.list);
          treeData.value = data.content.list;
        } else {
          message.error(data.message);
        }
      });
    };

    const riskLevels = [{
      "riskLevel": 0,
      "content": "仍有风险",
    },{
      "riskLevel": 1,
      "content": "已无风险",
    }];

    
    const selectData = ref<any[]>([]);
    const selectedAddress = ref();
    let timeout: any;
    let currentValue = '';

    function fetch(value: string, callback: any) {
      if (timeout) {
        clearTimeout(timeout);
        timeout = null;
      }
      currentValue = value;

      function fake() {
        axios.get("https://restapi.amap.com/v5/place/text", {
          params: {
            key: 'f6f43d7b8324f2f24402c5fd201ab78b',
            keywords: value,
          }
        }).then((response) => {
          if (currentValue === value) {
            const result = response.data.pois;
            const selectData: any[] = [];
            for (let i = 0; i < result.length; i++) {
              selectData.push({
                value: result[i].name,
                label: result[i].name,
              })
            }
            console.log("selectData processed", selectData);
            callback(selectData);
          }
        });
      }

      timeout = setTimeout(fake, 300);
    }

    const handleSearch = (val: string) => {
      fetch(val, (d: any[]) => (selectData.value = d));
    };
    const handleChange = (val: string) => {
      selectedAddress.value = val;
      fetch(val, (d: any[]) => (selectData.value = d));
    };

    const current_case = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    // 编辑
    const edit = (record: any) => {
      modalVisible.value = true;
      current_case.value = Tool.copy(record);
      selectedAddress.value = record.addressName;
    }

    // 新增
    const add = () => {
      modalVisible.value = true;
      current_case.value = {};
      selectedAddress.value = "";
    }


    const handleModalOK = () => {
      // 首先根据当前选中的地区详情查询具体地址信息
      axios.get("https://restapi.amap.com/v5/place/text", {
        params: {
          key: 'f6f43d7b8324f2f24402c5fd201ab78b',
          keywords: selectedAddress.value,
        }
      }).then((response) => {
        const result = response.data.pois;
        console.log("result: ", result);
        const poi = result[0];
        console.log("POI: ", poi);

        console.log("POI location: ", poi.location);

        let position = poi.location;
        console.log("position: ", position);
        let coordinate = position.split(',');

        const address = {
          addressName: poi.name,
          areaName: poi.adname,
          cityName: poi.cityname,
          longitude: coordinate[0],
          latitude: coordinate[1],
          addressPosition: poi.address,
        }
        current_case.value.address = address;
        postData();
      });

      modalLoading.value = true;
    };

    const postData = () => {

      let moment = require('moment-timezone');
      let tz = 'Asia/Shanghai'  //时区
      current_case.value.diagnosedDate = moment.utc(current_case.value.diagnosedDate).tz(tz).format('YYYY-MM-DD HH:mm:ss');

      const diagnosedCase = {
        caseId: current_case.value.caseId,
        diagnosedDate: current_case.value.diagnosedDate,
        curedState: current_case.value.curedState,
      }

      console.log("diagnosedCase", diagnosedCase);

      current_case.value.diagnosedCase = diagnosedCase;

      axios.post("/diagnosed-case/insert", current_case.value,{
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

    const handleDelete = (id: number) => {
      axios.delete("/diagnosed-case/delete/" + id,{
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          // 重新加载列表
          handleQuery({
                // 此处的属性名需要和后端对应
                page: pagination.value.current,
                size: pagination.value.pageSize,
              });
        }
      })
    }

    onMounted(() => {
      handleQuery({
        // 此处的属性名需要和后端对应
        page: 1,
        size: pagination.value.pageSize,
      });
    });

    return {
      param,
      columns,
      loading,
      pagination,
      selectData,
      selectedAddress,
      modalVisible,
      modalLoading,
      current_case,


      treeData,
      riskLevels,
      handleQuery,
      handleSearch,
      handleChange,
      add,
      edit,
      handleModalOK,
      handleDelete,
    }
  }
});
</script>
