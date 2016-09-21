var Systop_FileAttch_IDS = "";

var imgType = '*.jpg; *.gif; *.png';
var imgDesc = '图片(JPG; GIF; PNG)';

var fileType = '*.txt; *.doc; *.docx; *.xls; *.xlsx; *.ppt; *.pptx; *.jpg; *.gif; *.png; *.rar; *.zip; *.pdf;*.dwg';
var fileDesc = '一般文件';

/**
 * 渲染上传组件，具体属性和方法看官方文档
 * 
 * @multi:是否允许多选
 * @fileExt:文件后缀名,用于限制文件格式,本属性和fileDesc同时使用有效。
 * @fileDesc:文件描述信息,在文件选择时文件类型处出现。
 * @fileSize:上传文件大小[数字],单位是字节1M = 1024KB = 1024000Byte
 */
function renderUploader (multi, fileExt, fileDesc, fileSize) {
    if (navigator.userAgent.search(/android|iphone|ipad/i) == -1) {
        $('#systop_upload').uploadify({
		'uploader'  : URL_PREFIX + '/scripts/uploadify/uploadify.swf',
		'cancelImg' : URL_PREFIX + '/scripts/uploadify/cancel.png',
		'script'    : URL_PREFIX + '/fileattch/upLoadFile.do',
		'buttonImg' : URL_PREFIX + '/scripts/uploadify/upload_b.gif',
		'fileDataName' : 'attch',
		// 'removeCompleted' : false,
		'multi'          : multi,
		'queueID'        : 'systop-file-queue',
		'queueSizeLimit' : 5,
		'width'     : '85',
		'height'    : '25',
		'fileExt'     : fileExt,
		'fileDesc'    : fileDesc,

		'sizeLimit' : fileSize,
		'auto'      : true,
		'onComplete': function(event, ID, fileObj, response, data) {
			var htmlStr = $("#systop-uploaded-files").html();
			// icon 图标前缀
			imgStr = "<img src='" +URL_PREFIX + "/scripts/uploadify/imgs/" + getFileIcon(fileObj.type) + "' onError='loadOtherIcon(this)'>&nbsp;&nbsp;&nbsp;&nbsp;";
			// 文件信息
			baseInfo = "&nbsp;&nbsp;&nbsp;&nbsp;大小:" + fixNum(fileObj.size/(1024*1024)) + "MB&nbsp;&nbsp;&nbsp;&nbsp;" + "速度:" + fixNum(data.speed/1024) + "MB/s&nbsp;&nbsp;&nbsp;&nbsp;";
			// 删除按钮
			removeImg = "<img src='" + URL_PREFIX + "/images/icons/remove.png' onclick='deleteFile(" + response + ", true)'>";
			htmlStr += "<div id='" + response + "'>" + imgStr +  fileObj.name + baseInfo + removeImg + "</div>";
			$("#systop-uploaded-files").html(htmlStr);
			if (multi){
				Systop_FileAttch_IDS = $("#fileAttchIds").val();
				Systop_FileAttch_IDS += (response + ",");
			}else{
				Systop_FileAttch_IDS = response + ",";
			}
			$("#fileAttchIds").val(Systop_FileAttch_IDS);
		 }
  });
    }else
    {
            $('#systop_upload').uploadifive({
                'uploadScript' : URL_PREFIX + '/fileattch/upLoadFile.do',
                'buttonText':"",
                'buttonClass':"uploadButton",
                'removeCompleted':true,
                'queueID'        : 'systop-file-queue',
                'onUploadComplete':function(fileObj,response){
                    var htmlStr = $("#systop-uploaded-files").html();
                    // icon 图标前缀
                    imgStr = "<img src='" +URL_PREFIX + "/scripts/uploadify/imgs/" + getFileIcon(fileObj.type) + "' onError='loadOtherIcon(this)'>&nbsp;&nbsp;&nbsp;&nbsp;";
                    // 文件信息
                    baseInfo = "&nbsp;&nbsp;&nbsp;&nbsp;大小:" + fixNum(fileObj.size/(1024*1024)) + "MB";
                    // 删除按钮
                    removeImg = "<img src='" + URL_PREFIX + "/images/icons/remove.png' onclick='deleteFile(" + response + ", true)'>";
                    htmlStr += "<div id='" + response + "'>" + imgStr +  fileObj.name + baseInfo + removeImg + "</div>";
                    $("#systop-uploaded-files").html(htmlStr);
                   

                    if (multi){
                        Systop_FileAttch_IDS = $("#fileAttchIds").val();
                        Systop_FileAttch_IDS += (response + ",");
                    }else{
                        Systop_FileAttch_IDS = response + ",";
                    }
                    $("#fileAttchIds").val(Systop_FileAttch_IDS);
                },
                'fileDataName':"attch",
                'height':25,
                'width':80

            });
    }
}
/**
 * 将一个文件从文件列表中删除，同时删除数据库及物理文件
 * 
 * @param fileId
 */
