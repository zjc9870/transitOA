var Role = {
	init : function() {
		Role.initRoleList();
		Role.updateRoleFunction();
		Role.saveRole();
		Role.updateRole();
		Role.deleteRole();
	},
	initTreeView : function() {
		$.jstree.destroy();
		$('#function-tree').jstree({
			'plugins' : [ "wholerow", "checkbox", "types" ],
			'core' : {
				"themes" : {
					"responsive" : false
				},
				'data' : function(node, cb) {
					var roleId=$(".list-group-item.active").data("id");
					AjaxTool.post("role/getFunctionTree",{
						roleId:roleId
					},function(data){
						cb(data);
					});
				}
			},
			"types" : {
				"default" : {
					"icon" : "fa fa-folder icon-state-warning icon-lg"
				},
				"file" : {
					"icon" : "fa fa-file icon-state-warning icon-lg"
				}
			}
		});
	},
	initRoleList:function(){
		$(".list-group-item").unbind("click");
		$(".list-group-item").bind("click",function(){
			var id=$(this).data("id");
			var preId=$(".list-group-item.active").data("id");
			if(id==preId){
				return;
			}
			$(".list-group-item.active").removeClass("active");
			$(this).addClass("active");
			Role.initTreeView();
		});
	},
	updateRoleFunction:function(){
		$(".save-function-button").click(function(){
			var $list=$(".list-group-item.active");
			if($list.length==0){
				Toast.show("角色提醒","请选择角色");
				return;
			}
			SweetAlert.swal("修改功能提醒","","warning",{
				showCancelButton:true
			},function(){
				var instance = $('#function-tree').jstree(true);
				var selectedId = instance.get_selected();
				var functionIds = "";
				if (instance.get_selected() == false) {
					functionIds = "";
				} else {
					functionIds = selectedId[0];
					for (var i = 1; i < selectedId.length; i++) {
						//获取node
						var node=instance.get_node(selectedId[i]);
						//判断是否是叶子节点
						if(instance.is_leaf(node)){
							functionIds += "," + selectedId[i];
						}
					}
				}
				var roleId=$list.data("id");
				AjaxTool.post("role/updateRoleFunctions",{
					roleId : roleId,
					functionIds:functionIds
				},function(response) {
					Toast.show("提示",response.message);
				});
			});
		});
	},
	saveRole:function(){
		$(".save-button").click(function(){
			SweetAlert.swalInput("增加角色","请输入角色名","请输入角色名",function(inputValue){
				AjaxTool.post("role/save",{name:inputValue},function(response){
                	if(response.result){
                		Toast.show("角色提醒","角色保存成功");
                		var html='<a href="javascript:;" class="list-group-item" data-id="'+response.obj.id+'"><h4 class="list-group-item-heading">'+response.obj.name+'</h4></a>';
                		$(".list-group").append(html);
                		Role.initRoleList();
                	}else{
                		Toast.show("角色提醒","角色保存失败");
                	}
                });
			});
		});
	},
	updateRole:function(){
		$(".update-button").click(function(){
			var $list=$(".list-group-item.active");
			if($list.length==0){
				Toast.show("角色提醒","请选择角色");
				return;
			}
			var text=$list.find("h4").text();
			var id = $list.data("id");
			SweetAlert.swalInput("增加角色","修改的角色名:"+text,"请输入角色名",function(inputValue){
				AjaxTool.post("role/update",{id:id,name:inputValue},function(response){
                	if(response.result){
                		Toast.show("角色提醒","角色修改成功");
                		$list.find("h4").text(inputValue);
                	}else{
                		Toast.show("角色提醒","角色修改失败");
                	}
                });
			});
		});
	},
	deleteRole:function(){
		$(".delete-button").click(function(){
			var $list=$(".list-group-item.active");
			if($list.length==0){
				Toast.show("角色提醒","请选择角色");
				return;
			}
			SweetAlert.swal("删除角色提醒","","warning",{
				showCancelButton:true
			},function(){
				var $list=$(".list-group-item.active");
				if($list.length==0){
					Toast.show("角色提醒","请选择角色");
					return;
				}
				var id=$list.data("id");
				AjaxTool.post("role/delete",{id:id},function(response){
					if(response.result){
						$list.remove();
						Toast.show("角色提醒","角色删除成功");
					}else{
						Toast.show("角色提醒","角色删除失败");
					}
				});
			});
		});
	}
};

jQuery(document).ready(function() {
	Role.init();
});