var Department = {
	inputId : $("input[name='id']"),
	inputName:$("input[name='name']"),
	inputCode:$("input[name='code']"),
	inputDescription:$("input[name='description']"),
	init : function() {
		var mTable=DatatableTool.initDatatable("department-table", [ {
			'orderable' : false,
			'targets' : [ 0, 6 ]
		}, {
			"searchable" : false,
			"targets" : [ 0, 6 ]
		},{
			"width" : "100px",
			"targets" : 6
		} ], [ [ 1, "asc" ] ]);
		
		Department.initModal();
		Department.initSaveUpdate();
	},
	initModal:function(){
		//初始化modal,增加/修改/删除/批量删除
		DatatableTool.initModal(function(){
			DatatableTool.modalShow("#department-modal", "#department-form");
			Department.getFormPage(-1);
			
			$("#save").removeClass("hidden");
			$("#update").addClass("hidden");
		},function(id){
			DatatableTool.modalShow("#department-modal", "#department-form");
			Department.getFormPage(id);
			
			$("#save").addClass("hidden");
			$("#update").removeClass("hidden");
		},function(id){
			DatatableTool.deleteRow("department-table","department/delete",id);
		},function(ids){
			DatatableTool.deleteRows("department-table","department/batchDelete",ids);
		});
	},
	initSaveUpdate:function(){
		//绑定保存和修改按钮
		DatatableTool.bindSaveAndUpdate(function(){
			DatatableTool.saveRow("department/save",$("#department-form").serialize(),"department-table",function(rowNode,response){
				$("#department-modal").modal('hide');
				Department.initModal();
			});
		},function(){
			DatatableTool.updateRow("department/update",$("#department-form").serialize(),"department-table",function(rowNode,response){
				$("#department-modal").modal('hide');
				//如果更新了父部门名称，就需要把所有子部门的父部门名称改掉
				var data=response.addData;
				if(data&&data.length>0){
					for(i in data){
						$("#"+data[i]).find("td").eq(4).html(response.obj.name);
					}
				}
				Department.initModal();
			});
		});
	},
	getFormPage:function(id){
		AjaxTool.html("department/departmentFormPage",{
			id:id
		},function(html){
			$("#department-modal .modal-body").html(html);
		});
	}
};

jQuery(document).ready(function() {
	Department.init();
});