function deleteFile(fileId, isDelFileId){
	$.ajax({
	    url: URL_PREFIX + '/fileattch/ajaxRemove.do?model.id=' + fileId,
	    type: 'GET',
	    dataType: 'json',
	    error: function(){
	    	alert('Error delete fileattch...');
	    },
	    success: function(data, textStatus){
	    	// do nothing...
	    	// alert('Success delete fileAttch...' + data.id);
	    }
	});
	if (isDelFileId){
		document.getElementById(fileId).style.display="none";
		pattenId = fileId + ",";
		Systop_FileAttch_IDS = $("#fileAttchIds").val();
		if (Systop_FileAttch_IDS.indexOf(pattenId) != -1){
			Systop_FileAttch_IDS = Systop_FileAttch_IDS.replace(pattenId, "");
			$("#fileAttchIds").val(Systop_FileAttch_IDS);
		}
	}
}

var supportFileIcon = ".txt|.doc|.docx|.xls|.xlsx|.ppt|.pptx|.jpg|.gif|.png|.rar|.zip|.pdf";
var imgExtNames = ".jpg|.gif|.png|";
/**
 * @param fileType
 *            [demo.txt]:[.txt]
 */
function getFileIcon(fileType){
	if(fileType != null && fileType.indexOf(".") == 0){
		ext = fileType.substring(1);
		if (supportFileIcon.indexOf(fileType) != -1){
			if (imgExtNames.indexOf(fileType) != -1){
				return "img.png";
			}else{
				return ext + ".png";
			}
		}else{
			return "other.png";
		}
	}
}

// 加载图片
function loadOtherIcon(img){
	img.src = URL_PREFIX + "/scripts/uploadify/imgs/other.png";
}

// 取整数
function fixNum(num){
	newNum = new Number(num);
	return newNum.toFixed(2);

}

/**
 * 显示附件列表
 * 
 * @param fileIds
 */
function viewFileAttchList(fileIds, viewDel,containerId){
	if (fileIds == null || fileIds.length ==0){
		return;
	}
	$.ajax({
	    url: URL_PREFIX + '/fileattch/findFiles.do?fileIds=' + fileIds,
	    type: 'get',
	    dataType: 'json',
	    error: function(){
	    	alert('get file list error');
	    },
	    success: function(data, textStatus){
            containerId = containerId || "#systop_file_list";
//	    	var htmlStr = $(containerId).html();
	    	var htmlStr = "";
	    	if (data != null && data.files != null){
	    		for(var i = 0; i < data.files.length; i++){
	    			// icon 图标前缀
	    			imgStr = "<img src='" +URL_PREFIX + "/scripts/uploadify/imgs/" + getFileIcon(data.files[i].ext) + "' onError='loadOtherIcon(this)'>&nbsp;&nbsp;";
	    			// 文件信息
//	    			baseInfo = "<a href='" + URL_PREFIX + data.files[i].path + "' target='_blank'>" + data.files[i].name + "</a>";
	    			baseInfo = "<a href='" + URL_PREFIX +  "/fileattch/downLoad.do?model.id="+ data.files[i].id+ "' target='_blank' class='attach-link'>" + data.files[i].name + "</a>";
	    			baseInfo += "&nbsp;&nbsp;&nbsp;&nbsp;大小:" + fixNum(data.files[i].totalBytes/(1024*1024)) + "MB";
	    			// 删除按钮
	    			removeImg = "";
	    			if (viewDel){
	    				removeImg = "<img src='" + URL_PREFIX + "/images/icons/remove.png' onclick='deleteFile(" + data.files[i].id + ", true)'>";
	    			}
	    			htmlStr += "<div id='" + data.files[i].id + "'>" + imgStr + baseInfo + removeImg + "</div>";
	    		}

                if(typeof currentPanel ==="undefined"){
                    $(containerId).html(htmlStr);
                }
                else{
                    $(containerId,currentPanel).html(htmlStr);
                }
	    	}


            if(typeof doAfterAttachLoad !=="undefined"){
                doAfterAttachLoad();
            }
	    }
	});
}

