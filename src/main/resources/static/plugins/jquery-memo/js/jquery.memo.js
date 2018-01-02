;(function () {
    var Memo = function (ele, options) {
        this.$element = ele;
        var $element;
        var defaults = {
            requestQuery:{
            	url:"",
            	param:{}
            },
            requestAdd:{
            	url:"",
            	param:{}
            },
            requestUpdate:{
            	url:"",
            	param:{}
            },
            requestDelete:{
            	url:"",
            	param:{}
            }
        };
        var options = $.extend({}, defaults, options);
        var data = {};
        var currentData = {
            selected:{}
        };
        var requestFunc;

        var memoWrapperHtml = '<div class="memo-wrapper"><div class="memo">${content}</div></div>';
        var memoCalendarHtml = '<div class="memo-calendar"><div class="memo-day-wrapper"><div class="memo-day">${day}</div><div class="memo-week">${week}</div></div><div class="memo-year-wrapper">' +
            '<button type="button" class="memo-left-arrow"><span><svg viewBox="0 0 24 24"><path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z"></path></svg></span></button>' +
            '<span class="memo-year" data-year="${dataYear}" data-month="${dataMonth}">${year}</span><button type="button" class="memo-right-arrow"><span><svg viewBox="0 0 24 24"><path d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z"></path></svg></span></button></div>' +
            '<div class="memo-week-all"><span>一</span><span>二</span><span>三</span><span>四</span><span>五</span><span>六</span><span>日</span></div>' +
            '<div class="memo-month-wrapper"><div class="memo-month">${dayOfMonth}</div></div></div>';
        var dayRowHtml = '<div class="day-row">${col}</div>';
        var dayColHtml = '<button type="button" class="day-col" data-week=""><div></div><span>${day}</span></button>';
        var dayColShowHtml = '<span class="day-col"></span>';
        var memoContentHtml = '<div class="memo-content"><div class="memo-list-wrapper"><div class="memo-list"></div></div><div class="memo-form-wrapper">' + '<div class="memo-form"><div class="form-input"><input type="text" class="input-time"><input type="text" class="input-desc"></div>' +
            '<div class="form-button"><span class="button-cancel-wrapper"><input type="button" class="button-cancel" value="取消"/></span><span class="button-confirm-wrapper"><input type="button" class="button-confirm" value="确定"/></span></div></div>' +
            '<div class="memo-add"><span></span></div></div></div>';
        var memoContentItemHtml = '<div class="memo-item small-font smallsize-font" data-id="${id}"><div class="memo-time">${time}</div><div class="memo-desc">${desc}</div><div class="memo-actions"><span class="memo-update"></span><span class="memo-delete"></span></div></div>';

        this.init = function () {
            $element= this.$element;
            //设置当前日期
            var currentDate = new Date();
            var year = currentDate.getFullYear();
            var month = currentDate.getMonth() + 1;
            var day = currentDate.getDate();
            var week = getWeek(currentDate.getDay());

            memoCalendarHtml = memoCalendarHtml.replace("${day}", day);
            memoCalendarHtml = memoCalendarHtml.replace("${week}", week);
            memoCalendarHtml = memoCalendarHtml.replace("${year}", year + "年" + month + "月");
            memoCalendarHtml = memoCalendarHtml.replace("${dataYear}", year);
            memoCalendarHtml = memoCalendarHtml.replace("${dataMonth}", month);

            var rowWrapper=addCalendar(year,month);
            memoCalendarHtml = memoCalendarHtml.replace("${dayOfMonth}", rowWrapper);
            //把当前日期，显示的天数和底部form加入到MemoContent中
            var content = memoCalendarHtml + memoContentHtml;
            memoWrapperHtml = memoWrapperHtml.replace("${content}", content);
            $element.append(memoWrapperHtml);
            //设置当前日期
            $element.find("button.day-col").eq(day - 1).find("div").addClass("today-active");
            currentData.selected.year=year;
            currentData.selected.month=month;
            currentData.selected.day=day;
            $element.find(".memo-form").hide();
            
            setDayColClick();
            //设置点击下一月的监听
            $(".memo-left-arrow").click(function () {
                setMemoMonth(1);
            });
            $(".memo-right-arrow").click(function () {
                setMemoMonth(2);
            });
            setMemoFormClick();
            //获取当月的备忘录
            request(year,month);
        }
        
        var computeMemoDescWidth = function(){
        	$element.find(".memo-item").each(function(){
        		var itemWidth=$(this).outerWidth();
                $(this).find(".memo-desc").css("width","100%");
                if(itemWidth<=400){
                    $(this).find(".memo-time").css("width","50%");
        		}else{
                    $(this).find(".memo-time").css("width","35%");
                    // var timeWidth=$(this).find(".memo-time").outerWidth();
            		// var actionsWidth=$(this).find(".memo-actions").outerWidth();
            		// $(this).find(".memo-desc").width(itemWidth-timeWidth-actionsWidth-50);
            		// $(this).css("height","100%");
        		}
                $(this).css("height","100%");
        	});
        }
        
        var addCalendar=function(year,month){
            //设置显示的天数
            var rowWrapper = "";
            var dayCount = getDayCountByMonth(year, month);
            var firstDateWeek = getFirstDateByMonth(year, month).getDay();
            var rolCount = 5;
            if (firstDateWeek == 0) {
                firstDateWeek = 7;
            }
            if (dayCount == 31 && firstDateWeek > 5) {
                rolCount = 6;
            }
            if (dayCount == 30 && firstDateWeek > 6) {
                rolCount = 6;
            }
            if (dayCount == 28 && firstDateWeek == 1) {
                rolCount = 4;
            }
            var oCount = (dayCount - (7 - firstDateWeek + 1)) % 7;
            var dayOfMonth = 1;
            for (var i = 0; i < rolCount; i++) {
                var row = dayRowHtml;
                var colWrapper = "";
                for (var j = 1; j < 8; j++) {
                    if (i == 0) {
                        if (j < firstDateWeek) {
                            colWrapper += dayColShowHtml;
                            continue;
                        }
                    }
                    if (i == rolCount - 1) {
                        if (j > oCount && oCount != 0) {
                            colWrapper += dayColShowHtml;
                            continue;
                        }
                    }
                    colWrapper += dayColHtml.replace("${day}", dayOfMonth);
                    dayOfMonth++;
                }
                row = row.replace("${col}", colWrapper);
                rowWrapper += row;
            }
            return rowWrapper;
        }

        var setMemoMonth = function (direction) {
        	//删除memo-list
        	$element.find(".memo-list").html("");
            var year = $element.find(".memo-year").data("year");
            var month = $element.find(".memo-year").data("month");
            //左
            if (direction == 1) {
                if (month == 1) {
                    year--;
                    month=12;
                }else{
                    month--;
                }
            } else if (direction == 2) {
                //右
                if(month==12){
                    year++;
                    month=1;
                }else{
                    month++;
                }
            }
            //重置日历
            $element.find(".memo-year").data("year",year);
            $element.find(".memo-year").data("month",month);
            $element.find(".memo-year").text(year+"年"+month+"月");
            var memoCalendarHtml=addCalendar(year,month);
            $element.find(".memo-month").html("");
            $element.find(".memo-month").html(memoCalendarHtml);
          //设置当天日期
            var todayDate = new Date();
            var todayYear = todayDate.getFullYear();
            var todayMonth = todayDate.getMonth() + 1;
            var todayDay = todayDate.getDate();
            if(year==todayYear&&month==todayMonth){
            	$element.find("button.day-col").eq(todayDay - 1).find("div").addClass("today-active");
            }
            //设置当前选中日期
            if(year==currentData.selected.year&&month==currentData.selected.month){
                var selectedDay=currentData.selected.day;
                $element.find("button.day-col").eq(selectedDay - 1).find("div").addClass("current-active");
            }

            request(year,month);
            setDayColClick();
        }

        var request = function (year,month) {
            var url = options.requestQuery.url;
            if (url) {
            	var param=options.requestQuery.param;
            	param.year=year;
            	param.month=month;
                var promise = $.ajax({
                    url: url,
                    data: param,
                    type: "post",
                    dataType: "json"
                });
                promise.then(function (response) {
                    if (response.result) {
                    	var days=getDayCountByMonth(year,month);
                        for(var i=1;i<=days;i++){
                        	if(response.memoItemMap[i]){
                                if(year==currentData.selected.year&&currentData.selected.month==month){
                                	if(currentData.selected.day!=i){
                                		$element.find("button.day-col").eq(i-1).find("div").addClass("active");
                                	}
                                }else{
                                	$element.find("button.day-col").eq(i-1).find("div").addClass("active");
                                }
                        		data[year+"-"+month]=response.memoItemMap;
                        	}
                        }
                        //点击当前选中的日期
                        if(year==currentData.selected.year&&currentData.selected.month==month){
                        	$element.find("button.day-col").eq(currentData.selected.day-1).click();
                        }
                    }else{
//                    	Toast.show("备忘录提醒",response.message);
                    }
                    if (requestFunc) {
                        requestFunc(response);
                    }
                });
            }
        }

        //设置点击天数的监听
        var setDayColClick = function () {
            $element.find(".day-col").click(function(){
                var day = $(this).find("span").text();
                var year = $element.find(".memo-year").data("year");
                var month = $element.find(".memo-year").data("month");
                var week = getWeekByDate(year,month,day);
                week = getWeek(week);
                var memoItemMap = data[year + "-" + month];
                var days=getDayCountByMonth(year,month);
                //设置选中日期
                for(var i=1;i<=days;i++){
                	if(memoItemMap&&memoItemMap[i]){
                		var $activeDiv=$element.find("button.day-col").eq(i-1).find("div");
                		if(!$activeDiv.hasClass("today-active")){
                			$activeDiv.addClass("active");
                		}
                	}
                }
                $element.find("button.day-col div.current-active").removeClass("current-active");
                $element.find("button.day-col").eq(day - 1).find("div").removeClass("active");
                $element.find("button.day-col").eq(day - 1).find("div").addClass("current-active");
                $element.find(".memo-day").text(day);
                $element.find(".memo-week").text(week);
                currentData.selected.year=year;
                currentData.selected.month=month;
                currentData.selected.day=day;
                
                if(memoItemMap){
                	 var memoItems=memoItemMap[day];
                     $element.find(".memo-list").html("");
                     if (memoItems) {
                         for (var i = 0; i < memoItems.length; i++) {
                        	 addMemoItem(memoItems[i]);
                         }
                     }
                }
                computeMemoDescWidth();
                //设置更新/删除按钮
                setUpdateClick();
                setDeleteClick();
            });
        }
        
        //更新
        var setUpdateClick = function(){
        	var sweetAlertContentHtml = '<form id="memo-form"><div class="form-group" style="display:block;"><input type="text" class="form-control date-picker" name="time" placeholder="请输入时间"/><input type="text" class="form-control" name="desc" placeholder="请输入内容" style="margin-top:20px"/></div></form>';
        	$element.find(".memo-update").unbind("click");
        	$element.find(".memo-update").bind("click",function(){

                var year=currentData.selected.year;
                var month=currentData.selected.month;
                var day=currentData.selected.day;
        		var $this=$(this);
        		var $memoItem=$this.parent().parent();
        		swal({   
        			title: "备忘录修改",   
                    type: "content",   
                    content: sweetAlertContentHtml,
                    showCancelButton: true,   
                    closeOnConfirm: false,   
                    animation: "slide-from-top",   
                    confirmButtonText: "确定",
        			cancelButtonText: "取消",
        			openModal:function(){
        				var time=$memoItem.find(".memo-time").html();
        				var desc=$memoItem.find(".memo-desc").html();
        				$("#memo-form input[name='time']").val(time);
        				$("#memo-form input[name='desc']").val(desc);
        			}},function(isConfirm){  
                    	if (isConfirm){
                    		var url = options.requestUpdate.url;
                    		var param=options.requestUpdate.param;
                    		var id=$memoItem.data("id");
                    		var time=$("#memo-form input[name='time']").val();
            				var desc=$("#memo-form input[name='desc']").val();
            				param.id=id;
            				param.time=time;
            				param.desc=desc;
                    		if(url){
                    			var promise = $.ajax({
                                    url: url,
                                    data: param,
                                    type: "post",
                                    dataType: "json"
                                });
                        		promise.then(function(response){
                        			if(response.result){
                        				swal("备忘录修改提醒", response.message, "success");
                        				$memoItem.find(".memo-time").html(time);
                        				$memoItem.find(".memo-desc").html(desc);
                        				var memoItems=getSelectedData();
                        				for(var i=0;i<memoItems.length;i++){
                        					if(memoItems[i].id=id){
                        						memoItems[i].time=time;
                        						memoItems[i].desc=desc;
                        					}
                        				}
                        			}else{
                        				swal("备忘录修改提醒", response.message, "error");
                        			}
                        		});
                    		}
    			        }
               });
                var str = year+","+month+","+day;
                var date1 = new Date(str);
                var date2 = new Date(date1.getTime()+24*60*60*1000-1);
                $('.date-picker').datetimepicker({
                    startDate:date1,
                    endDate:date2,
                    format:'yyyy/mm/dd hh:ii',
                    language: 'zh-CN',
                    autoclose: 1,
                    startView: 1,
                    maxView:1
                });
        	});
        }
        
        //删除
        var setDeleteClick = function(){
        	$element.find(".memo-delete").unbind("click");
        	$element.find(".memo-delete").bind("click",function(){
        		var $this=$(this);
        		var $memoItem=$this.parent().parent();
        		swal({   
        			title: "备忘录删除提醒",   
        			text: "是否删除该备忘录？",
                    type: "warning",   
                    showCancelButton: true,   
                    closeOnConfirm: false,   
                    animation: "slide-from-top",   
                    confirmButtonText: "确定",
        			cancelButtonText: "取消"
        			},function(isConfirm){  
                    	if (isConfirm){
                    		var url = options.requestDelete.url;
                    		var param=options.requestDelete.param;
                    		var id=$memoItem.data("id");
            				param.id=id;
                    		if(url){
                    			var promise = $.ajax({
                                    url: url,
                                    data: param,
                                    type: "post",
                                    dataType: "json"
                                });
                        		promise.then(function(response){
                        			if(response.result){
                        				swal("备忘录删除提醒", response.message, "success");
                        				$memoItem.remove();
                        				var memoItems=getSelectedData();
                        				for(var i=0;i<memoItems.length;i++){
                        					if(memoItems[i].id=id){
                        						delete memoItems[i];
                        					}
                        				}
                        			}else{
                        				swal("备忘录删除提醒", response.message, "error");
                        			}
                        		});
                    		}
    			        }
               });
        	});
        }
        
        var setMemoFormClick=function(){
        	var sweetAlertContentHtml = '<form id="memo-form"><div class="form-group" style="display:block;"><input type="text" class="form-control date-picker" name="time" placeholder="请输入时间"/><input type="text" class="form-control" name="desc" placeholder="请输入内容" style="margin-top:20px"/></div></form>';
        	$element.find(".memo-add").click(function(){
                var year=currentData.selected.year;
                var month=currentData.selected.month;
                var day=currentData.selected.day;
        		swal({   
        			title: "备忘录增加",   
                    type: "content",   
                    content: sweetAlertContentHtml,
                    showCancelButton: true,   
                    closeOnConfirm: false,   
                    animation: "slide-from-top",   
                    confirmButtonText: "确定",
        			cancelButtonText: "取消",
        			},function(isConfirm){  
                    	if (isConfirm){
                    		var time=$("#memo-form input[name='time']").val();
            				var desc=$("#memo-form input[name='desc']").val();
                    		requestAdd(time,desc);
    			        }
               });
                var str = year+","+month+","+day;
                var date1 = new Date(str);
                var date2 = new Date(date1.getTime()+24*60*60*1000-1);
                $('.date-picker').datetimepicker({
                    startDate:date1,
                    endDate:date2,
                    format:'yyyy/mm/dd hh:ii',
                    language: 'zh-CN',
                    // weekStart: 1,
                    // todayBtn:  1,
                    autoclose: 1,
                    // todayHighlight: 1,
                    startView: 1,
                    maxView:1
                    // minView: 2,
                    // forceParse: 0
                });
        	});
//            $element.find(".memo-add").click(function(){
//                $(this).hide();
//                $element.find(".memo-form").show();
//            });
//            $element.find(".button-cancel").click(function(){
//                $element.find(".memo-form").hide();
//                $element.find(".memo-add").show();
//            });
//            $element.find(".button-confirm").click(function(){
//                requestAdd();
//            });
        }

        var requestAdd = function(time,desc){
            var url = options.requestAdd.url;
            if (url) {
            	var year=currentData.selected.year;
                var month=currentData.selected.month;
                var day=currentData.selected.day;
            	var param=options.requestAdd.param;
            	param.year=year;
            	param.month=month;
            	param.day=day;
            	param.time=time;
            	param.desc=desc;
                var promise = $.ajax({
                    url: url,
                    data: param,
                    type: "post",
                    dataType: "json"
                });
                promise.then(function (response) {
                    if (response.result) {
                    	swal("备忘录增加提醒", response.message, "success");
                    	request(year,month);
                    }else{
                    	swal("备忘录增加提醒", response.message, "error");
                    }
                });
            }
        }
        
        function getSelectedData(){
        	var year=currentData.selected.year;
			var month=currentData.selected.month;
			var day=currentData.selected.day;
			var memoItemMap = data[year + "-" + month];
			var memoItems=memoItemMap[day];
			return memoItems;
        }
        
        var addMemoItem = function(memoItem){
            var itemHtml = memoContentItemHtml;
            itemHtml = itemHtml.replace("${id}", memoItem.id);
            itemHtml = itemHtml.replace("${time}", memoItem.time);
            itemHtml = itemHtml.replace("${desc}", memoItem.desc);
            $element.find(".memo-list").append(itemHtml);
        }

        var getWeek = function (day) {
            var week;
            switch (day) {
                case 1:
                    week = "星期一";
                    break;
                case 2:
                    week = "星期二";
                    break;
                case 3:
                    week = "星期三";
                    break;
                case 4:
                    week = "星期四";
                    break;
                case 5:
                    week = "星期五";
                    break;
                case 6:
                    week = "星期六";
                    break;
                default:
                    week = "星期天";
            }
            return week;
        }

        this.requestCallback = function (requestFunction) {
            requestFunc = requestFunction;
        }
    };

    var instances = {};
    $.fn.Memo = function (options) {
        var selector = $(this).selector;
        var memo = instances[selector];
        if (memo) {
            return memo;
        }
        if(!options.requestUpdate.param){
        	options.requestUpdate.param={};
        }
        if(!options.requestAdd.param){
        	options.requestAdd.param={};
        }
        if(!options.requestDelete.param){
        	options.requestDelete.param={};
        }
        if(!options.requestQuery.param){
        	options.requestQuery.param={};
        }
        memo = new Memo(this, options);
        instances[selector] = memo;
        memo.init();
        return memo;
    };

    $.fn.MemoDestory = function () {
        var selector = $(this).selector;
        delete instances[selector];
    }

})();
/**
 * 获取当前月的第一天
 */
