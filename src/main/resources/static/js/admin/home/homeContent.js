var userRole = $('input[name="userRole"]').val().split(',');

$(document).on('click', '#htdb, #htyb', function() {
	var roleName = $('#userRolesName').val();
	var fl = $(this).data("fl");
	var url = 'contract/sqjlTab';
	var bz = 'sp';
	//如果是合同申请人登录时，主页合同待办显示未提交的合同，合同已办显示已提交的合同（未审批完成）
	//否则合同待办显示显示待审批合同， 合同已办显示已审批的合同
	if(roleName.indexOf("文员") != -1 || roleName.indexOf("其他公司发起人") !=-1){
		bz = 'sq';
		if(fl == 'dsp')
			fl = 'wtj';
		else fl = 'dsp';
	}
	setTheClickButtonSelect($(this).parent());
	getAndDisplayHtsjByFl(url, fl, bz);
});
$(document).on('click', '#wdtz, #ydtz', function(){
    var fl = $(this).data("fl");
    var url = 'meeting/hytzTab';
    setTheClickButtonSelect($(this).parent());
    getAndDisplayHytzByFl(url, fl);
});

$(document).on('click','#fwdb,#fwyb',function(){
    setTheClickButtonSelect($(this).parent());
	var roleName=$('#userRolesName').val();
	var fl=$(this).data("fl");
	// fl 指ysp和dsp
	var url; // 后台请求的地址
	var bz;// sp还是sq

    /**
	 * bz是发文审批还是发文申请，这都是待办
     * 发文机要专员是发文待办==发文未提交
	 * 发文已办==已提交
	 * 发文审核员：发文待办==未审核
	 * 发文已办==已审核
	 * 其余人：发文未读==未读
	 * 发文已读==已读
     */
    // 如果该人是发文机要专员
    if(roleName.indexOf("发文机要专员") !=-1){
		bz='sq';
        // 对于发文机要专员而言，发文待办(fl=dsp)表示未提交的发文,而fl=ysp表示已提交而dsp的发文
        fl = (fl == "dsp" ? "wtj" : "ysp")
        url='document/sqjlTab'
        getAndDisplayFwsjByFl(url,fl,bz)
	}
	// 如果该人是发文审核员
	if(roleName.indexOf("发文审核员") !=-1) {
    	bz = 'sp';
        fl = (fl == "dsp" ? "dsp" : "ysp")
        url='document/sqjlTab'
        getAndDisplayFwsjByFl(url,fl,bz)
	}
	// 如果该人既不是发文机要专员，也不是发文审核员，他就是普通用户，此时两个按钮是发文已读和发文未读
    if (roleName.indexOf("发文机要专员") == -1 && roleName.indexOf("发文审核员") == -1)
	{
		url='document/fwtzTab';
		fl = (fl=="dsp" ? 'wd' : 'yd')
		// 对一般用户而言，只有被通知或者不被通知，后台程序不需要bz
		getAndDisplayTzByfl(url,fl);
	}

});

$(document).on('click','#ycl,#dcl',function(){
	var roleName=$('#userRolesName').val();
	var fl=$(this).data("fl");
	var url='draftSw/tabRequest';
	var bz='sp';

	setTheClickButtonSelect($(this).parent());

	getAndDisplaySwCyByfl(url,fl);

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
	if(tabId== "dcy"||tabId== "ycy"){
		seeCy(id,tabId);
	}else if(tabId== "dbl"||tabId== "ybl"){
		seeBl(id,tabId);
	}else if(tabId== "dcl"||tabId== "ycl"){
		seeCl(id,tabId);
	}else if(tabId== "dps"||tabId== "yps"){
		seePs(id,tabId);
	}
	if(userRole.indexOf('集团文员') !== -1 || userRole.indexOf('其他公司发起人') !== -1 || userRole.indexOf('东交公司文员') !== -1) {
		if(tabId == "wtj") {
			seeApplyRecordE(id,tabId);
		}else {
			seeApplyRecordNE(id,tabId);
		}
	}
	else {
		seeConApprove(id,tabId);
	}
});
$(document).on('click', '.fwbt', function(){
	var id = $(this).data('id');
	var tabId = $(this).data('fl');
	// if (userRole.indexOf('机要专员') !==-1){
    if (userRole.indexOf('发文机要专员') !==-1){
		if(tabId == "wtj"){
			AjaxTool.html('document/sqjlxqE',{id: id},function (html) {
				$('#portlet-box').addClass('portlet box');
				$('.portlet-body').html(html);
				$('#back').data('tabId',tabId);
			});
		}else{
			AjaxTool.html('document/sqjlxqNE',{id: id},function (html) {
				$('#portlet-box').addClass('portlet box');
				$('.portlet-body').html(html);
				$('#back').data('tabId',tabId);
			});
		}
	}
	else{
		AjaxTool.html('document/gwspckxq',{id: id},function (html) {
			$('#portlet-box').addClass('portlet box');
			$('.portlet-body').html(html);
			switch (tabId) {
				case "ysp":
					$('.operation').attr('style','display:none');
					break;
				default:
					break;
			}
			$('#back').data('tabId',tabId);
		});
	}
});
$(document).on('click','.fwtzbt',function () {
	var id = $(this).data('id');
	var fwtzid = $(this).data('fwtzid');
	AjaxTool.html('document/fwtzNE',{id: id, fwtzid:fwtzid},function (html) {
		$('.portlet-body').html(html);
		if(tabId == "yd"){
			$('#yd').attr('style','display:none');
		}
		$('#back').data('tabId',tabId);
	});
});