function viewFileAttchList1(fileIds, viewDel,containerId){
	if (fileIds == null || fileIds.length ==0){
		return;
	}
	$.ajax({
	    url: URL_PREFIX + '/fileattch/findFiles.do?fileIds=' + fileIds,
	    type: 'get',
	    dataType: 'json',
	    error: function(){
	    	alert('get file list error');
	    },
	    success: function(data, textStatus){
            containerId = containerId || "#systop_file_list";
//	    	var htmlStr = $(containerId).html();
	    	var htmlStr = "";
	    	if (data != null && data.files != null){
	    		for(var i = 0; i < data.files.length; i++){
	    			// icon 图标前缀
	    			imgStr = "<img src='" +URL_PREFIX + "/scripts/uploadify/imgs/" + getFileIcon(data.files[i].ext) + "' onError='loadOtherIcon(this)'>&nbsp;&nbsp;";
	    			// 文件信息
//	    			baseInfo = "<a href='" + URL_PREFIX + data.files[i].path + "' target='_blank'>" + data.files[i].name + "</a>";
	    			baseInfo = "<a href='" + URL_PREFIX +  "/fileattch/downLoad.do?model.id="+ data.files[i].id+ "' target='_blank' class='attach-link'>" + data.files[i].name + "</a>";
	    			baseInfo += "&nbsp;&nbsp;&nbsp;&nbsp;大小:" + fixNum(data.files[i].totalBytes/(1024*1024)) + "MB";
	    			// 删除按钮
	    			/*removeImg = "";
	    			if (viewDel){
	    				removeImg = "<img src='" + URL_PREFIX + "/images/icons/remove.png' onclick='deleteFile(" + data.files[i].id + ", true)'>";
	    			}*/
	    			htmlStr += "<div id='" + data.files[i].id + "'>" + imgStr + baseInfo  + "</div>";
	    			
	    		}

                if(typeof currentPanel ==="undefined"){
                    $(containerId).html(htmlStr);
                }
                else{
                    $(containerId,currentPanel).html(htmlStr);
                }
	    	}


            if(typeof doAfterAttachLoad !=="undefined"){
                doAfterAttachLoad();
            }
	    }
	});
}


/**
 * 公文盖章处使用，点击加载该文档
 * @param fileIds
 * @param viewDel
 * @param containerId
 */
function editFileAttchList(fileIds, viewDel,containerId){
	if (fileIds == null || fileIds.length ==0){
		return;
	}
	$.ajax({
	    url: URL_PREFIX + '/fileattch/findFiles.do?fileIds=' + fileIds,
	    type: 'get',
	    dataType: 'json',
	    error: function(){
	    	alert('get file list error');
	    },
	    success: function(data, textStatus){
            containerId = containerId || "#systop_file_list";
//	    	var htmlStr = $(containerId).html();
	    	var htmlStr = "";
	    	if (data != null && data.files != null){
	    		for(var i = 0; i < data.files.length; i++){
	    			//var URL_PREFIX="E:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp2/wtpwebapps/hebyy";
	    			// icon 图标前缀
	    			imgStr = "<img src='" +URL_PREFIX + "/scripts/uploadify/imgs/" + getFileIcon(data.files[i].ext) + "' onError='loadOtherIcon(this)'>&nbsp;&nbsp;";
	    			// 文件信息
	    			baseInfo = "<a href='javascript:addFile(\"" + data.files[i].id + "\",\"" + data.files[i].path+ "\")' class='attach-link'>" + data.files[i].name + "</a>";
	    			baseInfo += "&nbsp;&nbsp;&nbsp;&nbsp;大小:" + fixNum(data.files[i].totalBytes/(1024*1024)) + "MB";
	    			// 删除按钮
	    			removeImg = "";
	    			htmlStr += "<div id='" + data.files[i].id + "'>" + imgStr + baseInfo + removeImg + "</div>";
	    		}

                if(typeof currentPanel ==="undefined"){
                    $(containerId).html(htmlStr);
                }
                else{
                    $(containerId,currentPanel).html(htmlStr);
                }
	    	}


            if(typeof doAfterAttachLoad !=="undefined"){
                doAfterAttachLoad();
            }
	    }
	});
}

/**
 * 显示附件列表
 * 
 * @param fileIds
 */