function getFirstDateCurrentMonth() {
    var date = new Date();
    date.setDate(1);
    return date;
}

/**
 * 获取当前月的最后一天
 */
function getLastDateCurrentMonth() {
    var date = new Date();
    var currentMonth = date.getMonth();
    var nextMonth = ++currentMonth;
    var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
    var oneDay = 1000 * 60 * 60 * 24;
    return new Date(nextMonthFirstDay - oneDay);
}

/**
 * 获取当前月的天数
 */
function getCurrentDayCount() {
    var firstDate = getFirstDateCurrentMonth();
    var lastDate = getLastDateCurrentMonth();
    var dayCount = lastDate.getDate() - firstDate.getDate() + 1;
    return dayCount;
}

/**
 * 获取某月的天数
 */
function getDayCountByMonth(year, month) {
    var firstDate = getFirstDateByMonth(year, month);
    var lastDate = getLastDateByMonth(year, month);
    var dayCount = lastDate.getDate() - firstDate.getDate() + 1;
    return dayCount;
}

/**
 * 得到某月第一天
 */
function getFirstDateByMonth(year, month) {
    var date = new Date();
    date.setYear(year);
    date.setMonth(month - 1);
    date.setDate(1);
    return date;
}

/**
 * 得到某月最后一天
 */
function getLastDateByMonth(year, month) {
    var date = new Date();
    date.setYear(year);
    date.setMonth(month - 1);
    var currentMonth = date.getMonth();
    var nextMonth = ++currentMonth;
    var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
    var oneDay = 1000 * 60 * 60 * 24;
    return new Date(nextMonthFirstDay - oneDay);
}

/**
 * 得到某天是星期几
 */
function getWeekByDate(year, month, day) {
	var date = new Date();
    date.setYear(year);
    date.setMonth(month - 1);
    date.setDate(day);
    return date.getDay();
}