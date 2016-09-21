var currentDeptGrade;
var selectedItems = new Array();
var selectedItems_ = new Array();
var reasonMap = new Map();// 隐藏的map，存id，reason
var idNameMap = new Map();
var startMap = new Map();

function initIdNameMap() {
	var map = new Map();
	if (null != $("#stuIdNames")) {
		var stuIdNames = $("#stuIdNames").val();
		var str = stuIdNames.split(",");
		for ( var i = 0; i < str.length; i++) {
			if (i != str.length - 1) {
				map.put(str[i].split(":")[0], str[i].split(":")[1]);
			}
		}
	}
	return map;
}
function initReasonMap() {
	var map = new Map();
	var str2 = $("#absentees").val();
	var str = str2.split(",");
	for ( var i = 0; i < str.length; i++) {
		if (i != str.length - 1) {
			map.put(str[i].split(":")[0], str[i].split(":")[1]);
		}
	}
	return map;
}
function initMap() {
	idNameMap = initIdNameMap();
	reasonMap = initReasonMap();
	startMap = initReasonMap();
}
function clearMap() {
	idNameMap.clear();
	reasonMap.clear();
}
var store = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
		url : ''
	}),
	reader : new Ext.data.JsonReader({
		root : 'root',
		totalProperty : 'totalProperty',
		id : 'id',
		fields : [ {
			name : 'name'
		}, {
			name : 'id'
		}, {
			name : 'changed'
		} ]
	}),
	remoteSort : true
});

// 弹出框每列内容
var cm = new Ext.grid.ColumnModel(
		[
				{
					header : "姓名",
					dataIndex : 'name',
					width : 100,
					sortable : true,
					align : 'center'
				},
				{
					header : "出勤状态",
					dataIndex : 'id',
					width : 100,
					sortable : true,
					align : 'center',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						var str = document.getElementById("absentees").value;
						var s = "";
						var strAll = "";
						if (str.indexOf(value) > -1) {
							s = str.substr(
									(str.indexOf(value) + value.length + 1), 1);
							strAll += "<select id='" + record.data["id"]
									+ "'onchange='reasonChange(this);' >";
							if ('P' == s) {
								strAll += "<option value='P' selected='selected' >出勤</option>";
							} else {
								strAll += "<option value='P' >出勤</option>";
							}
							if ('L' == s) {
								strAll += "<option value='L' selected='selected' >迟到</option>";
							} else {
								strAll += "<option value='L' >迟到</option>";
							}
							if ('S' == s) {
								strAll += "<option value='S' selected='selected' >病假</option>";
							} else {
								strAll += "<option value='S' >病假</option>";
							}
							if ('H' == s) {
								strAll += "<option value='H' selected='selected' >事假</option>";
							} else {
								strAll += "<option value='H' >事假</option>";
							}
							if ('E' == s) {
								strAll += "<option value='E' selected='selected' >早退</option>";
							} else {
								strAll += "<option value='E' >早退</option>";
							}
							if ('A' == s) {
								strAll += "<option value='A' selected='selected' >旷课</option>";
							} else {
								strAll += "<option value='A' >旷课</option>";
							}
							if ('Q' == s) {
								strAll += "<option value='Q' selected='selected' >其它</option>";
							} else {
								strAll += "<option value='Q' >其它</option>";
							}
							strAll += "</select>";
						} else {
							strAll = "<select id='"
									+ record.data["id"]
									+ "'onchange='reasonChange(this);'>"
									+ "<option value='P' selected='selected'>出勤</option>"
									+ "<option value='L'>迟到</option>"
									+ "<option value='S'>病假</option>"
									+ "<option value='H'>事假</option>"
									+ "<option value='E'>早退</option>"
									+ "<option value='A'>旷课</option>"
									+ "<option value='Q'>其它</option>"
									+ "</select>";
						}
						return strAll;
					}
				} ]);
