<template>
	<div>
		<div class="center_view">
			<div class="list_search_view">
				<el-form :model="searchQuery" class="search_form" >
					<div class="search_view">
						<div class="search_label">
							预约编号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.yuyuebianhao" placeholder="预约编号"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							医生账号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.yishengzhanghao" placeholder="医生账号"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							账号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.zhanghao" placeholder="账号"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							发送状态：
						</div>
						<div class="search_box">
							<el-select class="search_inp" v-model="searchQuery.fasongzhuangtai" placeholder="发送状态" clearable>
								<el-option label="待发送" :value="0"></el-option>
								<el-option label="发送成功" :value="1"></el-option>
								<el-option label="发送失败" :value="2"></el-option>
							</el-select>
						</div>
					</div>
					<div class="search_btn_view">
						<el-button class="search_btn" type="primary" @click="searchClick()" size="small">搜索</el-button>
						<el-button class="search_btn" type="info" @click="resetClick()" size="small">重置</el-button>
					</div>
				</el-form>
				<div class="btn_view">
					<el-button class="del_btn" type="danger" :disabled="selRows.length?false:true" @click="delClick(null)"  v-if="btnAuth('tongzhijilu','删除')">
						<i class="iconfont icon-shanchu4"></i>
						删除
					</el-button>
					<el-button class="add_btn" type="warning" :disabled="selRows.length?false:true" @click="retryBatchClick()" v-if="btnAuth('tongzhijilu','重试')">
						<i class="iconfont icon-xiugai5"></i>
						批量重试
					</el-button>
					<el-button class="add_btn" type="success" @click="statisticsClick()">
						<i class="iconfont icon-sousuo2"></i>
						发送统计
					</el-button>
				</div>
			</div>
			<el-table
				v-loading="listLoading"
				border
				:stripe='false'
				@selection-change="handleSelectionChange"
				ref="table"
				v-if="btnAuth('tongzhijilu','查看')"
				:data="list"
				@row-click="listChange">
				<el-table-column :resizable='true' align="left" header-align="left" type="selection" width="55" />
				<el-table-column label="序号" width="70" :resizable='true' align="left" header-align="left">
					<template #default="scope">{{ (listQuery.page-1)*listQuery.limit+scope.$index + 1}}</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="yuyuebianhao"
					label="预约编号">
					<template #default="scope">
						{{scope.row.yuyuebianhao}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="yishengzhanghao"
					label="医生账号">
					<template #default="scope">
						{{scope.row.yishengzhanghao}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="zhanghao"
					label="账号">
					<template #default="scope">
						{{scope.row.zhanghao}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="tongzhileixing"
					label="通知类型">
					<template #default="scope">
						<el-tag v-if="scope.row.tongzhileixing==1" type="success">预约成功通知</el-tag>
						<el-tag v-else-if="scope.row.tongzhileixing==2" type="warning">就诊前一天提醒</el-tag>
						<el-tag v-else-if="scope.row.tongzhileixing==3" type="danger">就诊当天提醒</el-tag>
						<span v-else>{{scope.row.tongzhileixing}}</span>
					</template>
				</el-table-column>
				<el-table-column min-width="200"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="tongzhineirong"
					label="通知内容"
					show-overflow-tooltip>
					<template #default="scope">
						{{scope.row.tongzhineirong}}
					</template>
				</el-table-column>
				<el-table-column min-width="120"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="fasongzhuangtai"
					label="发送状态">
					<template #default="scope">
						<el-tag v-if="scope.row.fasongzhuangtai==0" type="info">待发送</el-tag>
						<el-tag v-else-if="scope.row.fasongzhuangtai==1" type="success">发送成功</el-tag>
						<el-tag v-else-if="scope.row.fasongzhuangtai==2" type="danger">发送失败</el-tag>
						<span v-else>{{scope.row.fasongzhuangtai}}</span>
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="fasongshijian"
					label="发送时间">
					<template #default="scope">
						{{scope.row.fasongshijian}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="jihuafasongshijian"
					label="计划发送时间">
					<template #default="scope">
						{{scope.row.jihuafasongshijian}}
					</template>
				</el-table-column>
				<el-table-column min-width="100"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="chongshicishu"
					label="重试次数">
					<template #default="scope">
						<el-tag v-if="scope.row.chongshicishu>0" type="warning">{{scope.row.chongshicishu}}</el-tag>
						<span v-else>{{scope.row.chongshicishu}}</span>
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="shibaiyuanyin"
					label="失败原因"
					show-overflow-tooltip>
					<template #default="scope">
						<span style="color: #f56c6c;">{{scope.row.shibaiyuanyin}}</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="200" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<el-button class="view_btn" type="info" v-if=" btnAuth('tongzhijilu','查看')" @click="infoClick(scope.row.id)">
							<i class="iconfont icon-sousuo2"></i>
							查看
						</el-button>
						<el-button class="edit_btn" type="warning" @click="retryClick(scope.row)" v-if="scope.row.fasongzhuangtai==2 && btnAuth('tongzhijilu','重试')">
							<i class="iconfont icon-xiugai5"></i>
							重试
						</el-button>
						<el-button class="del_btn" type="danger" @click="delClick(scope.row.id)"  v-if="btnAuth('tongzhijilu','删除')">
							<i class="iconfont icon-shanchu4"></i>
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-pagination
				background
				:layout="layouts.join(',')"
				:total="total"
				:page-size="listQuery.limit"
                v-model:current-page="listQuery.page"
				prev-text="上一页"
				next-text="下一页"
				:hide-on-single-page="false"
				:style='{}'
				:page-sizes="[10, 20, 30, 40, 50, 100]"
				@size-change="sizeChange"
				@current-change="currentChange"  />
		</div>
		<formModel ref="formRef" @formModelChange="formModelChange"></formModel>
		
		<!-- 统计弹窗 -->
		<el-dialog title="发送统计" v-model="statisticsVisible" width="400px">
			<div class="statistics-content">
				<el-row :gutter="20">
					<el-col :span="24">
						<div class="stat-item">
							<div class="stat-label">待发送：</div>
							<div class="stat-value pending">{{statistics.pendingCount}} 条</div>
						</div>
					</el-col>
				</el-row>
				<el-row :gutter="20" style="margin-top: 15px;">
					<el-col :span="24">
						<div class="stat-item">
							<div class="stat-label">发送成功：</div>
							<div class="stat-value success">{{statistics.successCount}} 条</div>
						</div>
					</el-col>
				</el-row>
				<el-row :gutter="20" style="margin-top: 15px;">
					<el-col :span="24">
						<div class="stat-item">
							<div class="stat-label">发送失败：</div>
							<div class="stat-value fail">{{statistics.failCount}} 条</div>
						</div>
					</el-col>
				</el-row>
				<el-row :gutter="20" style="margin-top: 20px;">
					<el-col :span="24">
						<div class="stat-item">
							<div class="stat-label">总计：</div>
							<div class="stat-value total">{{statistics.pendingCount + statistics.successCount + statistics.failCount}} 条</div>
						</div>
					</el-col>
				</el-row>
			</div>
			<template #footer>
				<el-button @click="statisticsVisible = false">关闭</el-button>
			</template>
		</el-dialog>
	</div>
</template>
<script setup>
	import axios from 'axios'
    import moment from "moment"
	import {
		reactive,
		ref,
		getCurrentInstance,
		nextTick,
		onMounted,
		watch,
		computed,
	} from 'vue'
	import {
		useRoute,
		useRouter
	} from 'vue-router'
	import {
		ElMessageBox
	} from 'element-plus'
	import {
		useStore
	} from 'vuex';
	const store = useStore()
	const user = computed(()=>store.getters['user/session'])
	const avatar = ref(store.state.user.avatar)
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	import formModel from './formModel.vue'
	//基础信息

	const tableName = 'tongzhijilu'
	const formName = '通知记录'
	const route = useRoute()
	//基础信息
	onMounted(()=>{
	})
	//列表数据
	const list = ref(null)
	const table = ref(null)
	const listQuery = ref({
		page: 1,
		limit: 10,
		sort: 'id',
		order: 'desc'
	})
	const searchQuery = ref({})
	const selRows = ref([])
	const listLoading = ref(false)
	const listChange = (row) =>{
		nextTick(()=>{
			table.value.toggleRowSelection(row)
		})
	}
	//列表
	const getList = () => {
		listLoading.value = true
		let params = JSON.parse(JSON.stringify(listQuery.value))
		params['sort'] = 'id'
		params['order'] = 'desc'
		if(searchQuery.value.yuyuebianhao&&searchQuery.value.yuyuebianhao!=''){
			params['yuyuebianhao'] = '%' + searchQuery.value.yuyuebianhao + '%'
		}
		if(searchQuery.value.yishengzhanghao&&searchQuery.value.yishengzhanghao!=''){
			params['yishengzhanghao'] = '%' + searchQuery.value.yishengzhanghao + '%'
		}
		if(searchQuery.value.zhanghao&&searchQuery.value.zhanghao!=''){
			params['zhanghao'] = '%' + searchQuery.value.zhanghao + '%'
		}
		if(searchQuery.value.fasongzhuangtai!==undefined&&searchQuery.value.fasongzhuangtai!==''){
			params['fasongzhuangtai'] = searchQuery.value.fasongzhuangtai
		}
		context.$http({
			url: `${tableName}/page`,
			method: 'get',
			params: params
		}).then(res => {
			listLoading.value = false
			list.value = res.data.data.list
			total.value = Number(res.data.data.total)
		})
	}
	//删
	const delClick = (id) => {
		let ids = ref([])
		if (id) {
			ids.value = [id]
		} else {
			if (selRows.value.length) {
				for (let x in selRows.value) {
					ids.value.push(selRows.value[x].id)
				}
			} else {
				return false
			}
		}
		ElMessageBox.confirm(`是否删除选中${formName}`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			context.$http({
				url: `${tableName}/delete`,
				method: 'post',
				data: ids.value
			}).then(res => {
				context?.$toolUtil.message('删除成功', 'success',()=>{
					getList()
				})
			})
		}).catch(_ => {})
	}
	//多选
	const handleSelectionChange = (e) => {
		selRows.value = e
	}
	//列表数据
	//分页
	const total = ref(0)
	const layouts = ref(["total","prev","pager","next","sizes","jumper"])
	const sizeChange = (size) => {
		listQuery.value.limit = size
		getList()
	}
	const currentChange = (page) => {
		listQuery.value.page = page
		getList()
	}
	//分页
	//权限验证
	const btnAuth = (e,a)=>{
		return context?.$toolUtil.isAuth(e,a)
	}
	//搜索
	const searchClick = () => {
		listQuery.value.page = 1
		getList()
	}
	//重置
	const resetClick = () => {
		searchQuery.value = {}
		listQuery.value.page = 1
		getList()
	}
	//表单
	const formRef = ref(null)
	const formModelChange=()=>{
		searchClick()
	}
	const infoClick = (id=null)=>{
		if(id){
			formRef.value.init(id,'info')
		}
		else if(selRows.value.length){
			formRef.value.init(selRows.value[0].id,'info')
		}
	}
	
	// 重试发送
	const retryClick = (row) => {
		ElMessageBox.confirm('是否重试发送该通知？', '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			context.$http({
				url: `${tableName}/retry/${row.id}`,
				method: 'post'
			}).then(res => {
				context?.$toolUtil.message('发送成功', 'success',()=>{
					getList()
				})
			}).catch(err => {
				context?.$toolUtil.message('发送失败，请稍后重试', 'error')
				getList()
			})
		}).catch(_ => {})
	}
	
	// 批量重试
	const retryBatchClick = () => {
		if (!selRows.value.length) {
			context?.$toolUtil.message('请选择要重试的记录', 'warning')
			return
		}
		// 只选择发送失败的记录
		const failRows = selRows.value.filter(row => row.fasongzhuangtai == 2)
		if (!failRows.length) {
			context?.$toolUtil.message('所选记录中没有发送失败的记录', 'warning')
			return
		}
		const ids = failRows.map(row => row.id)
		
		ElMessageBox.confirm(`是否批量重试发送选中的 ${failRows.length} 条通知？`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			context.$http({
				url: `${tableName}/retryBatch`,
				method: 'post',
				data: ids
			}).then(res => {
				const successCount = res.data.successCount
				const totalCount = res.data.totalCount
				context?.$toolUtil.message(`成功重试 ${successCount}/${totalCount} 条`, 'success',()=>{
					getList()
				})
			})
		}).catch(_ => {})
	}
	
	// 统计
	const statisticsVisible = ref(false)
	const statistics = ref({
		pendingCount: 0,
		successCount: 0,
		failCount: 0
	})
	const statisticsClick = () => {
		context.$http({
			url: `${tableName}/statistics`,
			method: 'get'
		}).then(res => {
			statistics.value = res.data.data
			statisticsVisible.value = true
		})
	}
	
	//初始化
	const init = () => {
		getList()
	}
	init()
</script>
<style scoped>
.statistics-content {
	padding: 20px;
}
.stat-item {
	display: flex;
	align-items: center;
	font-size: 16px;
}
.stat-label {
	width: 100px;
	color: #606266;
}
.stat-value {
	font-weight: bold;
	font-size: 18px;
}
.stat-value.pending {
	color: #909399;
}
.stat-value.success {
	color: #67c23a;
}
.stat-value.fail {
	color: #f56c6c;
}
.stat-value.total {
	color: #409eff;
}
</style>
