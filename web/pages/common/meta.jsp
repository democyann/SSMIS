<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<link rel="shortcut icon" href="${ctx}/images/favicon.ico" /> 

<script type='text/javascript' src='${ctx}/scripts/jquery/jquery-1.7.2.js' ></script>
<script src="${ctx}/pages/common/pinyin.js" type="text/javascript"></script>
<LINK href="${ctx}/styles/style.css" type='text/css' rel='stylesheet'>
<script language="javascript" src="${ctx }/scripts/lodop/LodopFuncs.js"></script>
 <object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop64.exe"></embed>
</object>

<script type="text/javascript">


	var clicktime=0;
	$(function(){
		var jqueryForm = $('#save');
		if(jqueryForm ==undefined){
			return;
		}
		jqueryForm.submit = function(){
			if($("#save").validate().form()){
			clicktime++;
			//alert(clicktime);
			if(clicktime!=1){
				return false;
			}
			 
			}
		}
	
	});
	/* 打印功能 */
	var LODOP; //声明为全局变量
	var sfd = '';
	var title ="";//标题
	
	/**志超写的，所见即所得的打印方法*/
	function printURL(){
		 CreateTwoFormPage();
		 LODOP.PREVIEW();	
	}
	/**志超写的*/
	function CreateTwoFormPage(){
		    LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
		    LODOP.PRINT_INIT("出入境检疫处理申请单(进境活动物类)");
		    LODOP.ADD_PRINT_RECT("0%","0%","100%","100%",0,1);
			LODOP.SET_PRINT_PAGESIZE(1, 0, 0, "A4");
			sfd = '<table border="1" align="center" bordercolor="black" style="border-collapse:collapse;" width="90%" style="text-align:center" >';
			sfd += document.getElementById("fineTable").innerHTML;
			sfd += '</table>';
			LODOP.ADD_PRINT_TABLE("10%","0%","100%","100%",sfd);
	}
	
	/**在原有显示基础上加上头部和尾部的打印方法，一个table使用的*/
	function printURLs(title1,company,isHaveDate){
		 title =title1;
		 CreateTwoFormPages(company,isHaveDate);
		 LODOP.PREVIEW();	
	}
	/**一个table使用的*/
	function CreateTwoFormPages(company,isHaveDate){
		    var imageUrl="<img border='0' src='${ctx}/images/icons/printTop7.jpg'>";
		    LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
		    
		    
			LODOP.SET_PRINT_PAGESIZE(1, 0, 0, "A4");
			LODOP.ADD_PRINT_IMAGE(50,1,"94%","100%",imageUrl);//页头的图片
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.ADD_PRINT_IMAGE("90%",0,"93%","90%","<img border='0' src='${ctx}/images/icons/printBottom3.jpg'>");//页尾的双横线
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.SET_PRINT_STYLE("FontSize",12);
			LODOP.SET_PRINT_STYLE("Bold",1);
			
			var  pymc="Shijiazhuang RUIHAI Quarantine Technology Services Ltd.";
			//var company = "${companyName}";
			if(company!="石家庄瑞海检疫技术服务有限公司"){
				
				//特殊--机场分公司
				if(company=="机场分公司"){
					pymc="Shijiazhuang RUIHAI Quarantine Technology Services Ltd. Zhengding Airport Branch";
				}else if(company=="黄骅港分公司"){//特殊--黄骅港分公司
					pymc="Shijiazhuang RUIHAI Quarantine Technology Services Ltd.Huanghua Port Branch";
				}else if(company=="唐山海港分公司"){//特殊--唐山海港分公司
					pymc="Shijiazhuang RUIHAI Quarantine Technology Services Ltd.Tangshan Port Branch";
				}else if(company=="第一分公司"){//特殊--第一分公司
					pymc="             Shijiazhuang RUIHAI Quarantine Technology Services Ltd. First Branch";
				}else if(company=="三河市燕郊分公司"){//特殊--三河市燕郊分公司
					pymc="             Shijiazhuang RUIHAI Quarantine Technology Services Ltd.Yanjiao Branch";
				}else{//有规律的分公司 
				    	company = company.substr(0,company.length-3);//获得分公司
				       //  alert(company);
						pymc="Shijiazhuang RUIHAI Quarantine Technology Services Ltd." + codefans_net_CC2PY(company)+" Branch";
				}
			
			
				LODOP.ADD_PRINT_TEXT(77,"75%","100%","100%",company);
				LODOP.SET_PRINT_STYLE("FontSize",10);
				LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
				LODOP.ADD_PRINT_TEXT(99,"18%","100%","100%",pymc);//页首的动态拼音
				LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			}else{
				LODOP.ADD_PRINT_TEXT(77,"60%","100%","100%",company);
				LODOP.SET_PRINT_STYLE("FontSize",10);
				LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
				LODOP.ADD_PRINT_TEXT(99,"38%","100%","100%",pymc);//页首的动态拼音
				LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			}
			
			LODOP.ADD_PRINT_TEXT("92%",40,"100%","100%",title);//页尾的标题
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			var d = new Date(); // 获得当前日期
			var vYear = d.getFullYear();
			var vMon = d.getMonth() + 1;
			var vDay = d.getDate();
			var dd = vYear + "年" + vMon + "月" + vDay+"日"; //拼出下面显示的日期
			
			 if(isHaveDate=='1'){//为1时，显示日期
				 LODOP.ADD_PRINT_TEXT("92%","80%","100%","100%",dd); //添加日期显示
				LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			   }
			
			
			sfd = '<table border="1" align="center" bordercolor="black" style="border-spacing: none;padding:0px;border-collapse:collapse;" width="88%" style="text-align:center" >';
			sfd += document.getElementById("fineTable").innerHTML;
			sfd += '</table>';

			LODOP.ADD_PRINT_TABLE(170,"0%","100%","76%",sfd);
	
	}
	
	
	/**在原有显示基础上加上头部和尾部的打印方法  两个table使用的*/
	function printURLs2(title1,company,isHaveDate){
		 title =title1;
		 CreateThreeFormPages(company,isHaveDate);
		 LODOP.PREVIEW();	
	}
	/**两个table使用的*/
	function CreateThreeFormPages(company,isHaveDate){
	    var imageUrl="<img border='0' src='${ctx}/images/icons/printTop7.jpg'>";
	    LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	    
	    
		LODOP.SET_PRINT_PAGESIZE(1, 0, 0, "A4");
		LODOP.ADD_PRINT_IMAGE(50,1,"94%","100%",imageUrl);//页头的图片
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.ADD_PRINT_IMAGE("90%",0,"93%","90%","<img border='0' src='${ctx}/images/icons/printBottom3.jpg'>");//页尾的双横线
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLE("FontSize",12);
		LODOP.SET_PRINT_STYLE("Bold",1);
		
		var  pymc="Shijiazhuang RUIHAI Quarantine Technology Services Ltd.";
		//var company = "${companyName}";
		if(company!="石家庄瑞海检疫技术服务有限公司"){
			
			//特殊--机场分公司
			if(company=="机场分公司"){
				pymc="Shijiazhuang RUIHAI Quarantine Technology Services Ltd. Zhengding Airport Branch";
			}else if(company=="黄骅港分公司"){//特殊--黄骅港分公司
				pymc="Shijiazhuang RUIHAI Quarantine Technology Services Ltd.Huanghua Port Branch";
			}else if(company=="唐山海港分公司"){//特殊--唐山海港分公司
				pymc="Shijiazhuang RUIHAI Quarantine Technology Services Ltd.Tangshan Port Branch";
			}else if(company=="第一分公司"){//特殊--第一分公司
				pymc="             Shijiazhuang RUIHAI Quarantine Technology Services Ltd. First Branch";
			}else if(company=="三河市燕郊分公司"){//特殊--三河市燕郊分公司
				pymc="             Shijiazhuang RUIHAI Quarantine Technology Services Ltd.Yanjiao Branch";
			}else{//有规律的分公司 
			    	company = company.substr(0,company.length-3);//获得分公司
			       //  alert(company);
					pymc="Shijiazhuang RUIHAI Quarantine Technology Services Ltd." + codefans_net_CC2PY(company)+" Branch";
			}
			LODOP.ADD_PRINT_TEXT(77,"75%","100%","100%",company);
			LODOP.SET_PRINT_STYLE("FontSize",10);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.ADD_PRINT_TEXT(99,"18%","100%","100%",pymc);//页首的动态拼音
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		}else{
			LODOP.ADD_PRINT_TEXT(77,"60%","100%","100%",company);
			LODOP.SET_PRINT_STYLE("FontSize",10);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.ADD_PRINT_TEXT(99,"38%","100%","100%",pymc);//页首的动态拼音
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		}
		
		LODOP.ADD_PRINT_TEXT("92%",40,"100%","100%",title);//页尾的标题
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		
		var d = new Date(); // 获得当前日期
		var vYear = d.getFullYear();
		var vMon = d.getMonth() + 1;
		var vDay = d.getDate();
		var dd = vYear + "年" + vMon + "月" + vDay+"日"; //拼出下面显示的日期
		
		 if(isHaveDate=='1'){//为1时，显示日期
			 LODOP.ADD_PRINT_TEXT("92%","80%","100%","100%",dd); //添加日期显示
			 LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		   }
		
		

		sfd1 = '<table id="fineTable1" align="center" bordercolor="black" style="border-spacing: none;padding:0px;border-collapse:collapse;" width="90%" style="text-align:center" >';
		sfd1 += document.getElementById("fineTable1").innerHTML;
		sfd1 += '</table>';
		LODOP.ADD_PRINT_TABLE(170,"0%","100%","100%",sfd1);
		sfd = '<table id="fineTable" border="1" align="center" bordercolor="black" style="border-spacing: none;padding:0px;border-collapse:collapse;" width="90%" style="text-align:center" >';
		sfd += document.getElementById("fineTable").innerHTML;
		sfd += '</table>';
		
		
		
		LODOP.ADD_PRINT_TABLE(230,"0%","100%","70%",sfd);

}
</script>
