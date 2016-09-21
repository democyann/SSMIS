<link rel="stylesheet" type="text/css" href="${ctx}/scripts/extjs/resources/css/loading.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/scripts/extjs/resources/css/ext-all.css" />
<script type="text/javascript" src="${ctx}/scripts/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="${ctx}/scripts/extjs/ext-all.js"></script>
<script type="text/javascript" src="${ctx}/scripts/extjs/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/scripts/extjs/ext-util.js"></script>

<div id="loading">
    <div class="loading-indicator">Loading...</div>
</div>

<script type="text/javascript">
Ext.onReady(function(){
	setTimeout(function(){
	        if(Ext.get('loading')) {
	          Ext.get('loading').remove();
	        } 
	    }, 250);
});
</script>
