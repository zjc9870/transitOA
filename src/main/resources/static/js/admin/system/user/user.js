var sendInputId;
var ids = [];
var User = {
	inputId : $("input[name='id']"),
	inputUsername : $("input[name='username']"),
	inputPassword : $("input[name='password']"),
	inputFullName : $("input[name='fullName']"),
	inputEmail : $("input[name='email']"),
	inputPhone : $("input[name='phone']"),
	inputSex : $("input[name='sex']"),
	init : function() {
		var mTable=DatatableTool.initDatatable("user-table", [ {
			'orderable' : false,
			'targets' : [ 0, 8 ]
		}, {
			"searchable" : false,
			"targets" : [ 0, 8 ]
		}, {
			"width" : "30px",
			"targets" : 0
		}, {
			"width" : "220px",
			"targets" : 8
		} ], [ [ 1, "asc" ] ]);
		
		
		User.initModal();
		User.initSaveUpdate();
		User.userRoleUpdateSubmit();
		User.userDepartmentUpdateSubmit();
		User.fjsc();
		User.fjck();
	},
	initModal:function(){
		//初始化modal,增加/修改/删除/批量删除/部门/角色/头像
		DatatableTool.initModal(function(){
			DatatableTool.modalShow("#user-modal", "#user-form");
			User.getFormPage(-1);
			
			$("#save").removeClass("hidden");
			$("#update").addClass("hidden");
		},function(id){
			DatatableTool.modalShow("#user-modal", "#user-form");
			User.getFormPage(id);
			
			$("#save").addClass("hidden");
			$("#update").removeClass("hidden");
		},function(id){
			DatatableTool.deleteRow("user-table","user/delete",id);
		},function(ids){
			DatatableTool.deleteRows("user-table","user/deleteBatch",ids);
		});
		DatatableTool.initAddModal(function(id){
			//部门
			DatatableTool.modalShow("#green-modal", "#user-department-form");
			// $("input[name='id']").val(id);
			sendInputId = id;
			User.requestDepartmentCheckBox(id);
		},function(id){
			//角色
			DatatableTool.modalShow("#purple-modal", "#user-role-form");
			// $("input[name='id']").val(id);
			sendInputId = id;
			User.requestRoleCheckBox(id);
		},function(id){
			//头像
			DatatableTool.modalShow("#yellow-modal", "#user-avatar-form");
			$("input[name='userAvatarId']").val(id);
			AjaxTool.get("user/checkAvatar",{userId:id},function(response){
				if(response.result){
					$(".user-avatar-img").attr("src","user/showAvatar?userId="+id);
				}else{
					$(".user-avatar-img").attr("src","/images/avatar.png");
				}
			});
			var uploader=$("#user-avatar-form").FileUpload({
				url:"user/uploadAvatar",
				isMultiFile:true
			});
			uploader.done(function(data){
				if(data.result){
					$(".user-avatar-img").attr("src","user/showAvatar?userId="+data.result.obj+"&uuid"+Tools.getUUID());
				}
			});
		});
	},
	initSaveUpdate:function(){
		//绑定保存和修改按钮
		DatatableTool.bindSaveAndUpdate(function(){
			if(userValidator.form()) {
				DatatableTool.saveRow("user/save",$("#user-form").serialize()+"&attachmentId="+ids+"&ssgsId="+$("#ssgs").val(),"user-table",function(rowNode,response){
					$("#user-modal").modal('hide');
                    ids = [];
					User.initModal();
				});
			}
		},function(){
			DatatableTool.updateRow("user/update",$("#user-form").serialize()+"&attachmentId="+ids+"&ssgsId="+$("#ssgs").val(),"user-table",function(rowNode,response){
				$("#user-modal").modal('hide');
                ids = [];
				User.initModal();
			});
		});
	},
	getFormPage:function(id){
		AjaxTool.html("user/userFormPage",{
			id:id
		},function(html){
			$("#user-modal .modal-body").html(html);
		});
	},
	userRoleUpdateSubmit : function() {
		$("#role-save").click(function(){
			var roleid = "";
			var roleName="";
			$("#purple-modal input:checked").each(function(index) {
				if (index == 0) {
					roleid = $(this).val();
					roleName=$(this).parent().find("label").text();
				} else {
					roleid += "," + $(this).val();
					roleName+= "," +$(this).parent().find("label").text();
				}
			});
			// var id=$("input[name='id']").val();
			id = sendInputId;
			AjaxTool.post("user/updateUserRole", {
				"userId" : id,
				"roleId" : roleid
			}, function(response) {
				$("#purple-modal").modal('hide');
				Toast.show("角色提醒",response.message);
				if (response.result) {
					var mTable=$("#user-table").DataTable();
					var tr = mTable.row("#"+id);
					var data = tr.data();
					data[7] = roleName;
					mTable.row("#" + id).data(data);
					User.initModal();
				}
			});
		});
	},
	userDepartmentUpdateSubmit : function() {
		$("#department-save").click(function(){
			var departmentId = "";
			var departmentName="";
			$("#green-modal input:checked").each(function(index) {
				if (index == 0) {
					departmentId = $(this).val();
					departmentName=$(this).parent().find("label").text();
				} else {
					departmentId += "," + $(this).val();
					departmentName+= "," +$(this).parent().find("label").text();
				}
			});
			// var id=$("input[name='id']").val();
			var id = sendInputId;
			AjaxTool.post("user/updateUserDepartment", {
				"userId" : id,
				"departmentId" : departmentId
			}, function(response) {
				$("#green-modal").modal('hide');
				Toast.show("部门提醒",response.message);
				if (response.result) {
					var mTable=$("#user-table").DataTable();
					var tr = mTable.row("#"+id);
					var data = tr.data();
					data[8] = departmentName;
					tr.data(data);
					User.initModal();
				}
			});
		});
	},
	requestRoleCheckBox:function(){
		// var id=$("input[name='id']").val();
		var id = sendInputId;
		AjaxTool.post("role/getRoleCheckboxHtml",{
			userId:id
		},function(response){
			$("#role-checkbox").html(response.html);
		});
	},
	requestDepartmentCheckBox:function(){
		// var id=$("input[name='id']").val();
		var id = sendInputId;
		AjaxTool.post("department/getDepartmentCheckboxHtml",{
			userId:id
		},function(response){
			$("#department-checkbox").html(response.html);
		});
	},
	fjsc:function () {
		$("body").on("click","#uploadFile",function () {
			console.info("test");
			DatatableTool.modalShow("#upload-modal", "#fileUploadForm");
			var uploader = $("#fileUploadForm").FileUpload({
				url: "user/uploadIndividualAttachment",
				isMultiFile: true,
			});
			uploader.done(function(data) {
				ids.push(data.result.id);
				var li = document.createElement('li');
				var span = document.createElement('span');
				span.innerHTML = 'x';
				span.setAttribute('style','margin-left:10px;color:red;font-weight:bold;cursor:pointer');
				span.setAttribute('class','delete');
				span.setAttribute('id',data.result.id);
				li.innerHTML = data.result.name;
				li.appendChild(span);
				$('#fjlb').append(li);

				var deletes = document.getElementsByClassName('delete');
				for(var i=0;i<deletes.length;i++) {
					deletes[i].onclick = function () {
						ids.splice(ids.indexOf(this.id),1);
						this.parentNode.setAttribute('style','display:none;');
					}
				}
			});
		});
	},
	// fjck:function (){
	// 	$("body").on('click','#downloadFile',function () {
	// 		var attachmentVoslist = JSON.parse($('#attachmentVoslist').val());
	// 		var i = attachmentVoslist.length;
	// 		if(i>0){
	// 			var t = i - 1;
	// 			var span2 = document.createElement('span');
	// 			span2.setAttribute('id',attachmentVoslist[t].id);
	// 			window.location = "user/downloadIndividualAttachment?attachmentVosId=" + span2.id;
	// 		}
	// 		else{
	// 			alert('无附件');
	// 		}
	// 	} );
	// }
	fjck:function (){
		$("body").on('click','#downloadFile',function () {
			if ($('#attachmentVoslist').val() == "") {
				alert('无附件');
			}
			else {
				var attachmentVoslist = JSON.parse($('#attachmentVoslist').val());
				var i = attachmentVoslist.length;
				var t = i - 1;
				var span2 = document.createElement('span');
				span2.setAttribute('id', attachmentVoslist[t].id);
				window.location = "user/downloadIndividualAttachment?attachmentVosId=" + span2.id;
			}
		});
	}
};
// var ids = [];
// $("body").on("click","#uploadFile",function () {
// 	console.info("test");
// 	debugger
// 	DatatableTool.modalShow("#upload-modal", "#fileUploadForm");
// 	var uploader = $("#fileUploadForm").FileUpload({
// 		url: "personalCon/uploadIndividualAttachment",
// 		isMultiFile: true,
// 	});
// 	uploader.done(function(data) {
// 		ids.push(data.result.id);
// 		var li = document.createElement('li');
// 		var span = document.createElement('span');
// 		span.innerHTML = 'x';
// 		span.setAttribute('style','margin-left:10px;color:red;font-weight:bold;cursor:pointer');
// 		span.setAttribute('class','delete');
// 		span.setAttribute('id',data.result.id);
// 		li.innerHTML = data.result.name;
// 		li.appendChild(span);
// 		$('#fjlb').append(li);
//
// 		var deletes = document.getElementsByClassName('delete');
// 		for(var i=0;i<deletes.length;i++) {
// 			deletes[i].onclick = function () {
// 				ids.splice(ids.indexOf(this.id),1);
// 				this.parentNode.setAttribute('style','display:none;');
// 			}
// 		}
// 	});
// });
jQuery(document).ready(function() {

	User.init();
});