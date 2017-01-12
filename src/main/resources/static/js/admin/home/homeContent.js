;

$(document).on('click', '#htdb, #htyb', function() {
	var roleName = $('#userRolesName').val();
	var fl = $(this).data("fl");
	var url = 'contract/sqjlTab';
	var bz = 'sp';
	//如果是合同申请人登录时，主页合同待办显示未提交的合同，合同已办显示已提交的合同（未审批完成）
	//否则合同待办显示显示待审批合同， 合同已办显示已审批的合同
	if(roleName.indexOf("文员") != -1){
		bz = 'sq';
		if(fl == 'dsp')
			fl = 'wtj';
		else fl = 'dsp';
	}
	setTheClickButtonSelect($(this).parent());
	getAndDisplayHtsjByFl(url, fl, bz);
});
$(document).on('click', "#dtgg, #djgz, #gwfc", function() {
	var fl = $(this).data("fl");
	setTheClickButtonSelect($(this).parent());
	getAndDisplayNewsByFl(fl);
});

$(document).on('click', '.xwbt', function() {
	var xwid = $(this).data('id');
	AjaxTool.html('news/getNewsDetail', {id : xwid}, function(html){
		$('.page-content').html(html);
	})
});

$(document).on('click', '.lcbt', function () {
	var id = $(this).data('id');
	var tabId = $(this).data('fl');
	seeConApprove(id,tabId);
})


function seeConApprove(id,tabId) {
	AjaxTool.html('contract/htspckxq',{id: id},function (html) {
		$('#portlet-box').addClass('portlet box');
		$('.portlet-body').html(html);
		switch (tabId) {
			case "ysp":
				$('.operation').attr('style','display:none');
				break;
			default:
				break;
		}
	});
}

// 点击之后把点击的按钮变绿色
function setTheClickButtonSelect(button) {
	var tabs = button.parent().children();
	for (var int = 0; int < tabs.length; int++) {
		$(tabs[int]).removeClass("tab-box-selected");
	}
	button.addClass("tab-box-selected");
}

//根据新闻的分类获取并展示新闻
function getAndDisplayNewsByFl(fl) {
	AjaxTool.post('news/getNewsList', {
		xwlx : fl
	}, function(data) {
		if (data.success) {
			var leftUlStr = "<ul class='home-content-block'>";
			var rightUlStr = "<ul>";
			var cons = data.content;
			for (var i = 0; i < cons.length; i++) {
//				leftUlStr += ";
				leftUlStr += "<li class = 'xwbt' data-id = '"+cons[i].id+"'><span>" + cons[i].tittle + "</sapn></li>";
				rightUlStr += "<li><span>" + cons[i].userName + "&nbsp;"+ cons[i].sqsj + "</sapn></li>";
			}
			leftUlStr += "</ul>";
			rightUlStr += "</ul>";
			$('#newsLeft').html(leftUlStr);
			$('#newsRight').html(rightUlStr);
		}
	})
}
// 根据显示的分类（合同待办 ：待审批（dsp），合同已办： 已审批（ysp））获取并显示合同的数据
function getAndDisplayHtsjByFl(url, fl, bzStr) {
	AjaxTool.get(url, {
		lx : fl,
		bz : bzStr
	}, function(data) {
		if (data.success) {
			var leftUlStr = "<ul class='home-content-block'>";
			var rightUlStr = "<ul>"
			var cons = data.content;
			for (var i = 0; (i < cons.length && i < 7); i++) {
				leftUlStr += "<li class = 'lcbt' data-id = '"+cons[i].id+"' data-fl = '"+fl+"'><span>" + cons[i].htbt + "</span></li>";
				rightUlStr += "<li><span>" + cons[i].userName + "&nbsp;"+ cons[i].date + "</span></li>";
			}
			leftUlStr += "</ul>";
			rightUlStr += "</ul>";
			$('#leftLc').html(leftUlStr);
			$('#rightLc').html(rightUlStr);
		}
	})
}

// 日历插件 备忘录
jQuery(document).ready(function() {
	$('#dtgg').trigger('click');
	$('#htdb').trigger('click');
	
	var userid = $('input[name = "userId"]').val();
	$(".memo").Memo({
		requestQuery : {
			url : "memo/getMemoItems",
			param : {
				userId : userid
			}
		},
		requestAdd : {
			url : "memo/addMemoItem",
			param : {
				userId : userid
			}
		},
		requestDelete : {
			url : "memo/deleteMemoItem"
		},
		requestUpdate : {
			url : "memo/updateMemoItem"
		}
	});
});
