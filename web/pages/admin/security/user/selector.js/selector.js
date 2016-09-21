Ext.override(Ext.tree.TreeFilter, {
			filterBy : function(fn, scope, startNode) {
				startNode = startNode || this.tree.root;
				if (this.autoClear) {
					this.clear();
				}
				var af = this.filtered, rv = this.reverse;
				var bf = {};
				var pf = function(n) {
					if (!n.parentNode) {
						return;
					}
					bf[n.parentNode.id] = n.parentNode;
					pf(n.parentNode);
				}
				var f = function(n) {
					var m = fn.call(scope || n, n);
					if (m) {
						bf[n.id] = n;
						pf(n);
					}
					return true;
				}
				startNode.cascade(f);
				for (var id in this.tree.nodeHash) {
					var ns = bf[id];
					if (ns == undefined) {
						var nh = this.tree.nodeHash[id];
						af[nh.id] = nh;
						nh.ui.hide();
					} else {
						ns.expand();
					}
				}
				if (this.remove) {
					for (var id in af) {
						if (typeof id != "function") {
							var n = af[id];
							if (n && n.parentNode) {
								n.parentNode.removeChild(n);
							}
						}
					}
				}
			}
		});

var UserSelector = function(options) {
	this.url = options.url;
	if (options.params != null) {
		this.params = options.params;
	}
	this.multiSel = (options.multiSel == null) ? false : options.multiSel;
	this.width = (options.width == null) ? this.width : options.width;
	this.height = (options.height == null) ? this.height : options.height;
	this.el = (options.el == null) ? this.el : options.el;
	this.id = (options.id == null) ? Ext.id() : options.id;
	this.idsEl = (options.idsEl == null) ? this.idsEl : options.idsEl;
	this.textEl = (options.textEl == null) ? this.textEl : options.textEl;
	this.INPUT_ID += this.id;
	this.init();
	this.expandAll();
};

UserSelector.prototype = {
	url : '',
	params : {},
	id : null,
	multiSel : false,// 是否可以多选
	width : 580,
	height : 420,
	el : 'emp_sel_container',
	win : {},// Ext.Window
	tree : {},// Ext.Tree.TreePanel
	treeFilter : {},
	INPUT_ID : 'user_name_input_', // 姓名输入框ID

	setUrl : function(url) {
		this.url = url;
	},

	setBaseParams : function(baseParams) {
		this.store.baseParams = baseParams;
	},

	addParam : function(key, value) {
		this.params[key] = value;
	},

	init : function() {
		var _this = this;
		_this.tree = new Ext.tree.TreePanel({
					checkModel : _this.multiSel ? 'cascade' : 'single', // 单选多选
					onlyLeafCheckable : !_this.multiSel,
					rootVisible : false,
					animate : false,
					autoScroll : true,
					border : false,
					split : false,
					loader : new Ext.tree.TreeLoader({
								dataUrl : _this.url,
								baseAttrs : {
									uiProvider : Ext.tree.TreeCheckNodeUI
								}
							}),
					root : new Ext.tree.AsyncTreeNode({
								id : '0',
								text : 'root'
							}),
					listeners : {
						"beforeappend" : function(t, parent, node) {
							_this.iconSet(node);
						}
					}
				});

		_this.tree.getChecked = function(a, startNode) {
			startNode = startNode || this.root;
			var r = [];
			var f = function() {
				// 只能得选中的员工，部门排除
				if (this.attributes.checked && this.attributes.type == 'user') {
					r.push(!a ? this : (a == 'id'
							? this.id
							: this.attributes[a]));
				}
			}
			startNode.cascade(f);
			return r;
		}

		_this.tree.on("check", function(node, checked) {
					var ids = _this.tree.getChecked('id');
					var txts = _this.tree.getChecked('text');
					Ext.get(_this.textEl).dom.value = txts;
					Ext.get(_this.idsEl).dom.value = ids;
				});
		//单选情况下，双击用户节点，关闭对话框
		_this.tree.on('dblclick', function(node) {
			if(!_this.multiSel && node.attributes.type == 'user') {
				Ext.get(_this.textEl).dom.value = node.text;
				Ext.get(_this.idsEl).dom.value = node.id;
				_this.win.hide();
			}
		});
		

		// tree过虑
		_this.treeFilter = new Ext.tree.TreeFilter(_this.tree, {
					clearBlank : true,
					autoClear : true
				});

		var tbar = []; // 工具条
		tbar.push(this.tbarItems());
		tbar.push({// 查询按钮
			text : '查询',
			handler : function() {
				var text = Ext.get(_this.INPUT_ID).dom.value;
				_this.treeFilter.filterBy(function(n) {
							if (n.text.indexOf(text) == -1) {
								return false;
							} else {
								return true;
							}
						});
			}
		});
		tbar.push({// 查询按钮
			text : '关闭',
			handler : function() {
				_this.win.hide();
			}
		});
		
		_this.win = new Ext.Window({
					id : _this.id,
					el : _this.el,
					tbar : tbar,
					border : true,
					layout : 'fit',
					modal : true,
					plain : false,
					width : _this.width,
					height : _this.height,
					closeAction : 'hide',
					items : [_this.tree],
					buttons : []
				});
	},

	
	tbarItems : function() {
		var div = document.createElement("div");
		var txt = document.createElement("input");
		div.style.margin = "2px;";
		div.style.padding = "2px";
		txt.type = "text";
		txt.size = 10;
		txt.name = "model.name";
		txt.id = this.INPUT_ID;
		var msg = document.createTextNode(" 部门/姓名:");
		div.appendChild(msg);
		div.appendChild(txt);
		return div;
	},

	show : function(reload) {
		if (reload) {
			var input = Ext.getDom(this.INPUT_ID);
			if (input) {
				input.value = '';
			}
			this.treeFilter.clear();
		}
		// 设置TreeNode选框中的状态
		var str = Ext.get(this.idsEl).dom.value;
		if (str) {
			var ids = str.split(',');
			for (var i = 0; i < ids.length; i++) {
				var node = this.tree.nodeHash[ids[i]];
				if(this.multiSel) {
				  node.ui.check(true);
				} else {
					node.select();
				}
			}
		}
		this.win.show();
	},
	/**
	 * 设置图标
	 */
	iconSet : function(node) {
		if (node.attributes.type == 'user') { // 用户
			node.attributes.icon = URL_PREFIX + "/images/icons/user.gif";
		}
		if (node.attributes.type == '0') { // 部门
			node.attributes.icon = URL_PREFIX + "/images/icons/folder.gif";
		}
		if (node.attributes.type == '1') { // 单位
			node.attributes.icon = URL_PREFIX + "/images/icons/house.gif";
		}
	},
	expandAll : function() {
		// 不用延时加载的组件
		this.win.render();
		// 树展开
		this.tree.expandAll();
	}
};
