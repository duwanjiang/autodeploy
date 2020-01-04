layui.use(['table', 'form', 'laydate', 'element', 'tree', 'layer', 'upload'], function () {
    var table = layui.table;
    var element = layui.element;
    var form = layui.form;
    var layer = layui.layer;
    var upload = layui.upload;
    var $ = layui.$;
    //容器
    var container = {
        url: {
            typeComboBox: '../../job/queryAllSystems',
            query: '../../job/listJobs',
            upload: '../../job/uploadWar',
            deployJobs: '../../job/deployJobs',
            deployJob: '../../job/deployJob',
            initJobs: '../../job/initJobs',
            deleteJobs: '../../job/deleteJobs',
        },
        initPage: function () {
            var index = layer.load(0, {shade: false});
            typeComboBox();
            layer.close(index);
        },
        //上传文件
        bindEvent: function () {
            var uploadObj = {
                elem: '#choose_btn',
                url: container.url.upload,
                auto: false,
                accept: 'file',
                exts: 'war',
                bindAction: '#upload_btn',
                headers: {},
                size: 0,
                choose: function (obj) {
                    obj.preview(function (index, file, result) {
                        var fileName = file.name;
                        $('#file_text').val(fileName);
                    });
                },
                before: function (obj) {
                    this.headers.appCode = encodeURIComponent($('#search_type').val());
                    if (this.headers.appCode == '') {
                        layer.msg("请先选择系统");
                        return;
                    }
                    layer.load();
                },
                done: function (res, index, upload) {
                    if (res.msg) {
                        layer.msg(res.msg);
                    }
                    layer.closeAll('loading');
                },
                error: function (index, upload) {
                    layer.msg('上传失败');
                    layer.closeAll('loading');
                }
            };
            upload.render(uploadObj);
        },
        listJobs: function (appCode) {
            if (appCode == undefined || appCode == '') {
                layer.msg('请选择系统')
                return;
            }
            var index = layer.load(0, {shade: false});
            $.ajax({
                url: container.url.query,
                type: 'GET',
                data: {appCode: appCode},
                contentType: 'application/x-www-form-urlencoded',
                dataType: 'json',
                success: function (arr) {
                    //加载系统
                    container.load(arr);
                    layer.close(index);
                }, error: function () {
                    layer.msg('查询失败');
                    layer.closeAll('loading');
                }
            });
        },
        deployJobs: function (appCode) {
            if (appCode == undefined || appCode == '') {
                layer.msg('请选择系统')
                return;
            }
            var index = layer.load(0, {shade: false});
            $.ajax({
                url: container.url.deployJobs,
                type: 'GET',
                data: {appCode: appCode},
                contentType: 'application/x-www-form-urlencoded',
                dataType: 'json',
                success: function (arr) {
                    arr && layer.msg(arr.msg)
                    if (arr && arr.code == 200) {
                        container.listJobs(container.appCode)
                    }
                    layer.close(index);
                }, error: function () {
                    layer.msg('部署失败');
                    layer.closeAll('loading');
                }
            });
        },
        deployJob: function (jobName) {
            if (jobName == undefined || jobName == '') {
                layer.msg('任务名不能为空')
                return;
            }
            var index = layer.load(0, {shade: false});
            $.ajax({
                url: container.url.deployJob,
                type: 'GET',
                data: {jobName: jobName},
                contentType: 'application/x-www-form-urlencoded',
                dataType: 'json',
                success: function (arr) {
                    arr && layer.msg(arr.msg)
                    if (arr && arr.code == 200) {
                        container.listJobs(container.appCode)
                    }
                    layer.close(index);
                }, error: function () {
                    layer.msg('部署失败');
                    layer.closeAll('loading');
                }
            });
        },
        deleteJob: function (appCode) {
            if (appCode == undefined || appCode == '') {
                layer.msg('开始清空所有任务')
            } else {
                layer.msg('开始清空[' + appCode + ']系统的任务')
            }
            var index = layer.load(0, {shade: false});
            $.ajax({
                url: container.url.deleteJobs,
                type: 'GET',
                data: {appCode: appCode},
                contentType: 'application/x-www-form-urlencoded',
                dataType: 'json',
                success: function (arr) {
                    arr && layer.msg(arr.msg)
                    if (arr && arr.code == 200) {
                        container.listJobs(container.appCode)
                    }
                    layer.close(index);
                }, error: function () {
                    layer.msg('删除失败');
                    layer.closeAll('loading');
                }
            });
        },
        initJob: function (appCode) {
            if (appCode == undefined || appCode == '') {
                layer.msg('请选择系统')
                return;
            }
            var index = layer.load(0, {shade: false});
            $.ajax({
                url: container.url.initJobs,
                type: 'GET',
                data: {appCode: appCode},
                contentType: 'application/x-www-form-urlencoded',
                dataType: 'json',
                success: function (arr) {
                    arr && layer.msg(arr.msg)
                    if (arr && arr.code == 200) {
                        container.listJobs(container.appCode)
                    }
                    layer.close(index);
                }, error: function () {
                    layer.msg('初始化任务失败');
                    layer.closeAll('loading');
                }
            });
        },
        load: function (arr) {
            if (arr == null) {
                return;
            }
            var html = '';
            for (var i = 0; i < arr.length; i++) {
                var item = arr[i];
                var temp = '<div class="layui-col-md3">\n' +
                    '<div class="" style="height: 80px">\n' +
                    '<fieldset class="layui-elem-field layui-bg-gray">\n' +
                    '<legend>' + item.jobName + '</legend>\n' +
                    '<div class="layui-field-box">';
                temp += '状态:' + (item.jobStatus == 'FAILURE' ? '<red style=\"color:red;\">' + item.jobStatus + '</red>' : item.jobStatus)
                    + '<br class="layui-bg-blue"/>';
                temp += '版本:' + (item.versionInfoBean == null ? '未知' : item.versionInfoBean.version) + '<br class="layui-bg-blue"/>';
                temp += ' <button jobName="' + item.jobName + '" id="dp_btn">部署</button>';
                temp += '</div>\n' +
                    '</fieldset>\n' +
                    '</div>\n' +
                    '</div>';
                html += temp;
            }
            $('#job_count').html('任务数:' + arr.length + '个');
            $('#list-content').html(html);

        }
    };
    //ComboBox函数
    var typeComboBox = function () {
        var html = '<option value="">选择系统</option>';
        $.ajax({
            url: container.url.typeComboBox,
            type: 'GET',
            contentType: 'application/x-www-form-urlencoded',
            dataType: 'json',
            async: false,
            success: function (data) {
                for (var i = 0; i < data.length; i++) {
                    html += '<option value="' + data[i].systemCode + '">' + data[i].systemName + '</option>';
                }
                $('#search_type').html(html);
                form.render('select');
            },
            error: function (XMLHttpRequest) {
                layer.msg(XMLHttpRequest.responseText);
            }
        });

        //绑定选择事件
        form.on('select(type)', function (data) {
            if (data.value == '') {
                container.appCode = '';
                return;
            }
            container.appCode = data.value;
            //加载任务列表
            container.listJobs(container.appCode);
        });

        //绑定部署按钮事件
        $(document).on('click', '#deploy_btn', function () {
            container.deployJobs(container.appCode);
        });
        //绑定刷新按钮事件
        $(document).on('click', '#refresh_btn', function () {
            container.listJobs(container.appCode);
        });
        //绑定初始化按钮事件
        $(document).on('click', '#init_btn', function () {
            container.initJob(container.appCode);
        });
        //绑定部署事件
        $(document).on('click', '#dp_btn', function () {
            container.deployJob(this.getAttribute("jobName"));
        });
        //绑定删除事件
        $(document).on('click', '#delete_btn', function () {
            container.deleteJob(container.appCode);
        });
    };


    //页面初始化代码
    container.initPage();
    container.bindEvent();
});