$(document).on('click','.hytz', function(){
    var id = $(this).data('id');
    var hytzId = $(this).data('hytzid');
    var tabId = $(this).data('fl');
    var fl = $(this).data('lb');
    seeHyNotify(id, hytzId, tabId, fl);
});

$(function() {
    $("#dcl").trigger("click");//触发button的click事件
});

function seeApplyRecordE(id,tabId) {
	AjaxTool.html('contract/sqjlxqE',{id: id},function (html) {
		$('#portlet-box').addClass('portlet box');
		$('.portlet-body').html(html);
		$('#back').data('tabId',tabId);
	});
}

function seeApplyRecordNE(id,tabId) {
	AjaxTool.html('contract/sqjlxqNE',{id: id},function (html) {
		$('#portlet-box').addClass('portlet box');
		$('.portlet-body').html(html);
		$('#back').data('tabId',tabId);
	});

}

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

function seeHyNotify(id,hytzId,tabId,fl) {
    AjaxTool.html('meeting/hytzNE',{id: id, hytzid:hytzId},function (html) {
        $('#portlet-box').addClass('portlet box');
        $('.portlet-body').html(html);
        if(tabId == "yd"){
            $('#yd').attr('style','display:none');
        }
        if(fl.indexOf("集团") != -1){
            $(".dsgj").attr('style','display:none');
        }else if(fl.indexOf("东山公交") != -1){
            $(".jt").attr('style','display:none');
        }
        $('#back').data('tabId',tabId);
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
				leftUlStr += "<li class = 'xwbt' data-id = '"+cons[i].id+"'><span>" + cons[i].tittle + "</span></li>";
				rightUlStr += "<li><span>" + cons[i].userName + "&nbsp;"+ cons[i].sqsj + "</span></li>";
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
			var rightUlStr = "<ul>";
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

function getAndDisplayHytzByFl(url, fl) {
    AjaxTool.get(url, {
        lx : fl
    },function (data) {
        if (data.success){
            var leftUlStr = "<ul class='home-content-block'>";
            var cons = data.content;
            for (var i = 0; (i < cons.length && i < 7); i++){
                leftUlStr += "<li><span class = 'hytz'  data-id='"+cons[i].id+"' data-hytzid = '"+cons[i].hytzid+"' data-fl = '"+fl+"' data-lb = '"+cons[i].hydd+"'>" + cons[i].hyzt +"</span><span style='float: right'>" + cons[i].hyrq +" "+ cons[i].kssj +"</span></li>";
            }
            leftUlStr += "</ul>";
            $('#hyTab').html(leftUlStr);
        }
    })
}

function getAndDisplayFwsjByFl(url,fl,bzStr){
	AjaxTool.get(url, {
		lx: fl,
		bz: bzStr
	}, function(data) {
		if (data.success) {
			var leftUlStr = "<ul class='home-content-block'>";
			var rightUlStr = "<ul>";
			var fw = data.content;
			for (var i = 0; (i < fw.length && i < 7); i++) {
				leftUlStr += "<li class = 'fwbt' data-id = '"+fw[i].id+"' data-fl = '"+fl+"'><span>" + fw[i].bt + "</span></li>";
				rightUlStr += "<li><span>" + fw[i].userName + "&nbsp;"+ fw[i].date + "</span></li>" ;
			}
			leftUlStr += "</ul>";
			rightUlStr += "</ul>";
			$('#gwLeft').html(leftUlStr);
			$('#gwRight').html(rightUlStr);
		}
	})
}

function getAndDisplayTzByfl(url,fl){
	AjaxTool.get(url, {
			lx:fl
		}, function(data) {
			if (data.success){
				var leftUlStr = "<ul class='home-content-block2'>";
				var rightUlStr = "<ul>";
				var fwtz = data.content;
				for (var i = 0; (i < fwtz.length && i < 7); i++) {
					leftUlStr += "<li class = 'fwtzbt' data-id = '"+fwtz[i].id+"' data-fl = '"+fl+"' data-fwtzid = '"+fwtz[i].fwtzid+"'><span>" + fwtz[i].bt + "</span></li>";
					if (fl == 'wd'){
						rightUlStr += "<li><span>" + fwtz[i].userName + "&nbsp;"+ fwtz[i].tzsj + "</span></li>";
					}
					else if (fl == 'yd'){
						rightUlStr += "<li><span>" + fwtz[i].userName + "&nbsp;"+ fwtz[i].ydsj + "</span></li>";
					}


				}
				leftUlStr += "</ul>";
				rightUlStr += "</ul>";
				console.log(rightUlStr);
				$('#gwLeft').html(leftUlStr);
				$('#gwRight').html(rightUlStr);
			}

		}
	)
}

//显示公文中心的传阅和办理
function getAndDisplaySwCyByfl(url,fl){
	var ym=fl;
	var roleName = $('#userRolesName').val();
	//如果是领导，显示未传阅、未办理和未审批
	//如果是机要专员.显示未传阅、未办理和待处理
	if(roleName.indexOf("收文负责人") !=-1){
		ym+="dsz";
	}
	if(roleName.indexOf("收文机要专员") !=-1){
		ym+="jyzy";
	}
	AjaxTool.post(url, {
			tab :fl, ym: ym
		}, function(data) {
			if (data.success){
				var leftUlStr = "<ul class='home-content-block2'>";
				var rightUlStr = "<ul>"
				var drafts = data.content;
				for (var i = 0; (i < drafts.length && i <= 7); i++) {
					leftUlStr += "<li class = 'lcbt' data-id = '"+drafts[i].id+"' data-fl = '"+drafts[i].tab+"'><span>" + drafts[i].wjbt + "</span></li>";
					rightUlStr += "<li><span>" + drafts[i].swr + "&nbsp;"+ drafts[i].fqsj + "</span></li>";


				}
				leftUlStr += "</ul>";
				rightUlStr += "</ul>";
				$('#gwLeft').html(leftUlStr);
				$('#gwRight').html(rightUlStr);
			}

		}
	)
}

//收文传阅
function seeCy(id,tabId) {
	AjaxTool.html('draftSw/swCy',{draftSwId: id},function (html) {
		$('#portlet-box').addClass('portlet box');
		$('.portlet-body').html(html);
		$('#back').data('tabId',tabId);
		switch (tabId) {
			case "ycy":
				$('.operation').attr('style','display:none');
				break;
			default:
				break;
		}
		$('#back').data('tabId',tabId);
	});

}
//收文办理
function seeBl(id,tabId){
	AjaxTool.html('draftSw/swBl',{draftSwId: id},function (html) {
		$('#portlet-box').addClass('portlet box');
		$('.portlet-body').html(html);
		$('#back').data('tabId',tabId);
		switch (tabId) {
			case "ybl":
				$('.operation').attr('style','display:none');
				break;
			default:
				break;
		}
		$('#back').data('tabId',tabId);
	});
}
//收文处理
function seeCl(id,tabId) {
	if(tabId == "ycl"){
	    AjaxTool.html('draftSw/swjlxqNE',{id: id},function (html) {
	        $('.portlet-body').html(html);
	        $('#back').data('tabId',tabId);
	    })
	}else{
		AjaxTool.html('draftSw/swjlxq',{id: id},function (html) {
			$('.portlet-body').html(html);
	        $('#back').data('tabId',tabId);
			
		});
	}
}
//收文批示
function seePs(id,tabId) {
	AjaxTool.html('draftSw/swPs',{draftSwId: id},function (html) {
        $('.portlet-body').html(html);
        switch (tabId) {
            case "yps":
                $('.operation').attr('style','display:none');
                break;
            default:
                break;
        }
        $('#back').data('tabId',tabId);
    });

}
// 日历插件 备忘录
jQuery(document).ready(function() {
	var roleName = $('#userRolesName').val();
	// if (roleName.indexOf("董事长") ==-1 && roleName.indexOf("机要专员") ==-1){
    if (roleName.indexOf("发文审核员") ==-1 && roleName.indexOf("发文机要专员") ==-1){
		$('#fwdb').text("发文未读");
		$('#fwyb').text("发文已读");
	}

	$('#dtgg').trigger('click');
	$('#htdb').trigger('click');
	// $('#fwdb').trigger('click');
	$('#dcy').trigger('click');
    $('#wdtz').trigger('click');

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
