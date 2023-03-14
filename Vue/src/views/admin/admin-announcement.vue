<template>


  <a-layout>
    <!--    表格-->
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">


      <a-row :gutter="24">
        <a-col :span="10">

          <!--      查询与新增-->
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item :style="{width: '60%'}">
                <a-input v-model:value="param.announcementId" placeholder="名称">
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
              :row-key="record => record.announcementId"
              :data-source="announcements"
              :pagination="pagination"
              :loading="loading"
              @change="handleTableChange"
              size="small"
          >
            <template v-slot:action="{text, record}">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  编辑
                </a-button>
                <a-popconfirm
                    title="确认删除？"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="handleDelete(record.announcementId)">
                  <a-button type="danger" size="small">
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>

        <a-col :span="14">
          <a-form :model="current_announcement">
            <a-form-item label="标题">
              <a-input v-model:value="current_announcement.title"/>
            </a-form-item>
            <a-form-item label="内容">
              <div id="content"></div>
            </a-form-item>
          </a-form>

          <p>
            <a-form layout="inline"  :style="{float: 'right'}" size="large">
              <a-form-item :style="{float: 'left'}">
                当前状态：
                <a-tag size="large">
                  {{currentState}}
                </a-tag>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="handleSave">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>


        </a-col>
      </a-row>


    </a-layout-content>
  </a-layout>
</template>


<script lang="ts">

import E from 'wangeditor'
import {onMounted, ref} from "vue";
import {Tool} from "@/util/tool";
import axios from "axios";
import {message} from "ant-design-vue";
import {createEditor, createToolbar, IEditorConfig, IToolbarConfig} from '@wangeditor/editor';
import store from "@/store";

export default {
  name: "admin-announcement",

  setup() {
    const param = ref();
    param.value = {};
    const announcements = ref();
    const loading = ref(false);
    const currentState = ref();

    const pagination = ref({
      current: 1,
      pageSize: 7,
      total: 0
    });

    const columns = [
      {
        title: '编号',
        dataIndex: 'announcementId',
      },
      {
        title: '标题',
        dataIndex: 'title',
      },
      {
        title: '发布日期',
        dataIndex: 'annoDate'
      },
      {
        title: '操作',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    // 表单
    const current_announcement = ref();
    current_announcement.value = {
      annoDate: "",
      title: "",
    }
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    // 新增
    const add = () => {
      editor.txt.html("");
      modalVisible.value = true;
      current_announcement.value = {};
      currentState.value = "新增";
    }

    // 编辑
    const edit = (record: any) => {
      modalVisible.value = true;
      current_announcement.value = Tool.copy(record);
      editor.txt.html(current_announcement.value.content);
      currentState.value = "编辑";

    }

    // 数据查询
    const handleQuery = (params: any) => {
      loading.value = true;
      console.log("params: ", params);
      axios.get("/announcement/list", {
        params: {
          page: params.page,
          size: params.size,
          announcementId: param.value.announcementId
        },
        headers:{
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          announcements.value = data.content.list;

          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;

          // 如果查到的数据条数为0，且当前页数不为1，则说明删除完数据后该页面为空，将页面置为当前页的上一页
          if (announcements.value.length == 0 && params.page != 1) {
            handleQuery({
              page: params.page - 1,
              size: pagination.value.pageSize,
            })
          }
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

    const editor = new E('#content');
    const initEditor = () => {
      editor.config.menus = [
        'head',
        'bold',
        'fontSize',
        'fontName',
        'italic',
        'underline',
        'strikeThrough',
        'indent',
        'lineHeight',
        'foreColor',
        'backColor',
        'link',
        'list',
        'todo',
        'justify',
        'quote',
        'table',
        'code',
        'splitLine',
        'undo',
        'redo',
      ]
      editor.config.zIndex = 0;
      editor.config.height = 380;
      editor.create();
    }

    const handleSave = () => {

      current_announcement.value.content = editor.txt.html();
      console.log("content: ", current_announcement.value.content);

      axios.post("/announcement/save", current_announcement.value,{
        headers:{
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          // 重新加载列表
          handleQuery({
            // 此处的属性名需要和后端对应
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
          current_announcement.value = {
            announcementId: "",
            annoDate: "",
            title: "",
            content: "",
          }
          editor.txt.html("");
        } else {
          message.error(data.message);
        }
      });
    };

    const handleDelete = (id: number) => {
      axios.delete("/announcement/delete/" + id,{
        headers:{
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          // 重新加载列表
          handleQuery({
            // 此处的属性名需要和后端对应
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
          add();
        }
      })
    }

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      })

      initEditor();
      add();
    })

    return {
      columns,
      param,
      announcements,
      loading,
      pagination,
      current_announcement,
      modalLoading,
      modalVisible,
      currentState,


      add,
      edit,
      handleQuery,
      handleTableChange,
      handleSave,
      handleDelete,
    }


  }
}
</script>

<style scoped>

</style>