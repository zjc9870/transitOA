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
			'targets' : [ 0, 11 ]
		}, {
			"searchable" : false,
			"targets" : [ 0, 11 ]
		}, {
			"width" : "30px",
			"targets" : 0
		}, {
			"width" : "220px",
			"targets" : 11
		} ], [ [ 1, "asc" ] ]);
		
		
		User.initModal();
		User.initSaveUpdate();
		User.userRoleUpdateSubmit();
		User.userDepartmentUpdateSubmit();
		User.userAvatarSave();
	},
	initModal:function(){
		//初始化modal,增加/修改/删除/批量删除/部门/角色/头像
		DatatableTool.initModal(function(){
			DatatableTool.modalShow("#user-modal", "#user-form");
			$("#save").removeClass("hidden");
			$("#update").addClass("hidden");
		},function(id){
			DatatableTool.modalShow("#user-modal", "#user-form");
			
			$("#save").addClass("hidden");
			$("#update").removeClass("hidden");
			var mTable = $('#user-table').DataTable();
			var tr = mTable.row("#" + id);
			var data = tr.data();
			var username = data[1];
			var password = data[2];
			var fullName = data[3];
			var sex = data[4];
			var phone = data[5];
			var email = data[6];
			User.inputId.val(id);
			User.inputUsername.val(username);
			User.inputPassword.val(password);
			User.inputFullName.val(fullName);
			User.inputEmail.val(email);
			User.inputPhone.val(phone);
			if(sex=='男'){
				User.inputSex.eq(0).attr("checked",true);
			}else if(sex=='女'){
				User.inputSex.eq(1).attr("checked",true);
			}
		},function(id){
			DatatableTool.deleteRow("user-table","user/delete",id);
		},function(ids){
			DatatableTool.deleteRows("user-table","user/batchDelete",ids);
		});
		DatatableTool.initAddModal(function(id){
			//部门
			DatatableTool.modalShow("#green-modal", "#user-department-form");
			$("input[name='id']").val(id);
			User.requestDepartmentCheckBox(id);
		},function(id){
			//角色
			DatatableTool.modalShow("#purple-modal", "#user-role-form");
			$("input[name='id']").val(id);
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
		});
	},
	initSaveUpdate:function(){
		//绑定保存和修改按钮
		DatatableTool.bindSaveAndUpdate(function(){
			DatatableTool.saveRow("user/save",$("#user-form").serialize(),"user-table",function(rowNode,response){
				$("#user-modal").modal('hide');
				User.initModal();
			});
		},function(){
			DatatableTool.updateRow("user/update",$("#user-form").serialize(),"user-table",function(rowNode,response){
				$("#user-modal").modal('hide');
				User.initModal();
			});
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
			var id=$("input[name='id']").val();
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
			var id=$("input[name='id']").val();
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
	userAvatarSave : function(){
		$("#user-avatar-save").click(function(){
			var ctx=$("input[name='ctx']").val();
			var userId=$("input[name='userAvatarId']").val();
			var fileIds=new Array(0);
			var fileId=$("#avatar").attr("id");
			fileIds.push(fileId);
			FileTool.ajaxFileUpload("user/uploadAvatar",fileIds,{
				userId:userId
			},function(response){
				Toast.show("用户头像提醒",response.message);
				if(response.result){
					$(".user-avatar-img").attr("src","user/showAvatar?userId="+userId+"&uuid="+Tools.getUuid());
				}
			});
		});
	},
	requestRoleCheckBox:function(){
		var id=$("input[name='id']").val();
		AjaxTool.post("role/getRoleCheckboxHtml",{
			userId:id
		},function(response){
			$("#role-checkbox").html(response.html);
		});
	},
	requestDepartmentCheckBox:function(){
		var id=$("input[name='id']").val();
		AjaxTool.post("department/getDepartmentCheckboxHtml",{
			userId:id
		},function(response){
			$("#department-checkbox").html(response.html);
		});
	}
};

jQuery(document).ready(function() {
	User.init();
});