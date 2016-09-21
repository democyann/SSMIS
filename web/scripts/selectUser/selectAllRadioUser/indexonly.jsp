<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/meta.jsp" %>
<%@include file="/common/ec.jsp" %>
<title>选择用户</title>
    <style type="text/css">
        #ec_main_content
        {
            width: 99% !important;
        }
    </style>
</head>
<body>
<ec:table style="" items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
		action="onlyRadiocheck.do"
		useAjax="true" doPreload="false"
		maxRowsExported="10000000" 
		pageSizeList="15,50,all" 
		editable="false" 
		sortable="false"	
		rowsDisplayed="15"	
		generateScript="true"	
		resizeColWidth="false"	
		classic="false"	
		width="100%"
		height="350px"	
		minHeight="350"
		toolbarContent="navigation|pagesize|refresh|status"
	>
	<ec:row>
	    <ec:column width="30" property="_s" title="√" style="text-align:center">
            <input type="hidden" value="${item.mobile}" class="mobile-${item.id}"/>
            <input type="hidden" value="${item.email}" class="email-${item.id}"/>
	    	<input type="radio" name="selectedItems" id="${item.id}" value="${item.id}" class="checkbox" onclick="saveSel(this)"/>
	    </ec:column>
	    <ec:column width="50" property="_No" title="No." value="${GLOBALROWCOUNT}" style="text-align:center" />
		<ec:column width="80" property="name" title="姓名" styleClass="name-${item.id}"/>
		<ec:column width="140" property="dept.name" title="处室" styleClass="dept-${item.id}"/>
		<ec:column width="140" property="dept.id" style="display:none"  title="处室Id" styleClass="deptId-${item.id}"/>
		<ec:column width="80" property="position.name" title="职位" />
	</ec:row>
	</ec:table>
	
	<script type="text/javascript">

           function _selMap()
           {
               return window.parent._selMap;
           }

        function saveSel(item) {
            if ($(item).is(":checked")) {
                if( window.parent.onlyOne && getSelCount()>=1)
                {
                    alert("当前操作只允许单选!");
                    $(item).attr("checked","");
                    return;
                }
                var name = $(".name-" + item.id).html();
                var deptName = $(".dept-" + item.id).html();
                var deptId = $(".deptId-" + item.id).html();
                var mobile = $(".mobile-"+item.id).val();
                var email = $(".email-"+item.id).val();
               // alert(deptId);
                _selMap()[item] = {id:item.id, name:name, deptId:deptId,deptName:deptName,mobile:mobile,email:email};

            } else {
                _selMap()[item.id] = null;
            }
        }

        //加载完成后执行
        function afterFillForm() {
            jQuery(":checkbox").each(function (i) {
                this.checked = _selMap()[this.id];
            });
        }

        $(function(){
            afterFillForm();
        })

        function getSelCount()
        {
            var length = 0;
            $.each(_selMap(),function(i,n){
                if(n)
                length++;
            })
            return length;

        }

           function selectAll() {
               var items = $("#ec_table_body input:checkbox:not(:checked)");
               items.each(function () {
                   $(this).attr("checked", true);
                   saveSel(this);
               })
           }

	</script>
</body>
</html>