function viewFileAttchListTimes(times, fileIds, viewDel){

	
	if (fileIds == null || fileIds.length ==0){
		return;
	}

	$.ajax({
	    url: URL_PREFIX + '/fileattch/findFiles.do',
	    data :{'fileIds': fileIds} ,
	    type: 'post',
	    dataType: 'json',
	    error: function(){
	    	alert('get file list error');
	    },
	    success: function(data, textStatus){
	    	/*var htmlStr = $("#systop_file_list0"+times).html();*/
	    	var htmlStr="";
	    	
	    	if (data != null && data.files != null){
	    		
	    		for(var i = 0; i < data.files.length; i++){
	    			
	    			// icon 图标前缀11
	    			imgStr = "<img src='" +URL_PREFIX + "/scripts/uploadify/imgs/" + getFileIcon(data.files[i].ext) + "' onError='loadOtherIcon(this)'>&nbsp;&nbsp;";
	    			// 文件信息
	    			baseInfo = "<a href='" + URL_PREFIX +  "/fileattch/downLoad.do?model.id="+ data.files[i].id+ "' target='_blank'>" + data.files[i].name + "</a>";
	    			//baseInfo += "&nbsp;&nbsp;&nbsp;&nbsp;大小:" + fixNum(data.files[i].totalBytes/(1024*1024)) + "MB";
	    			
	    			var reg = /(\.flv)$/; 
	    			var name = data.files[i].name;
	    			if(name.match(reg)){
	    				baseInfo +="&nbsp;&nbsp;<a target='_blank' href='" + URL_PREFIX + "/tpf/material/video.do?fileId="+data.files[i].id+"' ><span style='color: green;'>在线观看</span></a>";
	    			}
	    			
	    			// 删除按钮
	    			removeImg = "";
	    			if (viewDel){
	    				var fId = data.files[i].id;
	    				fId="\""+fId+"\"";
	    				removeImg = "<img src='" + URL_PREFIX + "/images/icons/remove.png' onclick='deleteFile(" + fId + ", true)'>";
	    			}
	    			htmlStr =htmlStr+ "<div id='" + data.files[i].id + "'>" + imgStr + baseInfo + removeImg + "</div>";
	    			
	    		}
	    		$("#systop_file_list0"+times).html(htmlStr);
	    		
	    	}
	    }
	});
}
/**
 * 显示附件列表
 * 
 * @param fileIds
 */
function viewFileAttchListTimes1(times, fileIds, viewDel){
	if (fileIds == null || fileIds.length ==0){
		return;
	}

	$.ajax({
	    url: URL_PREFIX + '/fileattch/findFiles.do',
	    data :{'fileIds': fileIds} ,
	    type: 'post',
	    dataType: 'json',
	    error: function(){
	    	alert('get file list error');
	    },
	    success: function(data, textStatus){
	    	/*var htmlStr = $("#systop_file_list1"+times).html();*/
	    	var htmlStr="";
	    	
	    	if (data != null && data.files != null){
	    		
	    		for(var i = 0; i < data.files.length; i++){
	    			
	    			// icon 图标前缀11
	    			imgStr = "<img src='" +URL_PREFIX + "/scripts/uploadify/imgs/" + getFileIcon(data.files[i].ext) + "' onError='loadOtherIcon(this)'>&nbsp;&nbsp;";
	    			// 文件信息
	    			baseInfo = "<a href='" + URL_PREFIX +  "/fileattch/downLoad.do?model.id="+ data.files[i].id+ "' target='_blank'>" + data.files[i].name + "</a>";
	    			//baseInfo += "&nbsp;&nbsp;&nbsp;&nbsp;大小:" + fixNum(data.files[i].totalBytes/(1024*1024)) + "MB";
	    			
	    			var reg = /(\.flv)$/;
	    			var name = data.files[i].name;
	    			if(name.match(reg)){
	    				baseInfo +="&nbsp;&nbsp;<a target='_blank' href='" + URL_PREFIX + "/tpf/material/video.do?fileId="+data.files[i].id+"' ><span style='color: green;'>在线观看</span></a>";
	    			}
	    			
	    			// 删除按钮
	    			removeImg = "";
	    			if (viewDel){
	    				var fId = data.files[i].id;
	    				fId="\""+fId+"\"";
	    				removeImg = "<img src='" + URL_PREFIX + "/images/icons/remove.png' onclick='deleteFile(" + fId + ", true)'>";
	    			}
	    			htmlStr =htmlStr+ "<div id='" + data.files[i].id + "'>" + imgStr + baseInfo + removeImg + "</div>";
	    			
	    		}
	    		$("#systop_file_list1"+times).html(htmlStr);
	    	
	    	}
	    }
	});
}
