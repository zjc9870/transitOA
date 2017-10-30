var $searchBar = $('#searchBar'),
$searchResult = $('#searchResult'),
$searchText = $('#searchText'),
$searchInput = $('#searchInput'),
$searchClear = $('#searchClear'),
$searchCancel = $('#searchCancel');

$(document).ready(function () {
	var dsp_count = 0;
	var ysp_count = 0;
	var wtj_count = 0;
	
	
	var dsp_cons = "";
	var ysp_cons = "";
	var wtj_cons = "";

	AjaxTool.get('sqjlTab', {
        lx: 'wtj', bz: 'sq'},function (data) {
        if(data.success) {
            var str = "";
            var wtj_cons = data.content;
            if(wtj_cons.length>wtj_count+15){
            	$('#wtj_loading').attr('style','display:block');
            }
            str+="<div class='weui-cell__hd'><div class='weui-cells__title'>未提交</div>";
            for(var i=0;i<wtj_cons.length;i++) {
            	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApplyRecordE(\""+ wtj_cons[i].id +'\",\"'+'wtj'+"\")' class='weui_cell_bd'>";
            	str+="<p>"+wtj_cons[i].htbt+"</p>";
            	str+="</div>";
            	str+="<div class='weui-cell__ft'>";
            	str+="</div>";
            	str+="</a>";
            	if(i>=wtj_count+15){
            		wtj_count=wtj_count+16;
            		break;
            	}
            }
            $('#weui_body_wtj').html(str);
        }
    });
	AjaxTool.get('sqjlTab', {
        lx: 'dsp', bz: 'sq'},function (data) {
        if(data.success) {
            var str = "";
            var dsp_cons = data.content;
            if(dsp_cons.length>dsp_count+15){
            	$('#dsp_loading').attr('style','display:block');
            }
            str+="<div class='weui-cell__hd'><div class='weui-cells__title'>待审批</div>";
            for(var i=0;i<dsp_cons.length;i++) {
            	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApplyRecordNE(\""+ dsp_cons[i].id +'\",\"'+'dsp'+"\")' class='weui_cell_bd'>";
            	str+="<p>"+dsp_cons[i].htbt+"</p>";
            	str+="</div>";
            	str+="<div class='weui-cell__ft'>";
            	str+="</div>";
            	str+="</a>";
            	if(i>=dsp_count+15){
            		dsp_count=dsp_count+16;
            		break;
            	}
            }
            $('#weui_body_dsp').html(str);
        }
    });
	AjaxTool.get('sqjlTab', {
        lx: 'ysp', bz: 'sq'},function (data) {
        if(data.success) {
            var str = "";
            var ysp_cons = data.content;
            if(ysp_cons.length>ysp_count+15){
            	$('#ysp_loading').attr('style','display:block');
            }
            str+="<div class='weui-cell__hd'><div class='weui-cells__title'>已审批</div>";
            for(var i=0;i<ysp_cons.length;i++) {
            	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApplyRecordNE(\""+ ysp_cons[i].id +'\",\"'+'ysp'+"\")' class='weui_cell_bd'>";
            	str+="<p>"+ysp_cons[i].htbt+"</p>";
            	str+="</div>";
            	str+="<div class='weui-cell__ft'>";
            	str+="</div>";
            	str+="</a>";
            	if(i>=ysp_count+15){
            		ysp_count=ysp_count+16;
            		break;
            	}
            }
            $('#weui_body_ysp').html(str);
        }
    });

	$('#weui_body_ysp').hide();
	$('#weui_body_dsp').hide();
	$('#wtj_tab').click(function() {
		$('#weui_body_ysp').hide();
		$('#weui_body_dsp').hide();
		$('#weui_body_wtj').show();
	});
	$('#dsp_tab').click(function() {
		$('#weui_body_ysp').hide();
		$('#weui_body_dsp').show();
		$('#weui_body_wtj').hide();
	});
	$('#ysp_tab').click(function() {
		$('#weui_body_ysp').show();
		$('#weui_body_dsp').hide();
		$('#weui_body_wtj').hide();
	});
	
	$('#wtj_loading').click(function() {
		  var str = "";
        if(wtj_cons.length<=wtj_count+15){
        	$('#wtj_loading').attr('style','display:none');
        }
		  for(var i=wtj_count;i<wtj_cons.length;i++) {
			str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
          str+="<div onclick='seeApplyRecordE(\""+ wtj_cons[i].id +'\",\"'+'wtj'+"\")' class='weui_cell_bd'>";
          str+="<p>"+wtj_cons[i].htbt+"</p>";
          str+="</div>";
          str+="<div class='weui_cell_ft'>";
          str+="</div>";
          str+="</a>";
	      	if(i+1==wtj_cons.length){
	      		$('#wtj_loading').attr('style','display:none');
	      	}
	      	if(i>=wtj_count+15){
	      		wtj_count=wtj_count+16;
	      		break;
	      	}
	      }
		  $("#weui_content_wtj").append(str);
	});
	
	$('#dsp_loading').click(function() {
		  var str = "";
	      if(dsp_cons.length<=dsp_count+15){
	      	$('#dsp_loading').attr('style','display:none');
	      }
		  for(var i=dsp_count;i<dsp_cons.length;i++) {
			  str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
          	str+="<div onclick='seeApplyRecordE(\""+ dsp_cons[i].id +'\",\"'+'wtj'+"\")' class='weui_cell_bd'>";
          	str+="<p>"+dsp_cons[i].htbt+"</p>";
          	str+="</div>";
          	str+="<div class='weui_cell_ft'>";
          	str+="</div>";
          	str+="</a>";
	      	if(i+1==dsp_cons.length){
	      		$('#dsp_loading').attr('style','display:none');
	      	}
	      	if(i>=dsp_count+15){
	      		dsp_count=dsp_count+16;
	      		break;
	      	}
	      }
		  $("#weui_content_dsp").append(str);
		  loading = false;
		});
		
	$('#ysp_loading').click(function() {
		  var str = "";
          if(ysp_cons.length<=ysp_count+15){
          	$('#ysp_loading').attr('style','display:none');
          }
		  for(var i=ysp_count;i<ysp_cons.length;i++) {
			str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
            str+="<div onclick='seeApplyRecordE(\""+ ysp_cons[i].id +'\",\"'+'wtj'+"\")' class='weui_cell_bd'>";
            str+="<p>"+ysp_cons[i].htbt+"</p>";
            str+="</div>";
            str+="<div class='weui_cell_ft'>";
            str+="</div>";
            str+="</a>";
	      	if(i+1==ysp_cons.length){
	      		$('#ysp_loading').attr('style','display:none');
	      	}
	      	if(i>=ysp_count+15){
	      		ysp_count=ysp_count+16;
	      		break;
	      	}
	      }
		  $("#weui_content_ysp").append(str);
	});
	
	
		//搜索
	
    $(function(){

        function hideSearchResult(){
            $searchResult.hide();
            $searchInput.val('');
        }
        function cancelSearch(){
            hideSearchResult();
            $searchBar.removeClass('weui-search-bar_focusing');
            $searchText.show();
        }

        $searchText.on('click', function(){
            $searchBar.addClass('weui-search-bar_focusing');
            $searchInput.focus();
        });
        $searchInput
            .on('blur', function () {
                if(!this.value.length) cancelSearch();
            })
            .on('input', function(){
                if(this.value.length) {
                	changeResult(this.value,dsp_cons,ysp_cons,wtj_cons);
                    $searchResult.show();
                } else {
                    $searchResult.hide();
                }
            })
        ;
        $searchClear.on('click', function(){
            hideSearchResult();
            $searchInput.focus();
        });
        $searchCancel.on('click', function(){
            cancelSearch();
            $searchInput.blur();
        });
    });
	
	
});


function changeResult(keyword,dsp_cons,ysp_cons,wtj_cons){
	str = "";
	str += searchCons(keyword,dsp_cons,"dsp");
	str += searchCons(keyword,ysp_cons,"ysp");
	str += searchCons(keyword,wtj_cons,"wtj");
	$searchResult.html("");
	$searchResult.html(str);
}

function searchCons(keyword,cons,lx){
	var str = "";
	for(var i=0;i<cons.length;i++) {
		if(contain(cons[i].bt,keyword)==1){
	      	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
	      	str+="<div onclick='seeApprove(\""+ cons[i].id +'\",\"'+lx+"\")' class='weui-cell__bd weui-cell_primary'>";
	      	str+="<p>"+cons[i].bt+"</p>";
	      	str+="</div>";
	      	str+="<div class='weui-cell__ft'>";
	      	str+="</div>";
	      	str+="</a>";
		}
      }
	return str;
}

function contain(str,substr){
	if(str){
		if(str.indexOf(substr) >= 0 ){
			   return 1;
			}else{
				return 0;
			}
	}
	
}



function seeApplyRecordE(id,tabId) {
    AjaxTool.html('sqjlxqE',{id: id},function (html) {
        document.write(html);
    });
}

function seeApplyRecordNE(id,tabId) {
    AjaxTool.html('sqjlxqNE',{id: id},function (html) {
        document.write(html);
    });

}



$('#dsp_loading').click(function() {
	  var str = "";
    if(dsp_cons.length<=dsp_count+15){
    	$('#dsp_loading').attr('style','display:none');
    }
	  for(var i=dsp_count;i<dsp_cons.length;i++) {
		str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
      	str+="<div onclick='seeApplyRecordE(\""+ cons[i].id +'\",\"'+'yth'+"\")' class='weui_cell_bd'>";
      	str+="<p>"+cons[i].htbt+"</p>";
      	str+="</div>";
      	str+="<div class='weui_cell_ft'>";
      	str+="</div>";
      	str+="</a>";
    	if(i+1==dsp_cons.length){
    		$('#dsp_loading').attr('style','display:none');
    	}
    	if(i>=dsp_count+15){
    		dsp_count=dsp_count+16;
    		break;
    	}
    }
	  $("#weui_content_dsp").append(str);
	  loading = false;
	});
	
	$('#ysp_loading').click(function() {
		  var str = "";
        if(ysp_cons.length<=ysp_count+15){
        	$('#ysp_loading').attr('style','display:none');
        }
		  for(var i=ysp_count;i<ysp_cons.length;i++) {
			str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
          	str+="<div onclick='seeApplyRecordE(\""+ cons[i].id +'\",\"'+'yth'+"\")' class='weui_cell_bd'>";
          	str+="<p>"+cons[i].htbt+"</p>";
          	str+="</div>";
          	str+="<div class='weui_cell_ft'>";
          	str+="</div>";
          	str+="</a>";
	      	if(i+1==ysp_cons.length){
	      		$('#ysp_loading').attr('style','display:none');
	      	}
	      	if(i>=ysp_count+15){
	      		ysp_count=ysp_count+16;
	      		break;
	      	}
	      }
		  $("#weui_content_ysp").append(str);
});