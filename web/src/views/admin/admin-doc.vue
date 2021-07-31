<template>


  <a-layout class="ant-layout-has-sider ant-layout">

    <a-layout-content style="background:#fff;padding:24px;margin: 0;minHeight:280px" class="ant-layout-content">
      <a-row :gutter="24">
        <a-col :span="8">
          <p>
            <a-form :model="param" layout="inline">
              <a-form-item>
                <a-button type="primary" @click="handleQuery()">
                  查询
                </a-button>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="add">
                  新增
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-table
              v-if="level1.length > 0"
              :columns="columns"
              :data-source="level1"
              :loading="loading"
              :pagination="false"
              size="small"
              :row-key="record => record.id"
              @change="handleTableChange"
              :defaultExpandAllRows="true"
          >
            <template #name="{ text, record }">
              {{ record.sort }} {{ text }}
            </template>
            <template v-slot:action="{ text, record }">
              <a-space size="small">
                <a-button size="small" type="primary" @click="edit(record)">
                  编辑
                </a-button>
                <a-popconfirm
                    cancel-text="否 "
                    ok-text="是"
                    title="删除后不可恢复，确认删除？"
                    @confirm="showConfirm(record.id)"
                >
                  <a-button size="small" type="danger">
                    删除
                  </a-button>
                </a-popconfirm>

              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form :model="param" layout="inline">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :label-col="{ span: 6 }" :model="doc" layout="vertical">
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="名称"/>
            </a-form-item>
            <a-form-item>
              <a-tree-select
                  v-model:value="doc.parent"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :replaceFields="{title: 'name', key: 'id', value: 'id'}"
                  :tree-data="treeSelectData"
                  placeholder="请选择父文档"
                  style="width: 100%"
                  tree-default-expand-all
              >
              </a-tree-select>
            </a-form-item>
<!--            <a-form-item label="父文档">-->
<!--              <a-select-->
<!--                  ref="select"-->
<!--                  v-model:value="doc.parent"-->
<!--              >-->
<!--                <a-select-option value="0">-->
<!--                  无-->
<!--                </a-select-option>-->
<!--                <a-select-option v-for="c in level1" :key="c.id" :disabled="doc.id === c.id" :value="c.id">-->
<!--                  {{ c.name }}-->
<!--                </a-select-option>-->
<!--              </a-select>-->
<!--            </a-form-item>-->
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="顺序"/>
            </a-form-item>

            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent()">
                <EyeOutlined/>
                内容预览
              </a-button>
            </a-form-item>

            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>

      <a-drawer :closable="false" :visible="drawerVisible" placement="right" width="900" @close="onDrawerClose">
        <div :innerHTML="previewHtml" class="wangeditor"></div>
      </a-drawer>

    </a-layout-content>

  </a-layout>

  <!--  <a-modal-->
  <!--      v-model:visible="modalVisible"-->
  <!--      :confirm-loading="modalLoading"-->
  <!--      title="文档表单"-->
  <!--      @ok="handleModalOK"-->
  <!--  >-->
  <!--    -->
  <!--  </a-modal>-->

</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';
import {message, Modal} from 'ant-design-vue'
import {Tool} from '@/util/tool';
import {useRoute} from "vue-router";
import E from 'wangeditor';

