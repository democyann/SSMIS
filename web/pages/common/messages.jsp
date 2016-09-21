<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib  prefix="s" uri="/struts-tags"%>
<%-- ActionError Messages - usually set in Actions --%>

<script type="text/javascript">

  function hiddenDiv(divId){
	  
    document.getElementById(divId).style.display="none";
    document.getElementById("hiddentext").value="2";
  }
</script>
<script type="text/javascript">
function funcTest(){
    //每隔3秒执行一次timelyFun方法
    if(document.getElementById("hiddentext").value=="1"){
    
    window.setInterval("hiddenDiv('actionMessages')",3000);
    }

}

window.onload = funcTest;
</script>
<s:if test="hasActionMessages()">
<input type="hidden" id="hiddentext" value="1"/>
</s:if>
<s:if test="!hasActionMessages()">
<input type="hidden" id="hiddentext" value="2"/>
</s:if>


<s:if test="hasActionErrors()">
    <div id="errorMessages" style="width:100%; border:1px solid red;">
    <table style="border:none; width: 100%">
    	<tr>
    	  <td  style="border:none;" width="%97">
   			<s:iterator value="actionErrors">
	          <img src="${ctx}/images/icons/warning.gif" class="icon" />
	          <s:property escape="false"/><br>
	        </s:iterator>
    	  </td>
    	  <td  style="border:none;" align="right" valign="top">
    	  	<a href="#" onclick="hiddenDiv('errorMessages')" title="关闭">
    	  		<img src="${ctx}/images/icons/close.gif"/>
    	  	</a>
    	  </td>
    	</tr>
    </table>    
   </div>
</s:if>
<%-- FieldError Messages - usually set by validation rules --%>
<s:if test="hasFieldErrors()">
   <div id="fieldErrorsMessages" style="width:100%;border:1px solid red;">  
   		<table style="border:none; width: 100%">
    	<tr>
    	  <td  style="border:none;" width="%97">
	         <s:iterator value="fieldErrors">
	           <s:iterator value="value">
	              <img src="${ctx}/images/icons/warning.gif" class="icon" />
	              <s:property escape="false"/><br>
	           </s:iterator>
	         </s:iterator>
    	  </td>
    	  <td  style="border:none;" align="right" valign="top">
    	  	<a href="#" onclick="hiddenDiv('fieldErrorsMessages')" title="关闭">
    	  		<img src="${ctx}/images/icons/close.gif"/>
    	  	</a>
    	  </td>
    	</tr>
    </table>    
   </div>
</s:if>

<s:if test="hasActionMessages()">
  <div id="actionMessages" style="width:100%;border:1px solid #00ff00;">  
   		<table style="border:none; width: 100%">
    	<tr>
    	  <td  style="border:none;" width="%97">
	         <s:iterator value="actionMessages">
	           <img src="${ctx}/images/icons/accept.gif" class="icon" />
	           <s:property escape="false"/><br>	           
	         </s:iterator>
    	  </td>
    	  <td  style="border:none;" align="right" valign="top">
    	  	<a href="#" onclick="hiddenDiv('actionMessages')" title="关闭">
    	  		<img src="${ctx}/images/icons/close.gif"/>
    	  	</a>
    	  	
    	  </td>
    	</tr>
    </table>    
   </div>
</s:if>

