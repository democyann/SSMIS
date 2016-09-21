package com.systop.common.modules.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * 
* @ClassName: DictConstants 
* @Description: 字典表常量类 
* @author 乔轩 
* @date 2013-8-30 下午03:57:40 
*
 */
public final class DictConstants {
	
	public final static Map<String, String> DICT_MAP = new HashMap<String, String>();
	static{
		DICT_MAP.put("processType", "流程类型");
		DICT_MAP.put("course", "课程");
		DICT_MAP.put("major", "专业");
		DICT_MAP.put("courseType", "课程性质");
		DICT_MAP.put("dept", "院校类型");
		DICT_MAP.put("sector", "部门类型");
		DICT_MAP.put("noticeType", "通知公告类型");
		DICT_MAP.put("outgoingMessageType", "发文类型");
		DICT_MAP.put("incomingMessageType", "收文类型");
		DICT_MAP.put("myworkType", "我的工作类型");
		DICT_MAP.put("planType", "任务计划类型");
		DICT_MAP.put("nation", "民族");
		DICT_MAP.put("meetingNature", "会议性质");
		DICT_MAP.put("askType", "请示报告状态");
		DICT_MAP.put("commissionType", "企业委托录入");
		DICT_MAP.put("quantity","数量/重量");
		DICT_MAP.put("transactionQuantity","处理数量");
		DICT_MAP.put("reason","处理原因");
		DICT_MAP.put("intent","处理目的");
		DICT_MAP.put("method", "处理方法");
		DICT_MAP.put("receipts", "随附单据");
		DICT_MAP.put("assetType", "资产类型");
		DICT_MAP.put("scopePart","范围/部位");
		DICT_MAP.put("buildingStructure", "建筑结构/材质");
		DICT_MAP.put("processingArea","处理面积/容积");
		DICT_MAP.put("paymentsMeans","支付方式");
		DICT_MAP.put("safetyInspection","现场安全检查记录");
		DICT_MAP.put("evaluationmethod","评价方法");
		DICT_MAP.put("evaluationreason","评价原因");
		DICT_MAP.put("sourceRecord", "原始记录");
		DICT_MAP.put("currency", "币种");
		DICT_MAP.put("customer_type", "客户类型");
		DICT_MAP.put("customer_sources", "客户来源");
		DICT_MAP.put("customer_indAttributes", "行业属性");
		DICT_MAP.put("customer_entCharacter", "客户性质");
		DICT_MAP.put("drugunit", "单位用药量单位");
		DICT_MAP.put("customerService_type", "服务类型");
	}
	
	
}
