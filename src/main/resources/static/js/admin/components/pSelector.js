//人员选择框的实现
var pSelector = {
        init : function() {
            pSelector.initTreeView();
    },

    initSearch: function(){
        var form = $("<form id='s'></form>")
        var input = $("<input type='search' id='q'/>").appendTo(form);
        var button = $("<button type='submit'>搜索</button>").appendTo(form)
        $('#search-box').append(form);
        $("#s").submit(function(e) {
            e.preventDefault();
            $("#person-tree").jstree(true).search($("#q").val());
        });

    },

    initTreeView : function() {
        $.jstree.destroy();
        $('#person-tree').jstree({
            'plugins' : [ "wholerow", "checkbox", "types", "search" ],
            'core' : {
                "themes" : {
                    "responsive" : false
                },
                // 'data' : function(node, cb) {
                //     AjaxTool.post("/getPersonTree",
                //         null
                //     ,function(data){
                //         cb(data);
                //     });
                // }
                'data':[ {
                    "id" : "rer",
                    "text" : "公交部",
                    "state" : {
                        "selected" : false,
                        "opened" : false,
                        "disabled" : false
                    },
                    "icon" : null,
                    "sequence" : 0,
                    "children" : [ {
                        "id" : "fdaf",
                        "text" : "王伟",
                        "state" : {
                            "selected" : false,
                            "opened" : false,
                            "disabled" : false
                        },
                        "icon" : null,
                        "sequence" : 0,
                        "children" : [ ]
                    }, {
                        "id" : "err",
                        "text" : "系统部",
                        "state" : {
                            "selected" : false,
                            "opened" : false,
                            "disabled" : false
                        },
                        "icon" : null,
                        "sequence" : 0,
                        "children" : [ {
                            "id" : "erew",
                            "text" : "刘欢欢",
                            "state" : {
                                "selected" : false,
                                "opened" : false,
                                "disabled" : false
                            },
                            "icon" : null,
                            "sequence" : 0,
                            "children" : [ ]
                        } ]
                    }, {
                        "id" : "rere",
                        "text" : "业务部",
                        "state" : {
                            "selected" : false,
                            "opened" : false,
                            "disabled" : false
                        },
                        "icon" : null,
                        "sequence" :0,
                        "children" : [ ]
                    } ]
                } ]


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
        pSelector.initSearch();
    },
};

jQuery(document).ready(function() {
    pSelector.init();
});
