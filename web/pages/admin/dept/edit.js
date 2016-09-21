Ext.tree.XTreeLoader = function(config) {
	config = config || {};
	Ext.tree.XTreeLoader.superclass.constructor.call(this, config);
	this.addEvents("beforeCreateNode");
};

Ext.extend(Ext.tree.XTreeLoader, Ext.tree.TreeLoader, {
			createNode : function(attr) {
				this.fireEvent("beforeCreateNode", this, attr);
				return Ext.tree.XTreeLoader.superclass.createNode.apply(this,
						arguments);
			}
		});
/**
 * 
 * @param {url:
 *            '/admin/dept/deptTree.do', 
 *            el : combox container's id, 
 *            innerTree:tree container in combox, 
 *            parent: parent dept's id in backend database, 
 *            initValue: the selected dept'id, 
 *            onclick:callback function, it is called when node clicked. }
 * 
 */
var DeptTree = function(options) {
	this.onclick = options.onclick;

	this.url = options.url;
	if (options.parent) {
		this.parent = options.parent;
		this.url += ("?parentId=" + options.parent);
	}
	this.rootName = options.rootName;
	this.initValue = options.initValue;
	this.el = options.el;
	if(options.innerTree) {
	    this.innerTree = options.innerTree;
	}

};
DeptTree.prototype = {
	url : '',
	rootName: '',
	parent : '',
	initValue : '',
	innerTree:'inner-tree',
	el : '',	
	tree : {},
	comboxWithTree : {},
	init : function() {
		this.comboxWithTree = new Ext.form.ComboBox({
			store : new Ext.data.SimpleStore({
						fields : [],
						data : [[]]
					}),
			editable : false,
			mode : 'local',
			triggerAction : 'all',
			width : 202,
			maxHeight : 300,
			tpl : "<tpl for='.'><div style='width:197px; height:300px'><div id='inner-tree'></div></div></tpl>",
			selectedClass : '',
			onSelect : Ext.emptyFn
		});
		var _this = this;
		
		_this.tree = new Ext.tree.TreePanel({
			loader : new Ext.tree.XTreeLoader({
				dataUrl : _this.url
			}),
			border : true,
			animate : true,
			autoHeight : true,
			rootVisible : false,//是否显示根节点
			root : new Ext.tree.AsyncTreeNode({
						text : _this.rootName,
						id : '0'
					})
		});

		_this.tree.on('click', function(node) {
					_this.onclick(node.id, node.text);
					_this.comboxWithTree.setValue(node.text);
					_this.comboxWithTree.collapse();
				});

		_this.comboxWithTree.on('expand', function() {			
			     Ext.getDom('inner-tree').id = _this.innerTree;
					_this.tree.render(_this.innerTree);
				});

		if (_this.initValue) {
			_this.comboxWithTree.setValue(_this.initValue);
		}

		_this.comboxWithTree.render(_this.el);
	},
	/**
	 * Callback function that deal with the dept's id. When any dept is clicked,
	 * the function will be invoked. Users can customize the funcation by
	 * override.
	 */

	onclick : function(nodeId, nodeText) {

	}
};
