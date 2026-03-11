<template>
	<div>
		<el-dialog modal-class="edit_form_modal" class="edit_form" v-model="formVisible" :title="formTitle" width="60%" destroy-on-close :fullscreen='false'>
			<el-form class="formModel_form" ref="formRef" :model="form" :rules="rules">
				<el-row >
					<el-col :span="12">
						<el-form-item label="预约编号" prop="yuyuebianhao">
							<el-input class="list_inp" v-model="form.yuyuebianhao" placeholder="预约编号" 
								:readonly="true" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="医生账号" prop="yishengzhanghao">
							<el-input class="list_inp" v-model="form.yishengzhanghao" placeholder="医生账号"
								:readonly="true" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="账号" prop="zhanghao">
							<el-input class="list_inp" v-model="form.zhanghao" placeholder="账号"
								:readonly="true" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="通知类型" prop="tongzhileixing">
							<el-select class="list_inp" v-model="form.tongzhileixing" placeholder="通知类型" :disabled="true">
								<el-option label="预约成功通知" :value="1"></el-option>
								<el-option label="就诊前一天提醒" :value="2"></el-option>
								<el-option label="就诊当天提醒" :value="3"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="通知内容" prop="tongzhineirong">
							<el-input class="list_inp" v-model="form.tongzhineirong" placeholder="通知内容"
								type="textarea" :rows="3"
								:readonly="true" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="发送状态" prop="fasongzhuangtai">
							<el-select class="list_inp" v-model="form.fasongzhuangtai" placeholder="发送状态" :disabled="true">
								<el-option label="待发送" :value="0"></el-option>
								<el-option label="发送成功" :value="1"></el-option>
								<el-option label="发送失败" :value="2"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="发送时间" prop="fasongshijian">
							<el-date-picker
								class="list_date"
								v-model="form.fasongshijian"
								format="YYYY-MM-DD HH:mm:ss"
								value-format="YYYY-MM-DD HH:mm:ss"
								type="datetime"
								:readonly="true"
								placeholder="发送时间" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="计划发送时间" prop="jihuafasongshijian">
							<el-date-picker
								class="list_date"
								v-model="form.jihuafasongshijian"
								format="YYYY-MM-DD HH:mm:ss"
								value-format="YYYY-MM-DD HH:mm:ss"
								type="datetime"
								:readonly="true"
								placeholder="计划发送时间" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="就诊时间" prop="jiuzhenshijian">
							<el-date-picker
								class="list_date"
								v-model="form.jiuzhenshijian"
								format="YYYY-MM-DD HH:mm:ss"
								value-format="YYYY-MM-DD HH:mm:ss"
								type="datetime"
								:readonly="true"
								placeholder="就诊时间" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="重试次数" prop="chongshicishu">
							<el-input class="list_inp" v-model="form.chongshicishu" placeholder="重试次数"
								:readonly="true" />
						</el-form-item>
					</el-col>
					<el-col :span="24" v-if="form.fasongzhuangtai == 2">
						<el-form-item label="失败原因" prop="shibaiyuanyin">
							<el-input class="list_inp" v-model="form.shibaiyuanyin" placeholder="失败原因"
								type="textarea" :rows="2"
								:readonly="true" />
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<template #footer>
				<span class="formModel_btn_box">
					<el-button class="cancel_btn" @click="closeClick">关闭</el-button>
					<el-button v-if="form.fasongzhuangtai == 2" class="confirm_btn" type="warning" @click="retryClick">
						重试发送
					</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>
<script setup>
	import {
		reactive,
		ref,
		getCurrentInstance,
		nextTick,
		computed,
		defineEmits
	} from 'vue'
    import {
        useStore
    } from 'vuex';

	const store = useStore()
	const context = getCurrentInstance()?.appContext.config.globalProperties;

	const tableName = 'tongzhijilu'

	//基础信息
	const readOnly = ref(false)
	const form = ref({})
	const formVisible = ref(false)
	const formTitle = ref('')
	const formRef = ref(null)
	const rules = ref({})
	const disabledForm = ref({
		yuyuebianhao : false,
		yishengzhanghao : false,
		zhanghao : false,
		tongzhileixing : false,
		tongzhineirong : false,
		fasongzhuangtai : false,
		fasongshijian : false,
		shibaiyuanyin : false,
		chongshicishu : false,
		jiuzhenshijian : false,
		jihuafasongshijian : false,
	})
	const type = ref('')
	const emit = defineEmits(['formModelChange'])

	//方法

	//获取唯一标识
	const getUUID =()=> {
		return new Date().getTime();
	}
	const getInfo=()=>{
		context.$http({
			url: `${tableName}/info/${form.value.id}`,
			method: 'get'
		}).then(res => {
			form.value = res.data.data
			formVisible.value = true
		})
	}
	const init=(id=null,type=null)=>{
		if(type){
			type.value = type
		}
		if(type=='info'){
			formTitle.value = '查看通知记录'
			readOnly.value = true
			disabledForm.value = {
				yuyuebianhao : true,
				yishengzhanghao : true,
				zhanghao : true,
				tongzhileixing : true,
				tongzhineirong : true,
				fasongzhuangtai : true,
				fasongshijian : true,
				shibaiyuanyin : true,
				chongshicishu : true,
				jiuzhenshijian : true,
				jihuafasongshijian : true,
			}
			getInfo()
		}
	}
	const closeClick = () => {
		form.value = {}
		formVisible.value = false
	}
	
	// 重试发送
	const retryClick = () => {
		context.$http({
			url: `${tableName}/retry/${form.value.id}`,
			method: 'post'
		}).then(res => {
			context?.$toolUtil.message('发送成功', 'success',()=>{
				getInfo()
				emit('formModelChange')
			})
		}).catch(err => {
			context?.$toolUtil.message('发送失败，请稍后重试', 'error')
			getInfo()
		})
	}
	defineExpose({
		init
	})
</script>
<style lang="scss" scoped>
	.formModel_btn_box{
		display: flex;
		justify-content: center;
	}
</style>
