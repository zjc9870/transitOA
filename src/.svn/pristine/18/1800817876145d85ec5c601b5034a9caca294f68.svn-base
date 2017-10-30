var FileTool = function() {

	/**
	 * ajaxFileUpload插件
	 * 基本配置，ajaxFileUpload插件有个bug，即它会把现有的表单重新创建一个新的表单提交，如果表单中有"号，那么"后面的内容就提交不了了
	 */
	function ajaxFileUpload(url, fileElementsIds, data, successCallback) {
		$.ajaxFileUpload({
			secureuri : true,
			url : url,
			dataType : "json",
			fileElementId : fileElementsIds,// file标签的id
			data : data,
			success : function(result, status) {
				successCallback(result);
			}
		});
	}

	/**
	 * Uploadify 图片上传配置
	 */
	function uploadifyImg(selector, url, fileObjName, onUploadStart,
			onUploadSuccess) {
		$(selector).uploadify({
			'swf' : '/scfy/resources/plugins/uploadify/uploadify.swf',
			'uploader' : url,
			'cancelImg' : '/scfy/resources/plugins/uploadify/cancel.png',
			'folder' : '/media/picnews',
			'queueID' : 'progressbar',
			'fileSizeLimit' : '10MB',
			'fileTypeDesc' : '图片文件',
			'fileTypeExts' : '*jpg;*.jpeg;*.png;*.bmp',// 文件类型过滤
			'auto' : false,
			'multi' : false,
			'fileObjName' : fileObjName,
			'buttonText' : '选择图片',
			'onUploadStart' : function(file) {
				onUploadStart();
			},
			'onUploadSuccess' : function(file, data, response) {
				onUploadSuccess(jQuery.parseJSON(data));
			}
		});
	}

	/**
	 * Uploadify 视频上传配置
	 */
	function uploadifyVedio(selector, url, fileObjName, onUploadStart,
			onUploadSuccess) {
		$(selector)
				.uploadify(
						{
							'swf' : '/scfy/resources/plugins/uploadify/uploadify.swf',
							'uploader' : url,
							'cancelImg' : '/scfy/resources/plugins/uploadify/cancel.png',
							'folder' : '/media/picnews',
							'queueID' : 'progressbar',
							'fileSizeLimit' : '100MB',
							'fileTypeDesc' : '视频文件',
							'fileTypeExts' : '*.avi; *.wmv; *.mp4;*.mp3; *.mov; *.flv; *.mkv; *.rmvb',// 文件类型过滤
							'auto' : false,
							'multi' : false,
							'fileObjName' : fileObjName,
							'buttonText' : '选择视频',
							'removeCompleted' : false,
							'onUploadStart' : function(file) {
								onUploadStart();
							},
							'onUploadSuccess' : function(file, data, response) {
								onUploadSuccess(jQuery.parseJSON(data));
							}
						});
	}

	/**
	 * Uploadify 获取选择文件的数量
	 */
	function getUploadifyNum(selector) {
		return $(selector).data('uploadify').queueData.queueLength;
	}

	/**
	 * Jquery-file-upload插件 基础配置
	 */
	jqueryUpload = function(maxFileSizes, acceptFileTypes, url, selector,
			formData, uploadButtonSelector, doneCallback, changeCallback, submitCallback,
			failCallback) {
		var filesSelector = "#files";
		var progressSelector = "#progress .progress-bar";

		$(selector).fileupload({
			url : url,
			dataType : 'json',
			autoUpload : false,
			formData : formData
		}).on('fileuploadchange', function(e, data) {
			if(data.files.length>0){
				jqueryUploadButton(false);
			}
			if(data.files.length>1){
				$(".fileinput-filename").text(data.files.length+"个文件");
			}else{
				$(".fileinput-filename").text(data.files[0].name);
			}
			if (changeCallback) {
				changeCallback(data);
			}
		}).on('fileuploadadd', function(e, data) {
			$.each(data.files, function(index, file) {
				if (!validateFormat(acceptFileTypes, file.name)) {
					Toast.show("上传文件提醒", "文件格式有误");
					return;
				}
				if (file.size > maxFileSizes) {
					Toast.show("上传文件提醒", "文件大小超过规定");
					return;
				}
				$(progressSelector).css('width', '0%');
				data.context = $(uploadButtonSelector).bind("click",function() {
					data.submit();
				});
			});
		}).on("fileuploadsubmit", function(e, data) {
			if (submitCallback) {
				return submitCallback(e, data);
			}
			return true;
		}).on('fileuploadprogressall', function(e, data) {
			var progress = parseInt(data.loaded / data.total * 100, 10);
			$(progressSelector).css('width', progress + '%');
		}).on('fileuploaddone', function(e, data) {
			if (data.result) {
				$(filesSelector + " .upload-result").text("上传成功");
			} else {
				$(filesSelector + " .upload-result").text("上传失败");
			}
			if (doneCallback) {
				doneCallback(data.result, data);
			}
		}).on('fileuploadfail', function(e, data) {
			if (failCallback) {
				failCallback(data);
			}
		});
//		$(".fileinput-exists").hide();
//		$(".fileinput-remove").unbind("click");
//		$(".fileinput-remove").bind("click",function(){
//			jqueryUploadButton(true);
//			$(".fileinput-filename").text("");
//		});
	}
	
	function jqueryUploadButton(isRemove){
		if(isRemove){
			$(".fileinput-exists").hide();
			$(".fileinput-new").show();
		}else{
			$(".fileinput-exists").show();
			$(".fileinput-new").hide();
		}
	}

	/**
	 * Jquery-file-upload插件 文本上传
	 */
	jqueryUploadText = function(url, selector, formData, uploadButtonSelector,
			doneCallback, changeCallback, submitCallback, failCallback) {
		// 10MB,10485760,'.+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$',
		jqueryUpload(104857600, '.+(.doc|.docx|.txt|.DOC|.DOCX|.TXT)$', url,
				selector, formData, uploadButtonSelector, doneCallback,
				changeCallback, submitCallback, failCallback);
	}

	/**
	 * Jquery-file-upload插件 图片上传
	 */
	jqueryUploadImage = function(url, selector, formData, uploadButtonSelector,
			doneCallback, changeCallback, submitCallback, failCallback) {
		// 10MB,10485760,'.+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$',
		jqueryUpload(10485760,
				'.+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$',
				url, selector, formData, uploadButtonSelector, doneCallback,
				changeCallback, submitCallback, failCallback);
	}

	/**
	 * Jquery-file-upload插件 视频上传
	 */
	jqueryUploadVideo = function(url, selector, formData, uploadButtonSelector,
			doneCallback, changeCallback, submitCallback, failCallback) {
		// 100MB,104857600,'.+(.swf|.avi|.flv|.mpg|.rm|.mov|.wav|.asf|.3gp|.mkv|.rmvb)$',
		jqueryUpload(104857600, '.+(.swf|.flv|.mp4)$', url, selector, formData,
				uploadButtonSelector, doneCallback, changeCallback,
				submitCallback, failCallback);
	}

	jqueryUploadIsSelect = function(files) {
		var text = "";
		if (files == undefined) {
			text = $("#files span").eq(0).text();
		} else {
			text = $(files + " span").eq(0).text();
		}
		if (text == '') {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 验证格式
	 */
	function validateFormat(regExp, fileName) {
		var reg = new RegExp(regExp);
		if (!reg.test(fileName)) {
			return false;
		}
		return true;
	}

	/**
	 * 验证图片的格式
	 */
	function validateImage(fileName) {
		var reg = new RegExp(
				'.+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$');
		if (!reg.test(fileName)) {
			return false;
		}
		return true;
	}

	/**
	 * 验证视频的格式
	 */
	function validateVedio() {
		var reg = new RegExp(
				'.+(.swf|.avi|.flv|.mpg|.rm|.mov|.wav|.asf|.3gp|.mkv|.rmvb)$');
		if (!reg.test(fileName)) {
			return false;
		}
		return true;
	}

	return {
		ajaxFileUpload : function(url, fileElementsIds, data, successCallback) {
			ajaxFileUpload(url, fileElementsIds, data, successCallback);
		},
		uploadifyImg : function(selector, url, fileObjName, onUploadStart,
				onUploadSuccess) {
			uploadifyImg(selector, url, fileObjName, onUploadStart,
					onUploadSuccess);
		},
		uploadifyVedio : function(selector, url, fileObjName, onUploadStart,
				onUploadSuccess) {
			uploadifyVedio(selector, url, fileObjName, onUploadStart,
					onUploadSuccess);
		},
		getUploadifyNum : function(selector) {
			return getUploadifyNum(selector);
		},
		jqueryUploadText : function(url, selector, formData,
				uploadButtonSelector, doneCallback, changeCallback,
				submitCallback, failCallback) {
			jqueryUploadText(url, selector, formData, uploadButtonSelector,
					doneCallback, changeCallback, submitCallback, failCallback);
		},
		jqueryUploadImage : function(url, selector, formData,
				uploadButtonSelector, doneCallback, changeCallback,
				submitCallback, failCallback) {
			jqueryUploadImage(url, selector, formData, uploadButtonSelector,
					doneCallback, changeCallback, submitCallback, failCallback);
		},
		jqueryUploadVideo : function(url, selector, formData,
				uploadButtonSelector, doneCallback, changeCallback,
				submitCallback, failCallback) {
			jqueryUploadVideo(url, selector, formData, uploadButtonSelector,
					doneCallback, changeCallback, submitCallback, failCallback);
		},
		jqueryUploadIsSelect : function(files) {
			return jqueryUploadIsSelect(files);
		}
	}
}();
