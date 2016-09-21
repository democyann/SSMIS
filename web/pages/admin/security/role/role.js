var currentRoleId; // The current role that we are assigning perm for.

var store = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
		url : ''// URL will defined dynamicly.
	}),
	reader : new Ext.data.JsonReader({
		root : 'root',
		totalProperty : 'totalProperty',
		id : 'id',
		fields : [{
			name : 'id'
		}, {
			name : 'name'
		}, {
			name : 'descn'
		}, {
			name : 'changed'
		}]
	}),

	remoteSort : true
});
var cm = new Ext.grid.ColumnModel([{
	header : "许可名",
	dataIndex : 'name',
	width : 150,
	sortable : true
}, {
	header : "许可描述",
	dataIndex : 'descn',
	width : 300,
	align : 'left'
}, {
	header : "选择",
	dataIndex : 'changed',
	width : 50,
	sortable : false,
	align : 'center',
	renderer : function(value, cellmeta, record, rowIndex, columnIndex, store) {
		var checked = (value == true) ? 'checked' : '';
		return "<input type='checkbox' value='" + record.data["id"] + "' "
				+ checked + " id='cb' class='checkbox' "
				+ "onclick='javascript:onSelectPermission(this)'>"
	}
}]);
var grid = new Ext.grid.GridPanel({
	el : 'perm_grid',
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
	txt.id = "perm-name";
	var msg = document.createTextNode("许可名:");
	div.appendChild(msg);
	div.appendChild(txt);
	return div;
}

var tbarItems = toolbarItmes();

var win = new Ext.Window({
	el : 'win',
	tbar : [tbarItems, {
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
				url : '/security/permission/saveRolePermissions.do',
				params : {
					'role.id' : currentRoleId
				},
				method : 'POST',
				success : function() {
					win.hide();
					Ext.my().msg('', '您已经成功的为角色分配了许可.');
				}
			});
		}
	}, {
		text : '取消',
		handler : function() {
			cancelAssign();
			win.hide();
		}
	}],
	layout : 'fit',
	width : 580,
	height : 420,
	closeAction : 'hide',
	plain : false,
	modal : true,
	items : [grid],
	bodyStyle : 'padding:5px;',
	buttonAlign : 'center',
	buttons : []
});

win.addListener('hide', cancelAssign); // When dialog closed, a cancel command
										// will be sent to backend.

function cancelAssign() {
	Ext.Ajax.request({
		url : '/security/permission/cancelSaveRolePermissions.do',
		params : {
			'role.id' : currentRoleId
		},
		method : 'POST'
	});
}

function onSelectPermission(cb) {
	var url = (cb.checked)
			? '/security/permission/selectPermission.do'
			: '/security/permission/deselectPermission.do';
	Ext.Ajax.request({
		url : url,
		params : {
			'role.id' : currentRoleId,
			'model.id' : cb.value
		},
		method : 'POST'
	});
}

/*function assignPermissions(roleId) {
	currentRoleId = roleId;
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


function assignPermissions(roleId){
	currentRoleId = roleId;
	pwin = new Ext.Window({
		title: '权限列表',
		layout : 'auto',
		width : 650,
		autoHeight : true,
		closeAction : 'close',
		plain : false,
		modal : true,
		bodyStyle : 'padding:8px;background-color:#ffffff; ',
		buttonAlign : 'center',
		buttons: [
				    {
				    	text: '保存', handler: function(){
				    		$.ajax({
				    			url : '/hbcrj/security/permission/savePermissionList.do',
				    			dataType : 'json',
				    			type : 'POST',
				    			data : $('#permissionForm').serialize(),
				    			success : function(r){
				    				Ext.my().msg('', r.msg);
				    				pwin.close();
				    			}
				    		});
				    	}
				    },
				    {
		          		text: '关闭', handler: function(){ pwin.close();}
				    }
		]
	});
	pwin.show();
	pwin.load({
		url : URL_PREFIX+'/security/permission/permissionList.do?role.id='+roleId,
		scripts : true
	});
	pwin.getUpdater().on("update", function() {
		pwin.center();
	});
}


function url() {
	return '/security/permission/permissionsOfRole.do?role.id=' + currentRoleId
			+ '&model.name=' + Ext.get('perm-name').getValue();
}