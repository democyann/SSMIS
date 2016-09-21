var currentPermId; // The current role that we are assigning perm for.

var tree = new Ext.tree.TreePanel({
    el : 'tree-div',
    height: 300,
    width: 400,
    useArrows:true,
    autoScroll:true,
    animate:true,
    enableDD:true,
    containerScroll: true,
    rootVisible: false,
    frame: true,
    
    loader:new Ext.tree.TreeLoader({dataUrl : '/security/menu/menuTree.do'}),
    
    listeners: {
        'checkchange': function(node, checked){
        	onSelectResource({checked:checked,value:node.id});
            if (node.parentNode != null) {
                //父节点选中
                var pNode = node.parentNode;
                if (checked || tree.getChecked(id, pNode) == "") {
                    pNode.ui.toggleCheck(checked);
                    pNode.attributes.checked = checked;
                }
            }
        }
    }
});
var root = new Ext.tree.AsyncTreeNode({
    text: '系统菜单',
    id:'0'
});
tree.setRootNode(root);
tree.render();

var win_menu = new Ext.Window({
	el : 'win_menu',
	tbar : [{
		text : '保存',
		handler : function() {
			Ext.Ajax.request({
				url : '/security/resource/savePermissionResources.do',
				params : {
					'permission.id' : currentPermId
				},
				method : 'POST',
				success : function() {
					win_menu.hide();
					Ext.my().msg('', '您已经成功的为许可分配了菜单.');
				}
			});
		}
	}, {
		text : '取消',
		handler : function() {
			cancelAssign();
			win_menu.hide();
		}
	}],
	layout : 'fit',
	width : 650,
	height : 430,
	closeAction : 'hide',
	plain : false,
	modal : true,
	bodyStyle : 'padding:5px;',
	buttonAlign : 'center',
	buttons : []
});

win_menu.addListener('hide', cancelAssign); // When dialog closed, a cancel command

function assignMenus(permId) {
	currentPermId = permId;
	if (win_menu) {
		win_menu.add(tree);
		tree.getRootNode().expand(true);
		win_menu.show();
	}
}