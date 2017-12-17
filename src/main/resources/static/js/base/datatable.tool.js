var DatatableTool = function() {



	var initDatatable = function(tableId, columnDefs, order) {
		var i18nUrl = "/plugins/datatables/dataTables.chinese.txt";
		var mTable = $('#' + tableId).DataTable({
			'oLanguage' : {
				'sUrl' : i18nUrl
			},
			"pageLength" : 10,// 每页长度
			"bFilter" : true, // 搜索
			"bLengthChange" : true,// 改变每页长度
			"lengthMenu" : [ [ 10, 20, 30 ], [ 10, 20, 30 ] ],// 可以改变的每页长度
			// responsive : true,
			"bStateSave" : true,
			"columnDefs" : columnDefs,
			"order" : order,
			"processing": true,
		});
		$('#' + tableId).find('.group-checkable').change(function() {
			var set = jQuery(this).attr("data-set");
			var checked = jQuery(this).is(":checked");
			jQuery(set).each(function() {
				if (checked) {
					$(this).prop("checked", true);
				} else {
					$(this).prop("checked", false);
				}
			});
		});
		return mTable;
	}

	function initDatatableServer(tableId, ajax,columns,
			drawCallback) {
		var i18nUrl = "/plugins/datatables/dataTables.chinese.txt";
		var mTable = $('#' + tableId).DataTable({
			'oLanguage' : {
				'sUrl' : i18nUrl
			},
			"pageLength" : 20,
			"bFilter" : false, // 过滤功能
			"bLengthChange" : false, // 改变每页显示数据数量
			"ordering" : false,
			"deferRender" : true,
			"processing" : false,
			"serverSide" : true,
			"bStateSave" : true,
			"ajax" : {
				"type" : "post",
				"url" : ajax.url,
				"dataSrc" : "data",
				"data" : function(data) {
					if (ajax.data != null) {
						var param = ajax.data();
						param.start = data.start;
						param.length = data.length;
						return param;
					} else {
						return data;
					}
				}
			},
			"columns" : columns,
			"fnDrawCallback" : function(settings) {
				if (drawCallback != null) {
					drawCallback();
				}
			}
		});
		$('#' + tableId).find('.group-checkable').change(function() {
			var set = jQuery(this).attr("data-set");
			var checked = jQuery(this).is(":checked");
			jQuery(set).each(function() {
				if (checked) {
					$(this).prop("checked", true);
				} else {
					$(this).prop("checked", false);
				}
			});
		});
		return mTable;
	}
	
	function initEditorDatatable(tableId,columnDefs){
		var i18nUrl = "/plugins/datatables/dataTables.chinese.txt";
		var mTable=$("#"+tableId).DataTable({
			'oLanguage' : {
				'sUrl' : i18nUrl
			},
			"paging" : false,
			"bFilter" : false,
			"bLengthChange" : false,
			"columnDefs" : columnDefs,
			"bInfo" : false
		});
		return mTable;
	}

	var saveRow = function(url, data, tableId, successFunction,completeFunction) {
		var promise = $.ajax({
			url : url,
			data : data,
			type : "post",
			dataType : "json"
		});
		promise.then(function(response) {
			Toast.show("提示", response.message);
			if (!response.result) {
			} else {
				var mTable = $('#' + tableId).DataTable();
				if(response.data){
					var rowNode = mTable.row.add(response.data).draw().node();
					$(rowNode).attr("id", response.obj.id);
					if (successFunction != null) {
						successFunction($(rowNode), response);
					}
				}
			}
		});
		promise.then(function(){
			if(completeFunction!=null){
				completeFunction();
			}
		});
	}

	var updateRow = function(url, data, tableId, successFunction ,completeFunction) {
		var promise = $.ajax({
			url : url,
			data : data,
			type : "post",
			dataType : "json"
		});
		promise.then(function(response) {
			Toast.show("提示", response.message);
			if (!response.result) {
			} else {
				var mTable = $('#' + tableId).DataTable();
				var tr = mTable.row("#" + response.obj.id);
				var oriData = tr.data();
				var data = response.data;
				if(response.data){
					for (var i = 0; i < data.length; i++) {
						oriData[i] = data[i];
					}
					mTable.row("#" + response.obj.id).data(oriData);
					var rowNode = mTable.row("#" + response.obj.id).node();
					if (successFunction != null) {
						successFunction(rowNode, response);
					}
				}
			}
		});
		promise.then(function(){
			if(completeFunction!=null){
				completeFunction();
			}
		});
	}

	var deleteRows = function(tableId, url, ids) {
		var mTable = $('#' + tableId).DataTable();
		swal({
			title : "删除提醒",
			text : "是否确定删除？",
			type : "warning",
			allowOutsideClick : true,
			showConfirmButton : true,
			showCancelButton : true,
			confirmButtonClass : "btn-success",
			cancelButtonClass : "btn-danger",
			closeOnConfirm : true,
			closeOnCancel : true,
			confirmButtonText : "确定",
			cancelButtonText : "取消",
		}, function(isConfirm) {
			if (isConfirm) {
				var promise = $.ajax({
					url : url,
					data : {
						ids : ids
					},
					type : "post",
					dataType : "json"
				});
				promise.then(function(response) {
					Toast.show("删除提醒",response.message);
					if (response.result) {
						var idArr = ids.split(",");
						for (i in idArr) {
							$("#" + idArr[i]).addClass("row-delete");
						}
						mTable.rows(".row-delete").remove().draw();
						// 删除关联的ids
						var idArr = response.obj;
						if (idArr && idArr.length > 0) {
							for (i in idArr) {
								$("#" + idArr[i]).addClass("row-delete");
							}
							mTable.rows(".row-delete").remove().draw();
						}
					}
				});
			}
		});
	}

	var deleteRow = function(tableId, url, id) {
		var mTable = $('#' + tableId).DataTable();
		swal({
			title : "删除提醒",
			text : "是否确定删除？",
			type : "warning",
			allowOutsideClick : true,
			showConfirmButton : true,
			showCancelButton : true,
			confirmButtonClass : "btn-success",
			cancelButtonClass : "btn-danger",
			closeOnConfirm : true,
			closeOnCancel : true,
			confirmButtonText : "确定",
			cancelButtonText : "取消",
		}, function(isConfirm) {
			if (isConfirm) {
				var promise = $.ajax({
					url : url,
					data : {
						id : id
					},
					type : "post",
					dataType : "json"
				});
				promise.then(function(response) {
					Toast.show("删除提醒",response.message);
					if (response.result) {
						// 删除本身的id
						mTable.rows("#" + id).remove().draw();
						// 删除关联的ids
						var idArr = response.obj;
						if (idArr && idArr.length > 0) {
							for (i in idArr) {
								$("#" + idArr[i]).addClass("row-delete");
							}
							mTable.rows(".row-delete").remove().draw();
						}
					}
				});
			}
		});
	}

	var modalShow = function(modalId, formId) {
		$(modalId).modal('show');
        formStatus(formId, true);
		modalClose(modalId, formId);
	}

	var modalClose = function(modalId, formId) {
		$(modalId).on('hide.bs.modal', function(e) {
			resetForm(modalId,formId);
			formStatus(formId, false);
		});
	}

	var formStatus = function(formId, status) {
		if (status) {
			$(formId + " input").each(function() {
				if ($(this).val()) {
					$(this).addClass("edited");
				}
			});
			// $(formId + " input[type='text']").addClass("edited");
			// $(formId + " input[type='password']").addClass("edited");
			// $(formId + " input[type='number']").addClass("edited");
			$(formId + " textarea").addClass("edited");
		} else {
			$(formId + " input[type='text']").removeClass("edited");
			$(formId + " input[type='password']").removeClass("edited");
			$(formId + " input[type='number']").removeClass("edited");
			$(formId + " textarea").removeClass("edited");
		}
	}

	var resetForm = function(modalId,formId) {
		if (formId == null) {
			return;
		}
		$(formId + " input[type='text']").val("");
		$(formId + " input[type='password']").val("");
		$(formId + " input[type='number']").val("");
		$(formId + " input[type='hidden']").val("");
		$(formId + " textarea").val("");
		resetRadio(formId);
		resetCheckbox(formId);
		
		//如果是file上传，需要清空已经上传信息
        $(formId).FileUpload().destory();
        // 在用户管理界面修改时
		// userform这层外层modal关闭时，需要销毁内层上传modal，否则再修改人员信息会出现问题
		// 如果不做区分每次都销毁上传modal，在别的上传界面会出现新建的上传modal无法再次上传，真是见鬼了，还好我不是学前端的
		if(modalId=='#user-modal'){
            $('#fileUploadForm').FileUploadDestory();
        }
	}

	function resetRadio(formId) {
		$(formId + " input[type='radio']").attr("checked", false);
	}

	function resetCheckbox(formId) {
		$(formId + " input[type='checkbox']:checked").attr("checked", false);
	}
	
	//初始化modal:增加,修改,删除,批量删除modal
	var initModal = function(saveFunction,updateFunction,deleteFunction,batchDeleteFunction){
		initSaveModal(saveFunction);
		initUpdateModal(updateFunction);
		initDeleteModal(deleteFunction);
		initBatchDeleteModal(batchDeleteFunction);
	}
	
	//初始化附加的modal:green,purple,yellow Modal
	var initAddModal = function(greenFunction,purpleFunction,yellowFunction){
		initGreenModal(greenFunction);
		initPurpleModal(purpleFunction);
		initYellowModal(yellowFunction);
	}
	
	var initSaveModal = function(saveFunction){
		$(".save-button").unbind("click");
		$(".save-button").bind("click",function() {
			if(saveFunction){
				saveFunction();
			}
		});
	}
	
	var initUpdateModal = function(updateFunction){
		$(".update-button").unbind("click");
		$(".update-button").bind("click", function() {
			if(updateFunction){
				var id=$(this).data("id");
				updateFunction(id);
			}
		});
	}
	
	var initGreenModal = function(greenFunction){
		$(".green-button").unbind("click");
		$(".green-button").bind("click",function(){
			if(greenFunction){
				var id=$(this).data("id");
				greenFunction(id);
			}
		});
	}
	
	var initPurpleModal = function(purpleFunction){
		$(".purple-button").unbind("click");
		$(".purple-button").bind("click",function(){
			if(purpleFunction){
				var id=$(this).data("id");
				purpleFunction(id);
			}
		});
	}

	var initYellowModal = function(yellowFunction){
		$(".yellow-button").unbind("click");
		$(".yellow-button").bind("click",function(){
			if(yellowFunction){
				var id=$(this).data("id");
				yellowFunction(id);
			}
		});
	}
	
	var initDeleteModal = function(deleteFunction){
		$(".delete-button").unbind("click");
		$(".delete-button").bind("click",function(){
			if(deleteFunction){
				var id=$(this).data("id");
				deleteFunction(id);
			}
		});
	}
	
	var initBatchDeleteModal = function(batchDeleteFunction){
		$(".batch-delete-button").unbind("click");
		$(".batch-delete-button").bind("click",function(){
			var ids="";
			$(".checkboxes:checked").each(function(index){
				var id=$(this).parent().parent().parent().attr("id");
				if(index==0){
					ids=id;
				}else{
					ids+=","+id;
				}
			});
			if(ids!=''){
				if(batchDeleteFunction){
					batchDeleteFunction(ids);
				}
			}
		});
	}
	
	//绑定保存和修改
	var bindSaveAndUpdate = function(updateFunction,saveFunction){
		bindUpdate(updateFunction);
		bindSave(saveFunction);
	}
	
	var bindSave = function(saveFunction){
		$("#save").unbind("click");
		$("#save").bind("click",function(){
			if(saveFunction){
				saveFunction();
			}
		});
	}
	
	var bindUpdate = function(updateFunction){
		$("#update").unbind("click");
		$("#update").bind("click",function(){
			if(updateFunction){
				updateFunction();
			}
		});
	}
	
	return {
		initDatatable : function(tableId, columnDefs, order) {
			return initDatatable(tableId, columnDefs, order);
		},
		initDatatableServer : function(tableId, ajax, columnDefs,drawCallback) {
			return initDatatableServer(tableId, ajax,columnDefs,drawCallback);
		},
		initEditorDatatable : function(tableId,columnDefs){
			return initEditorDatatable(tableId,columnDefs);
		},
		initModal : function(saveFunction,updateFunction,deleteFunction,batchDeleteFunction){
			initModal(saveFunction,updateFunction,deleteFunction,batchDeleteFunction);
		},
		initAddModal : function(greenFunction,purpleFunction,yellowFunction){
			initAddModal(greenFunction,purpleFunction,yellowFunction);
		},
		bindSaveAndUpdate : function(saveFunction,updateFunction){
			bindSave(saveFunction);
			bindUpdate(updateFunction);
		},
		saveRow : function(url, data, tableId, successFunction,completeFunction) {
			saveRow(url, data, tableId, successFunction,completeFunction);
		},
		updateRow : function(url, data, tableId, successFunction,completeFunction) {
			updateRow(url, data, tableId, successFunction,completeFunction);
		},
		modalClose : function(modalId, formId) {
			modalClose(modalId, formId);
		},
		modalShow : function(modalId, formId) {
			modalShow(modalId, formId);
		},
		deleteRow : function(tableId, url, id) {
			deleteRow(tableId, url, id);
		},
		deleteRows : function(tableId, url, ids) {
			deleteRows(tableId, url, ids);
		},
		formStatus : function(formId, status) {
			formStatus(formId, status);
		}
	}
}();