var grid = new Ext.grid.GridPanel({
	el : 'user_grid',
	title : null,
	width : 550,
	height : 420,
	store : store,
	loadMask : true,
	cm : cm,
	bbar : new Ext.PagingToolbar({
		pageSize : 15,
		store : store,
		displayInfo : true,
		displayMsg : '共{2}条记录,显示{0}到{1}',
		emptyMsg : "没有记录"
	})
});

// 查询条件，姓名
function toolbarItmes() {
	var div = document.createElement("div");
	var txt = document.createElement("input");
	div.style.margin = "2px;";
	txt.type = "text";
	txt.name = "model.name";
	txt.id = "perm-name";
	var msg = document.createTextNode("姓名：");
	div.appendChild(msg);
	div.appendChild(txt);
	return div;
}

var tbarItems = toolbarItmes();

// 创建新的弹出窗口
var win = new Ext.Window(
		{
			el : 'win',
			tbar : [
					tbarItems,
					{
						text : '查询',
						handler : function() {
							store.proxy.conn.url = url();
							store.load({
								params : {
									start : 0,
									limit : 15
								}
							});
						}
					},
					{
						text : '保存',
						handler : function() {
							// 未出勤名单赋值
							if (reasonMap.size() > 0) {
								document.getElementById("absentees").value = hideMap2String(reasonMap);
								document.getElementById("absenteesName").value = showMap2String(reasonMap);
							} else {
								document.getElementById("absentees").value = "";
								document.getElementById("absenteesName").value = "";
							}
							// 实到人数赋值
							var adsNum = reasonMap.size();
							document.getElementById("peopleNum").value = document
									.getElementById("arrivedNum").value
									- adsNum;
							win.hide();
						}
					},
					{
						text : '取消',
						handler : function() {
							if (startMap.size() > 0) {
								document.getElementById("absentees").value = hideMap2String(startMap);
								document.getElementById("absenteesName").value = showMap2String(startMap);
							} else {
								document.getElementById("absentees").value = "";
								document.getElementById("absenteesName").value = "";
							}
							initMap();
							win.hide();
						}
					} ],
			layout : 'fit',
			width : 660,
			height : 420,
			closeAction : 'hide',
			plain : false,
			modal : true,
			items : [ grid ],
			bodyStyle : 'padding:5px;',
			buttonAlign : 'center',
			buttons : []
		});
win.addListener('hide', cancelAssign);

function cancelAssign() {
	initMap();
}
function ajaxGetStuIdNames() {
	var gradeId_ = $("#deptGrade").val();
	$.ajax({
		type : "POST",
		url : URL_PREFIX + '/tpf/supportrecord/ajaxGetStuIdNames.do',
		dataType : 'json',
		data : {
			"gradeId" : gradeId_
		},
		success : function(msg) {
			$("#stuIdNames").val(msg);
			initMap();
		}
	});
}
// 添加未出勤学生主方法
function addStudents(deptGrade) {
	currentDeptGrade = deptGrade;
	if (deptGrade != null && deptGrade != "") {
		if (win) {
			win.show();
			store.proxy.conn.url = url();
			store.load({
				params : {
					start : 0,
					limit : 15
				}
			});
		}

	} else {
		alert("请先选择班级！");
	}
}

// 与后台数据之间的交互
function url() {
	var absentees = document.getElementById("absentees").value;
	var urlStr;
	urlStr = URL_PREFIX
			+ '/tpf/supportrecord/studentsOfGrade.do?model.supportDept.id='
			+ currentDeptGrade + '&&model.absentees=' + absentees;
	if (Ext.get('perm-name').getValue() != ""
			&& Ext.get('perm-name').getValue() != null) {
		urlStr = urlStr + '&studentname=' + Ext.get('perm-name').getValue();
	}
	return urlStr;
}

