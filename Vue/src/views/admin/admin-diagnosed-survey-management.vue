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
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
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
          @change="handleTableChange">


        <template v-slot:action="{text, record}">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="确认删除？"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.surveyId)" >
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
      title="新增流调信息"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOK"
  >
  <a-form :model="current_survey" :label-col="{span: 6}" :wrapper-col="{span: 18}">


    <a-form-item label="所属确诊病例" :style="{width: '100%'}">
      <a-select
          v-model:value="current_survey.caseId"
          show-search
          placeholder="选择确诊编号"
          :options="options"
          :style="{width: '100%'}"
      >
      </a-select>
    </a-form-item>


    <a-form-item label="确诊地名" :style="{width: '100%'}">


      <a-select
          v-model:value="selectedAddress"
          show-search
          placeholder="选择途径地名"
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
    <a-form-item label="途径时间">
      <a-date-picker show-time placeholder="选择确诊时间"
                     v-model:value="current_survey.arrDate" :style="{width: '100%'}"
                     format="YYYY-MM-DD HH:mm:ss"/>
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
import {SelectTypes} from "ant-design-vue/es/select";


export default defineComponent({
  name: 'AdminDiagnosedSurveyManagement',
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
        title: '流调编号',
        key: 'surveyId',
        dataIndex: 'surveyId',
        align: 'center',
      },
      {
        title: '确诊编号',
        key: 'caseId',
        dataIndex: 'caseId',
        align: 'center',
      },
      {
        title: '途径地名',
        key: 'addressName',
        dataIndex: 'addressName',
        align: 'center',
      },
      {
        title: '途径位置',
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
        title: '操作',
        key: 'action',
        slots: {customRender: 'action'},
        align: 'center'
      }
    ];


    const treeData = ref();

    // 数据查询
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/diagnosed-survey/all", {
        params: {
          page: params.page,
          size: params.size,
          identity: param.value.identity
        },
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        loading.value = false;
        const data = response.data;

        if (data.success) {
          console.log("data: ", data.content);
          treeData.value = data.content.list;

          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;

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

    const current_survey = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    // 编辑
    const edit = (record: any) => {
      modalVisible.value = true;
      current_survey.value = Tool.copy(record);
      selectedAddress.value = record.addressName;
    }

    // 新增
    const add = () => {
      modalVisible.value = true;
      current_survey.value = {};
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
        current_survey.value.address = address;
        postData();
      });

      modalLoading.value = true;
    };

    const postData = () => {

      let moment = require('moment-timezone');
      let tz = 'Asia/Shanghai'  //时区
      current_survey.value.arrDate = moment.utc(current_survey.value.arrDate).tz(tz).format('YYYY-MM-DD HH:mm:ss');

      const diagnosedSurvey = {
        surveyId: current_survey.value.surveyId,
        caseId: current_survey.value.caseId,
        arrDate: current_survey.value.arrDate,
      }

      current_survey.value.diagnosedSurvey = diagnosedSurvey;

      axios.post("/diagnosed-survey/insert", current_survey.value,{
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
      axios.delete("/diagnosed-survey/delete/" + id,{
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

    const handleTableChange = (pagination: any) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize,
      });
    };

    const options = ref();
    options.value = [];
    // { value: 'jack', label: 'Jack' },

    const getCaseId = () =>{
      axios.get("/diagnosed-case/getIds",{
        headers:{
          'type': store.getters.getUser().type}
      }).then((response)=>{
        const data = response.data;
        if(data.success){
          const list = data.content;
          for(let i = 0 ; i < list.length; i++){
            options.value.push({
              value: list[i],
              label: list[i],
            })
          }


          console.log("options:", options.value);

        }else{
          message.error(data.message);
        }
      })
    }




    onMounted(() => {
      handleQuery({
        // 此处的属性名需要和后端对应
        page: 1,
        size: pagination.value.pageSize,
      });
      getCaseId();
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
      current_survey,
      options,

      treeData,
      riskLevels,
      handleQuery,
      handleSearch,
      handleChange,
      add,
      edit,
      handleModalOK,
      handleDelete,
      handleTableChange,
    }
  }
});
</script>
