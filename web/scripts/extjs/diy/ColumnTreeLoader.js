Ext.tree.ColumnTreeLoader = function(config) {
	config = config || {};
	Ext.tree.ColumnTreeLoader.superclass.constructor.call(this, config);
	this.addEvents("beforeCreateNode");
};

Ext.extend(Ext.tree.ColumnTreeLoader, Ext.tree.TreeLoader, {
	createNode : function(attr){
		this.fireEvent("beforeCreateNode", this, attr);
        return Ext.tree.ColumnTreeLoader.superclass.createNode.apply(this, arguments);
    }
});