export default defineComponent({
  name: 'Admindoc',
  setup() {
    const route = useRoute();
    console.log('路由：', route);
    console.log('route.path: ', route.path);
    console.log('route.query: ', route.query);
    console.log('route.params: ', route.params);
    console.log('route.fullPath: ', route.fullPath);
    console.log('route.name: ', route.name);
    console.log('route.meta: ', route.meta);

    const param = ref();
    param.value = {};

    const docs = ref();
    const loading = ref(false);
    // 因为树选择组件的树形状态，会随当前编辑的节点变化，所以单独声明一个响应式变量
    const treeSelectData = ref();
    treeSelectData.value = [];
    /**
     * 一级文档，children 属性是二级文档
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     **/
    const level1 = ref();

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        slots: {customRender: 'name'}
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    level1.value = [];
    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      level1.value = [];
      axios.get("/doc/all/" + route.query.ebookId).then((response) => {
        loading.value = false;
        const data = response.data;
        if(data.success){
          docs.value = data.content;
          console.log("原始数组：", doc.value);
          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          console.log("树形结构：", level1);

          // 父文档下拉框初始化，相当于点击新增
          treeSelectData.value = Tool.copy(level1.value);
          treeSelectData.value.unshift({id: 0, name: '无'});
        }else {
          message.error(data.message);
        }

      });
    };



    //表单
    const doc = ref();
    doc.value = {
      ebookId: route.query.ebookId
    };
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    // 因为树选择组件的树形状态，会随当前编辑的节点变化，所以单独声明一个响应式变量
    let editor: any;

    const handleSave = () => {
      modalLoading.value = true;
      doc.value.content = editor.txt.html();
      axios.post("/doc/save", doc.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data => CommonResp
        if (data.success) {
          // modalVisible.value = false;
          message.success("保存成功！");
          // 重新加载列表

          handleQuery({});
        }else {
          message.error(data.message);
        }
      });
    }

    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log('disable', node);
          // 将目标节点设置为 disable
          node.disabled = true;
          // 遍历所有子节点，将所有子节点全部加上 disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j  = 0; j < children.length; j++) {
              setDisable(children, children[j].id);
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    }

    const ids: Array<string> = [];
    const names: Array<string> = [];
    /**
     * 查找整根树枝
     */
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log('disable', node);
          // 将目标 id 放入结果集 ids 中
          // node.disabled = true;
          ids.push(id);
          names.push(node.name);
          // 遍历所有子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id);
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    }

    /**
     * 内容查询
     **/
    const handleQueryContent = () => {
      axios.get("/doc/find-content/" + doc.value.id).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          editor.txt.html(data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    // 编辑
    const edit = (record: any) => {
      // 清空富文本框
      editor.txt.html('');

      modalVisible.value = true;
      doc.value = Tool.copy(record);

      handleQueryContent();
      // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);
      // 为选择树添加一个”无“
      treeSelectData.value.unshift({id:0, name: '无'});
    }

    //添加
    const add = () => {
      // 清空富文本框
      editor.txt.html('');
      modalVisible.value = true;
      doc.value = {
        ebookId: route.query.ebookId
      };

      treeSelectData.value = Tool.copy(level1.value);
      // 为选择树添加一个“无”
      treeSelectData.value.unshift({id:0, name:'无'});
    }

    //删除
    const handleDelete = (id: number) => {
      ids.length = 0;
      console.log('ids', ids);
      getDeleteIds(level1.value, id);
      axios.delete("/doc/delete/" + ids.join(',')).then((response) => {
        const data = response.data; // data => CommonResp
        if (data.success) {
          // 重新加载列表
          handleQuery({});
        }
      });
    }

    const showConfirm = (id: number) => {
      ids.length = 0;
      names.length = 0;
      getDeleteIds(level1.value, id);
      Modal.confirm({
        title: '重要提醒',
        // icon: ExclamationCircleOutlined,
        content: '将删除：【' + names.join('，') + '】，删除后不可恢复。确认删除？',
        onOk() {
          handleDelete(id);
        },
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        onCancel() {
        },
      });
    }

    /**
     * 富文本预览
     */
    const drawerVisible = ref(false);
    const previewHtml = ref();
    const handlePreviewContent = () => {
      const html = editor.txt.html();
      previewHtml.value = html;
      drawerVisible.value = true;
    };
    const onDrawerClose = () => {
      drawerVisible.value = false;
    };



    onMounted(() => {
      handleQuery({
      });
      editor = new E('#content');
      editor.config.zIndex = 0;
      editor.create();


    });

    return {
      param,
      // docs,
      level1,
      columns,
      loading,


      edit,
      add,
      doc,
      modalVisible,
      modalLoading,
      handleDelete,
      handleQuery,
      treeSelectData,
      showConfirm,
      handleSave,
      drawerVisible,
      previewHtml,
      handlePreviewContent,
      onDrawerClose,
    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>