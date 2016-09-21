/*
 * Ext JS Library 2.0.2 Copyright(c) 2006-2008, Ext JS, LLC. licensing@extjs.com
 * 
 * http://extjs.com/license
 */
var tree;
Ext.onReady(function() {
	Ext.isBorderBox = true;
	tree = new Ext.tree.ColumnTree({
		el : 'tree-dept',
		width : 553,
		animate : false,
		autoHeight : true,
		rootVisible : false,//是否显示根节点
		autoScroll : true,
		lines : true,
		title : '',
		useArrows : false,

		columns : [{
			header : '处室名称',
			width : 250,
			dataIndex : 'text'
		}, {
			header : '编辑',
			width : 100,
			dataIndex : 'edit'
		}],

		loader : new Ext.tree.ColumnTreeLoader({
			dataUrl : '/systop/dept/deptTree.do',
			uiProviders : {
				'col' : Ext.tree.ColumnNodeUI
			},
			listeners : {
				"beforeCreateNode" : function(l, attr) {
					attr.uiProvider = "col";
					attr.checked = false;
					attr.iconCls = (attr.leaf) ? "task" : "task-folder";
					attr.cn = "selectedItems";
					if (!attr.descn)
						attr.descn = '&nbsp;';
					attr.edit = [
							'<div align="center">',
							Ext.ctx('<img src="{ctx}/images/icons/modify.gif" border="0" onclick="edit('),
							attr.id, ')"></div>'].join('');
				}
			}
		}),

		root : new Ext.tree.AsyncTreeNode({
			text : 'Tasks'
		})
	});
	tree.render();
	
	Ext.get('remove-btn').on('click', remove);
});
function edit(id) {
	Ext.get("targetId").dom.value = id;
	Ext.get("operator").dom.submit();
}
function remove() {
	if(!tree) {
		return;
	}
	var checked = tree.getChecked();
	
	if(!checked || !checked.length) {		
		Ext.MessageBox.alert('提示', '请选择一个处室.');
		return;
	}
	
	Ext.MessageBox.confirm('提示', '是否确定删除这些处室?', function(btn) {
		if(btn == 'yes') {
			Ext.get('killer').dom.submit();
		}
	});
}
