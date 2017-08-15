;(function () {
    var DataTableEditor = function (ele, table, options) {
        var $element;
        this.$element = ele;
        var mTable = table;
        var defaults = {
            rowTypes: [],
            rowData: [],
            selectors: {"add": ".editor-add", "update": ".editor-update", "delete_": ".editor-delete"},
            buttons: {
                "update": '<a href="javascript:;" class="btn btn-xs blue editor-update">修改</a>',
                "delete_": '<a href="javascript:;" class="btn btn-xs red editor-delete">删除</a>'
            },
            server: false,
            urls: {"save": "", "delete_": ""},
            saveData: [],
            deleteData:[]
        };
        var options = $.extend({}, defaults, options);
        var recordData = {
            "columnCount": 0,
            "oriData": "",
            "isEdited": false,
            "editedRow": "-1",
            "currentOperation": "-1",
            "addCount": 0
        }
        var mSaveCallback;
        var mDeleteCallback;

        var inputTextHtmlStart = '<input type="text" class="table-editor-input-text" value="';
        var inputTextHtmlEnd = '">';
        var inputDateHtmlStart = '<input type="date" class="table-editor-input-text" value="';
        var inputDateHtmlEnd = '">';
        var selectHtmlStart = '<select class="table-editor-select">';
        var selectHtmlEnd = '</select>';
        var selectOptionStartHtml = '<option';
        var selectOptionStartEndHtml = ">";
        var selectOptionEndHtml = '</option>';
        var saveButtonHtml = '<a href="javascript:;" class="btn btn-xs blue saveButton">保存</a>';
        var cancelButtonHtml = '<a href="javascript:;" class="btn btn-xs red cancelButton">取消</a>';

        this.initEditor = function () {
            recordData.columnCount = this.$element.find("thead tr th").length;
            $element = this.$element;
            createEditRow(options.selectors.add);
            updateEditRow(options.selectors.update);
            deleteEditRow(options.selectors.delete_);
        }

        var createEditRow = function (selector) {
            $(selector).unbind("click");
            $(selector).bind("click", function () {
                cancelEdit();
                var data = convertdForm();
                var rowNode = mTable.row.add(data).draw().node();
                recordData.addCount++;
                recordData.isEdited = true;
                recordData.currentOperation = "add";
                var id = "add-tr-" + recordData.addCount;
                $(rowNode).attr("id", id);
                recordData.editedRow = id;
                setButtonListener();
            });
        };

        var updateEditRow = function (selector) {
            $element.find("tbody tr").each(function () {
                var id = $(this).attr("id");
                $(this).find(selector).bind("click", function () {
                    updateBindRow(id);
                });
            });
        }

        var updateSingleEditRow = function (selector, id) {
            $element.find("#" + id).find(selector).bind("click", function () {
                updateBindRow(id);
            });
        }

        var deleteEditRow = function (selector) {
            $element.find("tbody tr").each(function () {
                var id = $(this).attr("id");
                $(this).find(selector).bind("click", function () {
                    deleteBindRow(id);
                });
            });
        }

        var deleteSingleEditRow = function (selector, id) {
            $element.find("#" + id).find(selector).bind("click", function () {
                deleteBindRow(id);
            });
        }

        function updateBindRow(id) {
            cancelEdit();
            var row = mTable.row("#" + id);
            recordData.oriData = row.data();
            var data = convertdForm(recordData.oriData);
            row.data(data);
            setButtonListener();
            recordData.isEdited = true;
            recordData.editedRow = id;
            recordData.currentOperation = "update";
        }

        function deleteBindRow(id) {
            cancelEdit();
            swal({
                title: "确定删除吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "删除",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            }, function (isConfirm) {
                if (isConfirm) {
                    if (options.server) {
                        if (id.indexOf("add-tr-") > 0) {
                            var row = mTable.row("#" + id);
                            row.remove().draw();
                        } else {
                            var promise = deleteServer(id);
                            promise.then(function (response) {
                                if (mDeleteCallback) {
                                    var result = mDeleteCallback(response);
                                    if (result) {
                                        var row = mTable.row("#" + id);
                                        row.remove().draw();
                                    }
                                }
                            });
                        }
                    } else {
                        var row = mTable.row("#" + id);
                        row.remove().draw();
                    }
                }
            });
        }

        function convertdForm(mData) {
            var columnCount = recordData.columnCount;
            var data = new Array(columnCount);
            for (var i = 0; i < columnCount; i++) {
                if (i == columnCount - 1) {
                    data[i] = saveButtonHtml + cancelButtonHtml;
                } else {
                    var type = options.rowTypes[i];
                    if (type == 'text') {
                        if (mData === undefined) {
                            data[i] = inputTextHtmlStart + options.rowData[i] + inputTextHtmlEnd;
                        } else {
                            data[i] = inputTextHtmlStart + mData[i] + inputTextHtmlEnd;
                        }
                    } else if (type == 'select' || type == 'select-') {
                        var html = selectHtmlStart;
                        var optionData = options.rowData[i];
                        var newOptionData = new Array(0);
                        for (var j = 0; j < optionData.length; j++) {
                            newOptionData.push(optionData[j]);
                        }
                        if (type == 'select-') {
                            //如果type==select-，那么代表需要去重，把表中该select已经存在的去掉
                            $element.find("tbody tr").each(function () {
                                var text = $(this).find("td").eq(i).text();
                                for (var j = optionData.length - 1; j >= 0; j--) {
                                    var hasText = optionData[j];
                                    if (text == hasText) {
                                        if(mData!=undefined&&mData[i]==hasText){

                                        }else{
                                            newOptionData.remove(hasText);
                                        }
                                    }
                                }
                            });
                            optionData = newOptionData;
                        }
                        for (var j = 0; j < optionData.length; j++) {
                            if (mData == undefined) {
                                html += selectOptionStartHtml + selectOptionStartEndHtml + optionData[j] + selectOptionEndHtml;
                            } else {
                                var selectedData = mData[i];
                                if (selectedData == optionData[j]) {
                                    html += selectOptionStartHtml + " selected " + selectOptionStartEndHtml + optionData[j] + selectOptionEndHtml;
                                } else {
                                    html += selectOptionStartHtml + selectOptionStartEndHtml + optionData[j] + selectOptionEndHtml;
                                }
                            }
                        }
                        html += selectHtmlEnd;
                        data[i] = html;
                    } else if (type == 'date') {
                        if (mData === undefined) {
                            data[i] = inputDateHtmlStart + options.rowData[i] + inputDateHtmlEnd;
                        } else {
                            data[i] = inputDateHtmlStart + mData[i] + inputDateHtmlEnd;
                        }
                    }
                }
            }
            return data;
        }

        function setButtonListener() {
            $(".saveButton").bind("click", function () {
                saveEdit();
            });
            $(".cancelButton").bind("click", function () {
                cancelEdit();
            });
        }

        function saveEdit() {
            var isEdited = recordData.isEdited;
            var editedRow = recordData.editedRow;
            var columnCount = recordData.columnCount;
            if (isEdited) {
                var row = mTable.row("#" + editedRow);
                var rowNode = row.node();
                var id = $(rowNode).attr("id");
                var tds = $("#" + id).find("td");
                var data = new Array(columnCount);
                tds.each(function (index) {
                    var type = options.rowTypes[index];
                    if (type == 'text' || type == 'date') {
                        data[index] = $(this).find("input").val();
                    } else if (type == 'select' || type == 'select-') {
                        data[index] = $(this).find("select option:selected").text();
                    }
                });
                if (options.server) {
                    var promise = saveServer(id,data, recordData.currentOperation);
                    promise.then(function (response) {
                        if (mSaveCallback) {
                            var result = mSaveCallback(response);
                            if (result) {
                                data[columnCount - 1] = options.buttons.update + options.buttons.delete_;
                                row.data(data).draw();
                                if (recordData.currentOperation == 'add') {
                                    id = response.id;
                                }
                                $(rowNode).attr("id", id);
                                resetBindUpdateDelete(id);
                                resetRecord();
                            }
                        }
                    });
                } else {
                    data[columnCount - 1] = options.buttons.update + options.buttons.delete_;
                    row.data(data).draw();
                    resetBindUpdateDelete(editedRow);
                    resetRecord();
                    if (mSaveCallback) {
                        mSaveCallback(id);
                    }
                }
            }
        }

        function cancelEdit() {
            var isEdited = recordData.isEdited;
            var editedRow = recordData.editedRow;
            var currentOperation = recordData.currentOperation;
            var oriData = recordData.oriData;
            if (isEdited) {
                var row = mTable.row("#" + editedRow);
                if (currentOperation == 'update') {
                    row.data(oriData).draw();
                    resetBindUpdateDelete(editedRow);
                } else if (currentOperation == 'add') {
                    row.remove().draw();
                }
                resetRecord();
            }
        }

        function resetBindUpdateDelete(editedRow) {
            updateSingleEditRow(options.selectors.update, editedRow);
            deleteSingleEditRow(options.selectors.delete_, editedRow);
        }

        function resetRecord() {
            recordData.isEdited = false;
            recordData.editedRow = -1;
            recordData.currentOperation = "-1";
        }

        function saveServer(id,data, operation) {
            var newData = $.toJSON(data);
            var params = new Array(0);
            var saveData = options.saveData;
            for (var i = 0; i < saveData.length; i++) {
                var param = saveData[i];
                params.push(param);
            }
            params = $.toJSON(saveData);
            if(id.indexOf("add-tr")>=0){
            	id=null;
            }
            var mData = {"id":id,"data":newData, "operation": operation, "params": params};
            var promise = $.ajax({
                url: options.urls.save,
                data: mData,
                type: "post",
                dataType: "json"
            });
            return promise;
        }

        function deleteServer(id) {
        	var params=$.toJSON(options.deleteData);
            var promise = $.ajax({
                url: options.urls.delete_,
                data: {id: id,params:params},
                type: "post",
                dataType: "json"
            });
            return promise;
        }

        Array.prototype.indexOf = function (val) {
            for (var i = 0; i < this.length; i++) {
                if (this[i] == val) return i;
            }
            return -1;
        };

        Array.prototype.remove = function (val) {
            var index = this.indexOf(val);
            if (index > -1) {
                this.splice(index, 1);
            }
        };

        this.getAllData = function () {
            cancelEdit();
            var newData = new Array(0);
            $element.find("tbody tr").each(function () {
                var id = $(this).attr("id");
                var row = mTable.row("#" + id);
                var data = row.data();
                if (data === undefined) {
                    newData = null;
                    return;
                }
                data = data.splice(0, data.length - 1);
                newData.push(data);
            });
            if (newData == null) {
                return null;
            } else {
                return $.toJSON(newData);
            }
        }

        this.addRow = function (dataArray) {
            dataArray.push(options.buttons.update + options.buttons.delete_);
            var rowNode = mTable.row.add(dataArray).draw().node();
            recordData.addCount++;
            var id = "add-tr-" + recordData.addCount;
            $(rowNode).attr("id", id);
            resetBindUpdateDelete(id);
            resetRecord();
        }

        this.cancel = function () {
            cancelEdit();
        }

        this.saveCallback = function (saveCallback) {
            if (saveCallback) {
                mSaveCallback = saveCallback;
            }
        }

        this.deleteCallback = function (deleteCallback) {
            if (deleteCallback) {
                mDeleteCallback = deleteCallback;
            }
        }
    };

    $.fn.DataTableEditor = function (table, options) {
        var dataTableEditor = new DataTableEditor(this, table, options);
        dataTableEditor.initEditor();
        return dataTableEditor;
    };

})();

