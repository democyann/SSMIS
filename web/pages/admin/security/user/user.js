var currentUserId; // The current user that we are assigning roles for.

var store = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
		url : ''// URL will defined dynamicly.
	}),
	reader : new Ext.data.JsonReader({
		root : 'root',
		totalProperty : 'totalProperty',
		id : 'id',
		fields : [ {
			name : 'id'
		}, {
			name : 'name'
		}, {
			name : 'descn'
		}, {
			name : 'changed'
		} ]
	}),

	remoteSort : true
});
var cm = new Ext.grid.ColumnModel([
		{
			header : "角色名",
			dataIndex : 'name',
			width : 200,
			sortable : true
		},
		{
			header : "角色描述",
			dataIndex : 'descn',
			width : 300,
			align : 'left'
		},
		{
			header : "选择",
			dataIndex : 'changed',
			width : 50,
			sortable : false,
			align : 'center',
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				var checked = (value == true) ? 'checked' : '';
				return "<input type='checkbox' value='" + record.data["id"]
						+ "' " + checked + " id='cb' class='checkbox' "
						+ "onclick='javascript:onSelectRole(this)'>"
			}
		} ]);
var grid = new Ext.grid.GridPanel({
	el : 'role_grid',
	title : null,
	width : 500,
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

function toolbarItmes() {
	var div = document.createElement("div");
	var txt = document.createElement("input");
	div.style.margin = "2px;";
	txt.type = "text";
	txt.name = "model.name";
	txt.id = "role-name";
	var msg = document.createTextNode("角色名:");
	div.appendChild(msg);
	div.appendChild(txt);
	return div;
}

var tbarItems = toolbarItmes();

var win = new Ext.Window({
	el : 'win',
	tbar : [ tbarItems, {
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
	}, {
		text : '保存',
		handler : function() {
			Ext.Ajax.request({
				url : '/security/role/saveUserRoles.do',
				params : {
					'user.id' : currentUserId
				},
				method : 'POST',
				success : function() {
					win.hide();
					Ext.my().msg('', '您已经成功的为用户分配了角色.');
				}
			});
		}
	}, {
		text : '取消',
		handler : function() {
			cancelAssign();
			win.hide();
		}
	} ],
	layout : 'fit',
	width : 580,
	height : 420,
	closeAction : 'hide',
	plain : false,
	modal : true,
	items : [ grid ],
	bodyStyle : 'padding:5px;',
	buttonAlign : 'center',
	buttons : []
});

win.addListener('hide', cancelAssign); // When dialog closed, a cancel command
// will be sent to backend.

function cancelAssign() {
	Ext.Ajax.request({
		url : '/security/role/cancelSaveUserRoles.do',
		params : {
			'user.id' : currentUserId
		},
		method : 'POST'
	});
}

function onSelectRole(cb) {
	var url = (cb.checked) ? '/security/role/selectRole.do'
			: '/security/role/deselectRole.do';
	Ext.Ajax.request({
		url : url,
		params : {
			'user.id' : currentUserId,
			'model.id' : cb.value
		},
		method : 'POST'
	});
}

/*function assignRoles(userId) {
	currentUserId = userId;
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
}*/

function assignRoles(userId, userType) {
	currentUserId = userId;
	currentType = userType;
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
}


function url() {
	var urlStr = '/security/role/rolesOfUser.do?user.id=' + currentUserId;
	if (currentType != null ) {
		urlStr = urlStr + '&userType=' + currentType;
	}
	if (Ext.get('role-name')) {
		urlStr = urlStr + '&model.name=' + Ext.get('role-name').getValue();
	}
	return urlStr;
}

function assignLevel(userId){
	var conf = "dialogWidth=830px;dialogHeight=730px;status=no;help=no;scrollbars=no;scroll=no";
	var levelWin = window;
	window.showModalDialog(URL_PREFIX + '/security/user/levelOfUser.do?model.id=' + userId, levelWin, conf);
	location.reload();
}