function hideMap2String(map) {
	var str = "";
	for ( var i = 0; i < map.size(); i++) {
		str = str + map.element(i).key + ":" + map.element(i).value + ",";
	}
	return str;
}
function showMap2String(map) {
	var str = "";
	for ( var i = 0; i < map.keys().length; i++) {
		var reason = map.get(map.keys()[i]);
		var reason_ = "";
		if (null != reason && "" != reason) {
			if ('P' == reason) {
				reason_ = "出勤";
			} else if ('L' == reason) {
				reason_ = "迟到";
			} else if ('S' == reason) {
				reason_ = "病假";
			} else if ('H' == reason) {
				reason_ = "事假";
			} else if ('E' == reason) {
				reason_ = "早退";
			} else if ('A' == reason) {
				reason_ = "旷课";
			} else if ('Q' == reason) {
				reason_ = "其它";
			} else {
				reason_ = "";
			}
		}
		str = str + idNameMap.get(map.keys()[i]) + ":" + reason_ + ",";
	}
	return str;
}
function reasonChange(sel) {
	var id = sel.id;
	reasonMap.remove(id);
	if (sel.value != "P") {
		reasonMap.put(sel.id, sel.value);
	}
}
/*
 * Map对象，实现Map功能
 * 
 * 
 * size() 获取Map元素个数 isEmpty() 判断Map是否为空 clear() 删除Map所有元素 put(key,
 * value)向Map中增加元素（key, value) remove(key) 删除指定key的元素，成功返回true，失败返回false
 * get(key)获取指定key的元素值value，失败返回null
 * element(index)获取指定索引的元素（使用element.key，element.value获取key和value），失败返回null
 * containsKey(key)判断Map中是否含有指定key的元素 containsValue(value) 判断Map中是否含有指定value的元素
 * keys()获取Map中所有key的数组（array） values() 获取Map中所有value的数组（array）
 * 
 */
function Map() {
	this.elements = new Array();

	// 获取Map元素个数
	this.size = function() {
		return this.elements.length;
	},

	// 判断Map是否为空
	this.isEmpty = function() {
		return (this.elements.length < 1);
	},

	// 删除Map所有元素
	this.clear = function() {
		this.elements = new Array();
	},

	// 向Map中增加元素（key, value)
	this.put = function(_key, _value) {
		if (this.containsKey(_key) == true) {
			if (this.containsValue(_value)) {
				if (this.remove(_key) == true) {
					this.elements.push({
						key : _key,
						value : _value
					});
				}
			} else {
				this.elements.push({
					key : _key,
					value : _value
				});
			}
		} else {
			this.elements.push({
				key : _key,
				value : _value
			});
		}
	},

	// 删除指定key的元素，成功返回true，失败返回false
	this.remove = function(_key) {
		var bln = false;
		try {
			for ( var i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					this.elements.splice(i, 1);
					return true;
				}
			}
		} catch (e) {
			bln = false;
		}
		return bln;
	},

	// 获取指定key的元素值value，失败返回null
	this.get = function(_key) {
		try {
			for ( var i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					return this.elements[i].value;
				}
			}
		} catch (e) {
			return null;
		}
	},

	// 获取指定索引的元素（使用element.key，element.value获取key和value），失败返回null
	this.element = function(_index) {
		if (_index < 0 || _index >= this.elements.length) {
			return null;
		}
		return this.elements[_index];
	},

	// 判断Map中是否含有指定key的元素
	this.containsKey = function(_key) {
		var bln = false;
		try {
			for ( var i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					bln = true;
				}
			}
		} catch (e) {
			bln = false;
		}
		return bln;
	},

	// 判断Map中是否含有指定value的元素
	this.containsValue = function(_value) {
		var bln = false;
		try {
			for ( var i = 0; i < this.elements.length; i++) {
				if (this.elements[i].value == _value) {
					bln = true;
				}
			}
		} catch (e) {
			bln = false;
		}
		return bln;
	},

	// 获取Map中所有key的数组（array）
	this.keys = function() {
		var arr = new Array();
		for ( var i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].key);
		}
		return arr;
	},

	// 获取Map中所有value的数组（array）
	this.values = function() {
		var arr = new Array();
		for ( var i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].value);
		}
		return arr;
	};
}