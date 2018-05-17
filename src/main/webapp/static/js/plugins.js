if ($.validator) {
    $.validator.setDefaults({
        errorElement: "span",
        errorPlacement: function (error, element) {
            error.addClass("help-block");
            var inputGroup = $(element).parent(".input-group");
            inputGroup.length > 0 ? inputGroup.after(error) : element.after(error);
        },
        highlight: function (element, errorClass, validClass) {
            $(element).closest(".form-group").addClass("has-error");
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).closest(".form-group").removeClass("has-error");
        }
    });
}
if ($.minicolors) {
    $.minicolors.defaults = $.extend($.minicolors.defaults, {
        theme: 'bootstrap'
    });
}
Bw = {};
Bw.getParameterValues = function (name) {
    var search = location.search;
    var values = [];
    if (search.length > 0) {
        search = search.substring(1);
        var paramArr = search.split("&");
        for (var i in params) {
            var pair = paramArr[i].split("=");
            var key = pair[0];
            var value = pair[1];
            if (key == name) {
                values[values.length] = value;
            }
        }
    }
    return values;
};
Bw.getParameter = function (key) {
    var search = location.search;
    var values = [];
    if (search.length > 0) {
        search = search.substring(1);
        var paramArr = search.split("&");
        for (var i in params) {
            var pair = paramArr[i].split("=");
            var key = pair[0];
            var value = pair[1];
            if (key == name) {
                return value;
            }
        }
    }
};
Bw.appendJsessionid = function (url, jsessionid) {
    if (!jsessionid) {
        jsessionid = $.cookie("JSESSIONID");
    }
    if (url && jsessionid) {
        var index = url.indexOf("?");
        if (index != -1) {
            url = url.substring(0, index) + ";jsessionid=" + jsessionid + url.substring(index);
        } else {
            url += ";jsessionid=" + jsessionid;
        }
    }
    return url;
};
Bw.imageDim = function (src, options) {
    if (src == undefined || src == "") {
        //$("#"+imgId).hide();
        return;
    }
    if (!(options && options.cache)) {
        if (src.indexOf("?") == -1) {
            src += "?";
        } else {
            src += "&";
        }
        src += "d=" + new Date() * 1;
    }
    var img = $("<img>", {
        "src": src
    }).css({
        "display": "none",
        "background-color": "#fff",
        "border": "1px solid #ccc",
        "padding": "2px",
        "position": "absolute",
        "z-index": 10000
    }).appendTo(document.body).offset({left: -99999, top: -99999}).show();
    img.load(function () {
        var dim = {"width": img.width(), "height": img.height()};
        if (options !== null && options !== undefined) {
            var maxWidth = options.maxWidth || Number.MAX_VALUE;
            var maxHeight = options.maxHeight || Number.MAX_VALUE;
            var to = options.to;
            var of = options.of;
            var scaleDim;
            if (dim.width > maxWidth || dim.height > maxHeight) {
                var widthScale = maxWidth / dim.width;
                var heightScale = maxHeight / dim.height;
                if (widthScale < heightScale) {
                    scaleDim = {"width": maxWidth, "height": dim.height * widthScale};
                } else {
                    scaleDim = {"width": dim.width * heightScale, "height": maxHeight};
                }
            }
            if (scaleDim != undefined) {
                img.css({"width": scaleDim.width, "height": scaleDim.heigth});
            }
            if (to !== null && to !== undefined) {
                to = $(to);
                if (to.is("img")) {
                    to.attr("src", src);
                    if (scaleDim != undefined) {
                        to.css({"width": scaleDim.width, "height": scaleDim.heigth});
                    }
                }
            }
            if (of !== null && of !== undefined) {
                img.positionSideOf(of);
            }
        }
    });
    return img;
};
(function ($) {
    var position = function (preferred, self, selfWidth, selfHeight, topHeight, bottomHeight, leftWidth, rightWidth, topByTop, topByBottom, leftByLeft, leftByRight) {
        var top, left;
        if (bottomHeight - selfHeight < 0 && topHeight > bottomHeight) {
            top = topByTop;
        } else if (topHeight - selfHeight < 0 && bottomHeight > topHeight) {
            top = topByBottom;
        } else if (preferred[0] != 'bottom') {
            top = topByTop;
        } else {
            top = topByBottom;
        }
        if (rightWidth - selfWidth < 0 && leftWidth > rightWidth) {
            left = leftByLeft;
        } else if (leftWidth - selfWidth < 0 && rightWidth > leftWidth) {
            left = leftByRight;
        } else if (preferred[1] != 'right') {
            left = leftByLeft;
        } else {
            left = leftByRight;
        }

        self.offset({left: left, top: top});
        if ($.fn.bgiframe && !self.is("img")) {
            self.bgiframe();
        }
    };
    $.fn.positionSideOf = function (of, options) {
        var settings = {
            preferred: ['bottom', 'right'],
            spacing: 1
        };
        $.extend(settings, options);

        var ofOffset = of.offset();
        var selfWidth = this.outerWidth();
        var selfHeight = this.outerHeight();
        var topHeight = ofOffset.top - $(document).scrollTop() + of.outerHeight();
        var bottomHeight = $(document).scrollTop() + $(window).height() - ofOffset.top;
        var leftWidth = ofOffset.left - $(document).scrollLeft();
        var rightWidth = $(document).scrollLeft() + $(window).width() - ofOffset.left - of.outerWidth();
        var topByTop = ofOffset.top + of.outerHeight() - selfHeight;
        var topByBottom = ofOffset.top;
        var leftByLeft = ofOffset.left - selfWidth - settings.spacing;
        var leftByRight = ofOffset.left + of.outerWidth() + settings.spacing;

        position(settings.preferred, this, selfWidth, selfHeight, topHeight, bottomHeight, leftWidth, rightWidth, topByTop, topByBottom, leftByLeft, leftByRight);
    };
    $.fn.positionOf = function (of, options) {
        var settings = {
            preferred: ['bottom', 'right'],
            spacing: 1
        };
        $.extend(settings, options);

        var ofOffset = of.offset();
        var selfWidth = this.outerWidth();
        var selfHeight = this.outerHeight();
        var leftWidth = ofOffset.left - $(document).scrollLeft() + of.outerWidth();
        var rightWidth = $(document).scrollLeft() + $(window).width() - ofOffset.left;
        var topHeight = ofOffset.top - $(document).scrollTop();
        var bottomHeight = $(document).scrollTop() + $(window).height() - ofOffset.top - of.outerHeight();
        var topByTop = ofOffset.top - selfHeight - settings.spacing;
        var topByBottom = ofOffset.top + of.outerHeight() + settings.spacing;
        var leftByRight = ofOffset.left;
        var leftByLeft = ofOffset.left + of.outerWidth() - selfWidth;
        position(settings.preferred, this, selfWidth, selfHeight, topHeight, bottomHeight, leftWidth, rightWidth, topByTop, topByBottom, leftByLeft, leftByRight);
    };
})(jQuery);
Cms = {};
Cms.check = function (name, checked) {
    $("input:checkbox[name=" + name + "]").each(function () {
        this.checked = checked;
    });
};
Cms.checkeds = function (name) {
    return $("input:checkbox:enabled:checked[name=" + name + "]").length;
};
Cms.moveTop = function (name, nested) {
    nested = nested || 2;
    $.each($("input:checkbox:checked[name=" + name + "]").toArray().reverse(), function () {
        var toMove = $(this);
        for (var i = 0; i < nested; i++) {
            toMove = toMove.parent();
        }
        toMove.prevAll().last().before(toMove);
    });
};
Cms.moveUp = function (name, nested) {
    nested = nested || 2;
    $("input:checkbox:checked[name=" + name + "]").each(function () {
        var toMove = $(this);
        for (var i = 0; i < nested; i++) {
            toMove = toMove.parent();
        }
        toMove.prev().before(toMove);
    });
};
Cms.moveDown = function (name, nested) {
    nested = nested || 2;
    $.each($("input:checkbox:checked[name=" + name + "]").toArray().reverse(), function () {
        var toMove = $(this);
        for (var i = 0; i < nested; i++) {
            toMove = toMove.parent();
        }
        toMove.next().after(toMove);
        //hack 在ie8中将首行往下移动，出现诡异的布局混乱
        toMove.prev().mouseover().mouseout();
    });
};
Cms.moveBottom = function (name, nested) {
    nested = nested || 2;
    $("input:checkbox:checked[name=" + name + "]").each(function () {
        var toMove = $(this);
        for (var i = 0; i < nested; i++) {
            toMove = toMove.parent();
        }
        toMove.nextAll().last().after(toMove);
        //hack 在ie8中将首行往下移动，出现诡异的布局混乱
        toMove.prevAll().last().mouseover().mouseout();
    });
};
/**
 * button_placeholder_id
 * progressTarget
 * cancelButtonId
 * upload_url
 * file_post_name
 * file_size_limit
 * file_types
 * file_types_description
 * button_action：控制文件单选、多选
 * queue_complete_handler：上传完成事件
 */
Cms.swfUpload = function (name, settings) {
    if (!settings || !settings.upload_url) {
        alert("upload_url cannot be null!");
    }
    settings.upload_url = Bw.appendJsessionid(settings.upload_url, settings.jsessionid);
    var def_settings = {
        flash_url: CTX + "/static/vendor/swfupload/swfupload.swf",
        flash9_url: CTX + "/static/vendor/swfupload/swfupload_fp9.swf",
        file_post_name: "file",
        //file_size_limit : "100 MB",
        file_types: "*.*",
        file_types_description: "All Files",
        //file_upload_limit : 100,
        file_queue_limit: 0,
        custom_settings: {
            progressTarget: name + "SwfProgress",
            cancelButtonId: name + "SwfCancel"
        },

        debug: false,

        // Button settings
        // button_image_url: CTX + "/static/vendor/swfupload/xpbutton_61x22.png",
        button_cursor: SWFUpload.CURSOR.HAND,
        button_width: "54",
        button_height: "34",
        button_placeholder_id: name + "SwfButton",
        button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
        button_action: SWFUpload.BUTTON_ACTION.SELECT_FILES,

        // The event handler functions are defined in handlers.js
        file_queued_handler: fileQueued,
        file_queue_error_handler: fileQueueError,
        file_dialog_complete_handler: fileDialogComplete,
        upload_start_handler: uploadStart,
        upload_progress_handler: uploadProgress,
        upload_error_handler: uploadError,
        upload_success_handler: uploadSuccess,
        upload_complete_handler: uploadComplete
    };
    $.extend(def_settings, settings);
    return new SWFUpload(def_settings);
};
Cms.swfUploadFile = function (name, settings) {
    settings = settings || {};
    settings.button_action = SWFUpload.BUTTON_ACTION.SELECT_FILE;
    settings.file_types_description = "Files";
    settings.upload_url = settings.upload_url || CTX + CMSCP + "/core/upload_file.do?_site=" + ($.cookie("_site") || "");
    settings.upload_success_handler = function (file, serverData) {
        doUploadSuccess(file, serverData, this);
        var data = $.parseJSON(serverData);
        $("#" + name).val(data.fileUrl);
        $("#" + name + "Name").val(data.fileName);
        $("#" + name + "Length").val(data.fileLength);
    };
    return Cms.swfUpload(name, settings);
};
Cms.swfUploadFiles = function (name, settings, addFileRow) {
    settings = settings || {};
    settings.button_action = SWFUpload.BUTTON_ACTION.SELECT_FILES;
    settings.file_types_description = "Files";
    settings.upload_url = settings.upload_url || CTX + CMSCP + "/core/upload_file.do?_site=" + ($.cookie("_site") || "");
    settings.upload_success_handler = function (file, serverData) {
        doUploadSuccess(file, serverData, this);
        var data = $.parseJSON(serverData);
        addFileRow(data.fileUrl, data.fileName, data.fileLength);
    };
    return Cms.swfUpload(name, settings);
};
Cms.swfUploadVideo = function (name, settings) {
    settings = settings || {};
    settings.button_action = SWFUpload.BUTTON_ACTION.SELECT_FILE;
    settings.file_types_description = "Videos";
    settings.upload_url = settings.upload_url || CTX + CMSCP + "/core/upload_video.do?_site=" + ($.cookie("_site") || "");
    settings.upload_success_handler = function (file, serverData) {
        doUploadSuccess(file, serverData, this);
        var data = $.parseJSON(serverData);
        $("#" + name).val(data.fileUrl);
        $("#" + name + "Name").val(data.fileName);
        $("#" + name + "Length").val(data.fileLength);
    };
    return Cms.swfUpload(name, settings);
};
Cms.swfUploadDoc = function (name, settings) {
    settings = settings || {};
    settings.button_action = SWFUpload.BUTTON_ACTION.SELECT_FILE;
    settings.file_types_description = "Documents";
    settings.upload_url = settings.upload_url || CTX + CMSCP + "/core/upload_doc.do?_site=" + ($.cookie("_site") || "");
    settings.upload_success_handler = function (file, serverData) {
        doUploadSuccess(file, serverData, this);
        var data = $.parseJSON(serverData);
        $("#" + name).val(data.fileUrl);
        $("#" + name + "Name").val(data.fileName);
        $("#" + name + "Length").val(data.fileLength);
        if (data.pdfUrl) {
            $("#" + name + "Pdf").val(data.pdfUrl);
        }
        if (data.swfUrl) {
            $("#" + name + "Swf").val(data.swfUrl);
        }
    };
    return Cms.swfUpload(name, settings);
};
Cms.swfUploadImage = function (name, settings) {
    settings = settings || {};
    settings.button_action = SWFUpload.BUTTON_ACTION.SELECT_FILE;
    settings.file_types_description = "Images";
    settings.upload_url = settings.upload_url || CTX + CMSCP + "/core/upload_image.do?_site=" + ($.cookie("_site") || "");
    settings.upload_start_handler = function (file) {
        var params = {};
        if ($("#s_" + name).length != 0) {
            params.scale = $("#s_" + name).prop("checked");
        }
        if ($("#e_" + name).length != 0) {
            params.exact = $("#e_" + name).prop("checked");
        }
        if ($("#wm_" + name).length != 0) {
            params.watermark = $("#wm_" + name).prop("checked");
        }
        if ($("#w_" + name).length != 0) {
            params.width = $("#w_" + name).val();
        }
        if ($("#h_" + name).length != 0) {
            params.height = $("#h_" + name).val();
        }
        if ($("#t_" + name).length != 0) {
            params.thumbnail = $("#t_" + name).val();
        }
        if ($("#tw_" + name).length != 0) {
            params.thumbnailWidth = $("#tw_" + name).val();
        }
        if ($("#th_" + name).length != 0) {
            params.thumbnailHeight = $("#th_" + name).val();
        }
        this.setPostParams(params);
        doUploadStart(file, this);
    };
    settings.upload_success_handler = function (file, serverData) {
        doUploadSuccess(file, serverData, this);
        var data = $.parseJSON(serverData);
        $("#" + name).val(data.fileUrl).change();
    };
    return Cms.swfUpload(name, settings);
};
Cms.swfUploadImages = function (name, settings, addImageRow) {
    settings = settings || {};
    settings.button_action = SWFUpload.BUTTON_ACTION.SELECT_FILES;
    settings.file_types_description = "Images";
    settings.upload_url = settings.upload_url || CTX + CMSCP + "/core/upload_image.do?_site=" + ($.cookie("_site") || "");
    settings.upload_start_handler = function (file) {
        var params = {};
        if ($("#s_" + name).length != 0) {
            params.scale = $("#s_" + name).prop("checked");
        }
        if ($("#e_" + name).length != 0) {
            params.exact = $("#e_" + name).prop("checked");
        }
        if ($("#wm_" + name).length != 0) {
            params.watermark = $("#wm_" + name).prop("checked");
        }
        if ($("#w_" + name).length != 0) {
            params.width = $("#w_" + name).val();
        }
        if ($("#h_" + name).length != 0) {
            params.height = $("#h_" + name).val();
        }
        if ($("#t_" + name).length != 0) {
            params.thumbnail = $("#t_" + name).val();
        }
        if ($("#tw_" + name).length != 0) {
            params.thumbnailWidth = $("#tw_" + name).val();
        }
        if ($("#th_" + name).length != 0) {
            params.thumbnailHeight = $("#th_" + name).val();
        }
        this.setPostParams(params);
        doUploadStart(file, this);
    };
    settings.upload_success_handler = function (file, serverData) {
        doUploadSuccess(file, serverData, this);
        var data = $.parseJSON(serverData);
        addImageRow(data.fileUrl, '');
    };
    return Cms.swfUpload(name, settings);
};

Cms.jfUpload = function (name, settings) {
    settings = settings || {};
    settings.maxNumberOfFiles = settings.maxNumberOfFiles || 1;
    if (settings.file_size_limit && settings.file_size_limit > 0) {
        settings.maxFileSize = settings.file_size_limit * 1024;
    }
    settings.start = function (e) {
        $('#' + name + 'Progress').show();
        $('#' + name + 'Progress .progress-bar').css('width', '0%');
    };
    settings.always = function (e) {
        setTimeout(function () {
            $('#' + name + 'Progress').hide('fast');
            $('#' + name + 'Progress .progress-bar').css('width', '0%');
        }, 1000);
        if(settings.afterAlways) {
            settings.afterAlways();
        }
    };
    settings.progress = function (e, data) {
        var progress = parseInt(data.loaded / data.total * 100, 10);
        $('#' + name + 'Progress .progress-bar').css( 'width', progress + '%');
    };
    settings.messages = {
        maxFileSize: '文件太大了',
            acceptFileTypes: '该文件类型不允许上传'
    };
    settings.processfail = function (e, data) {
        var currentFile = data.files[data.index];
        if (data.files.error && currentFile.error) {
            alert(currentFile.error);
        }
    };
    $('#' + name + 'FileUpload').fileupload(settings).prop('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');
};
Cms.jfUploadFile = function (name, settings) {
    settings = settings || {};
    settings.dataType = 'json';
    settings.url = settings.url || CTX + CMSCP + "/core/upload_file.do?_site=" + ($.cookie("_site") || "");
    settings.maxNumberOfFiles = settings.maxNumberOfFiles || 1;
    if (settings.file_size_limit && settings.file_size_limit > 0) {
        settings.maxFileSize = settings.file_size_limit * 1024;
    }
    settings.done = function (e, data) {
        $("#" + name).val(data.result.fileUrl);
        $("#" + name + "Name").val(data.result.fileName);
        $("#" + name + "Length").val(data.result.fileLength);
    };
    settings.start = function (e) {
        $('#' + name + 'Progress').show();
        $('#' + name + 'Progress .progress-bar').css('width', '0%');
    };
    settings.always = function (e) {
        setTimeout(function () {
            $('#' + name + 'Progress').hide('fast');
            $('#' + name + 'Progress .progress-bar').css('width', '0%');
        }, 1000);
    };
    settings.progress = function (e, data) {
        var progress = parseInt(data.loaded / data.total * 100, 10);
        $('#' + name + 'Progress .progress-bar').css( 'width', progress + '%');
    };
    settings.messages = {
        maxFileSize: '文件太大了',
        acceptFileTypes: '该文件类型不允许上传'
    };
    settings.processfail = function (e, data) {
        var currentFile = data.files[data.index];
        if (data.files.error && currentFile.error) {
            alert(currentFile.error);
        }
    };
    var jfu = $('#' + name + 'FileUpload').fileupload(settings);
    jfu.prop('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');
    return jfu;
};
Cms.jfUploadFiles = function (name, settings, addFileRow) {
    settings = settings || {};
    settings.dataType = 'json';
    settings.url = settings.url || CTX + CMSCP + "/core/upload_file.do?_site=" + ($.cookie("_site") || "");
    settings.maxNumberOfFiles = settings.maxNumberOfFiles || 1;
    if (settings.file_size_limit && settings.file_size_limit > 0) {
        settings.maxFileSize = settings.file_size_limit * 1024;
    }
    settings.done = function (e, data) {
        addFileRow(data.result.fileUrl, data.result.fileName, data.result.fileLength);
    };
    settings.start = function (e) {
        $('#' + name + 'Progress').show();
        $('#' + name + 'Progress .progress-bar').css('width', '0%');
    };
    settings.always = function (e) {
        setTimeout(function () {
            $('#' + name + 'Progress').hide('fast');
            $('#' + name + 'Progress .progress-bar').css('width', '0%');
        }, 1000);
    };
    settings.progress = function (e, data) {
        var progress = parseInt(data.loaded / data.total * 100, 10);
        $('#' + name + 'Progress .progress-bar').css( 'width', progress + '%');
    };
    settings.messages = {
        maxFileSize: '文件太大了',
        acceptFileTypes: '该文件类型不允许上传'
    };
    settings.processfail = function (e, data) {
        var currentFile = data.files[data.index];
        if (data.files.error && currentFile.error) {
            alert(currentFile.error);
        }
    };
    var jfu = $('#' + name + 'FileUpload').fileupload(settings);
    jfu.prop('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');
    return jfu;
};
Cms.jfUploadVideo = function (name, settings) {
    settings = settings || {};
    settings.dataType = 'json';
    settings.url = settings.url || CTX + CMSCP + "/core/upload_video.do?_site=" + ($.cookie("_site") || "");
    settings.maxNumberOfFiles = settings.maxNumberOfFiles || 1;
    if (settings.file_size_limit && settings.file_size_limit > 0) {
        settings.maxFileSize = settings.file_size_limit * 1024;
    }
    settings.done = function (e, data) {
        $("#" + name).val(data.result.fileUrl);
        $("#" + name + "Name").val(data.result.fileName);
        $("#" + name + "Length").val(data.result.fileLength);
    };
    settings.start = function (e) {
        $('#' + name + 'Progress').show();
        $('#' + name + 'Progress .progress-bar').css('width', '0%');
    };
    settings.always = function (e) {
        setTimeout(function () {
            $('#' + name + 'Progress').hide('fast');
            $('#' + name + 'Progress .progress-bar').css('width', '0%');
        }, 1000);
    };
    settings.progress = function (e, data) {
        var progress = parseInt(data.loaded / data.total * 100, 10);
        $('#' + name + 'Progress .progress-bar').css( 'width', progress + '%');
    };
    settings.messages = {
        maxFileSize: '文件太大了',
        acceptFileTypes: '该文件类型不允许上传'
    };
    settings.processfail = function (e, data) {
        var currentFile = data.files[data.index];
        if (data.files.error && currentFile.error) {
            alert(currentFile.error);
        }
    };
    var jfu = $('#' + name + 'FileUpload').fileupload(settings);
    jfu.prop('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');
    return jfu;
};
Cms.jfUploadFlash = function (name, settings) {
    settings = settings || {};
    settings.dataType = 'json';
    settings.url = settings.url || CTX + CMSCP + "/core/upload_flash.do?_site=" + ($.cookie("_site") || "");
    settings.maxNumberOfFiles = settings.maxNumberOfFiles || 1;
    if (settings.file_size_limit && settings.file_size_limit > 0) {
        settings.maxFileSize = settings.file_size_limit * 1024;
    }
    settings.done = function (e, data) {
        $("#" + name).val(data.result.fileUrl);
    };
    settings.start = function (e) {
        $('#' + name + 'Progress').show();
        $('#' + name + 'Progress .progress-bar').css('width', '0%');
    };
    settings.always = function (e) {
        setTimeout(function () {
            $('#' + name + 'Progress').hide('fast');
            $('#' + name + 'Progress .progress-bar').css('width', '0%');
        }, 1000);
    };
    settings.progress = function (e, data) {
        var progress = parseInt(data.loaded / data.total * 100, 10);
        $('#' + name + 'Progress .progress-bar').css( 'width', progress + '%');
    };
    settings.messages = {
        maxFileSize: '文件太大了',
        acceptFileTypes: '该文件类型不允许上传'
    };
    settings.processfail = function (e, data) {
        var currentFile = data.files[data.index];
        if (data.files.error && currentFile.error) {
            alert(currentFile.error);
        }
    };
    var jfu = $('#' + name + 'FileUpload').fileupload(settings);
    jfu.prop('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');
    return jfu;
};
Cms.jfUploadDoc = function (name, settings) {
    settings = settings || {};
    settings.dataType = 'json';
    settings.url = settings.url || CTX + CMSCP + "/core/upload_doc.do?_site=" + ($.cookie("_site") || "");
    settings.maxNumberOfFiles = settings.maxNumberOfFiles || 1;
    if (settings.file_size_limit && settings.file_size_limit > 0) {
        settings.maxFileSize = settings.file_size_limit * 1024;
    }
    settings.done = function (e, data) {
        $("#" + name).val(data.result.fileUrl);
        $("#" + name + "Name").val(data.result.fileName);
        $("#" + name + "Length").val(data.result.fileLength);
        if (data.result.pdfUrl) {
            $("#" + name + "Pdf").val(data.result.pdfUrl);
        }
        if (data.result.swfUrl) {
            $("#" + name + "Swf").val(data.result.swfUrl);
        }
    };
    settings.start = function (e) {
        $('#' + name + 'Progress').show();
        $('#' + name + 'Progress .progress-bar').css('width', '0%');
    };
    settings.always = function (e) {
        setTimeout(function () {
            $('#' + name + 'Progress').hide('fast');
            $('#' + name + 'Progress .progress-bar').css('width', '0%');
        }, 1000);
    };
    settings.progress = function (e, data) {
        var progress = parseInt(data.loaded / data.total * 100, 10);
        $('#' + name + 'Progress .progress-bar').css( 'width', progress + '%');
    };
    settings.messages = {
        maxFileSize: '文件太大了',
        acceptFileTypes: '该文件类型不允许上传'
    };
    settings.processfail = function (e, data) {
        var currentFile = data.files[data.index];
        if (data.files.error && currentFile.error) {
            alert(currentFile.error);
        }
    };
    var jfu = $('#' + name + 'FileUpload').fileupload(settings);
    jfu.prop('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');
    return jfu;
};
Cms.jfUploadImage = function (name, settings) {
    settings = settings || {};
    settings.dataType = 'json';
    settings.url = settings.url || CTX + CMSCP + "/core/upload_image.do?_site=" + ($.cookie("_site") || "");
    settings.maxNumberOfFiles = settings.maxNumberOfFiles || 1;
    if (settings.file_size_limit && settings.file_size_limit > 0) {
        settings.maxFileSize = settings.file_size_limit * 1024;
    }
    // acceptFileTypes : /(\.|\/)(gif|jpe?g|png)$/i,
    // maxFileSize: 10000000,
    settings.formData = {};
    if ($("#s_" + name).length != 0) {
        settings.formData.scale = $("#s_" + name).prop("checked");
    }
    if ($("#e_" + name).length != 0) {
        settings.formData.exact = $("#e_" + name).prop("checked");
    }
    if ($("#wm_" + name).length != 0) {
        settings.formData.watermark = $("#wm_" + name).prop("checked");
    }
    if ($("#w_" + name).length != 0) {
        settings.formData.width = $("#w_" + name).val();
    }
    if ($("#h_" + name).length != 0) {
        settings.formData.height = $("#h_" + name).val();
    }
    if ($("#t_" + name).length != 0) {
        settings.formData.thumbnail = $("#t_" + name).val();
    }
    if ($("#tw_" + name).length != 0) {
        settings.formData.thumbnailWidth = $("#tw_" + name).val();
    }
    if ($("#th_" + name).length != 0) {
        settings.formData.thumbnailHeight = $("#th_" + name).val();
    }
    settings.done = function (e, data) {
        $("#" + name).val(data.result.fileUrl).change();
    };
    settings.start = function (e) {
        $('#' + name + 'Progress').show();
        $('#' + name + 'Progress .progress-bar').css('width', '0%');
    };
    settings.always = function (e) {
        setTimeout(function () {
            $('#' + name + 'Progress').hide('fast');
            $('#' + name + 'Progress .progress-bar').css('width', '0%');
        }, 1000);
    };
    settings.progress = function (e, data) {
        var progress = parseInt(data.loaded / data.total * 100, 10);
        $('#' + name + 'Progress .progress-bar').css( 'width', progress + '%');
    };
    settings.messages = {
        maxFileSize: '文件太大了',
        acceptFileTypes: '该文件类型不允许上传'
    };
    settings.processfail = function (e, data) {
        var currentFile = data.files[data.index];
        if (data.files.error && currentFile.error) {
            alert(currentFile.error);
        }
    };
    var jfu = $('#' + name + 'FileUpload').fileupload(settings);
    jfu.prop('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');
    return jfu;
};
Cms.jfUploadImages = function (name, settings, addImageRow) {
    settings = settings || {};
    settings.dataType = 'json';
    settings.url = settings.url || CTX + CMSCP + "/core/upload_image.do?_site=" + ($.cookie("_site") || "");
    settings.maxNumberOfFiles = settings.maxNumberOfFiles || 1;
    if (settings.file_size_limit && settings.file_size_limit > 0) {
        settings.maxFileSize = settings.file_size_limit * 1024;
    }
    // acceptFileTypes : /(\.|\/)(gif|jpe?g|png)$/i,
    // maxFileSize: 10000000,
    settings.formData = {};
    if ($("#s_" + name).length != 0) {
        settings.formData.scale = $("#s_" + name).prop("checked");
    }
    if ($("#e_" + name).length != 0) {
        settings.formData.exact = $("#e_" + name).prop("checked");
    }
    if ($("#wm_" + name).length != 0) {
        settings.formData.watermark = $("#wm_" + name).prop("checked");
    }
    if ($("#w_" + name).length != 0) {
        settings.formData.width = $("#w_" + name).val();
    }
    if ($("#h_" + name).length != 0) {
        settings.formData.height = $("#h_" + name).val();
    }
    if ($("#t_" + name).length != 0) {
        settings.formData.thumbnail = $("#t_" + name).val();
    }
    if ($("#tw_" + name).length != 0) {
        settings.formData.thumbnailWidth = $("#tw_" + name).val();
    }
    if ($("#th_" + name).length != 0) {
        settings.formData.thumbnailHeight = $("#th_" + name).val();
    };
    settings.done = function (e, data) {
        addImageRow(data.result.fileUrl, '');
    };
    settings.start = function (e) {
        $('#' + name + 'Progress').show();
        $('#' + name + 'Progress .progress-bar').css('width', '0%');
    };
    settings.always = function (e) {
        setTimeout(function () {
            $('#' + name + 'Progress').hide('fast');
            $('#' + name + 'Progress .progress-bar').css('width', '0%');
        }, 1000);
    };
    settings.progress = function (e, data) {
        var progress = parseInt(data.loaded / data.total * 100, 10);
        $('#' + name + 'Progress .progress-bar').css( 'width', progress + '%');
    };
    settings.messages = {
        maxFileSize: '文件太大了',
        acceptFileTypes: '该文件类型不允许上传'
    };
    settings.processfail = function (e, data) {
        var currentFile = data.files[data.index];
        if (data.files.error && currentFile.error) {
            alert(currentFile.error);
        }
    };
    var jfu = $('#' + name + 'FileUpload').fileupload(settings);
    jfu.prop('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');
    return jfu;
};

Cms.scaleImg = function (imgId, maxWidth, maxHeight, src) {
    if (src == "") {
        $("#" + imgId).hide();
        return;
    }
    if (src.indexOf("?") == -1) {
        src += "?d=" + new Date() * 1;
    } else {
        src += "&d=" + new Date() * 1;
    }
    var id = "scaleImg" + new Date() * 1;
    var imgHtml = "<img id='" + id + "' src='" + src + "' style='position:absolute;top:-99999px;left:-99999px'/>";
    $(imgHtml).appendTo($("#" + imgId).parent());
    $("#" + id).load(function () {
        var width = this.width, height = this.height;
        if ((width != 0 && height != 0) && (width > maxWidth || height > maxHeight)) {
            var scale = width / maxWidth > height / maxHeight ? width / maxWidth : height / maxHeight;
            $("#" + imgId).width(width / scale).height(height / scale);
        } else {
            $("#" + imgId).width(width).height(height);
        }
        $(this).remove();
        $("#" + imgId).attr("src", src).show();
    }).error(function () {
        $("#" + imgId).hide();
    });
    return id;
};
Cms.imgCrop = function (action, name) {
    var frameId = "img_area_select_iframe";
    var iframe = $("#" + frameId).get(0);
    if (typeof(iframe) == "undefined") {
        var iframeHtml = '<iframe id="' + frameId + '" name="' + frameId + '" src="javascript:false" style="position:absolute;top:-99999px;left:-99999px"/>';
        $(iframeHtml).appendTo(document.body);
        iframe = $("#" + frameId).get(0);
    }
    var url = action + "?d=" + new Date() * 1 + "&src=" + $("#" + name).val() + "&targetWidth=" + $("#w_" + name).val() + "&targetHeight=" + $("#h_" + name).val() + "&targetFrame=" + frameId + "&name=" + name;
    window.open(url, "img_are_select", "height=625, width=1000, top=0, left=center, toolbar=no, menubar=no, scrollbars=auto, resizable=yes,location=no, status=no");
};
// jQuery plugin
(function ($) {
    $.fn.headSort = function () {
        var sort = $(this).attr("pagesort");
        var dir = $(this).attr("pagedir");
        var url = $(this).attr("pageurl");
        $(".ls-sort").each(function () {
            if (sort == $(this).attr("pagesort")) {
                $(this).addClass("ls-sort-" + dir);
            }
        });
        $(this).find(".ls-th-sort").each(function () {
            var sortSpan = $(this).find(".ls-sort");
            var currSort = sortSpan.attr("pagesort");
            var currDir = sort == currSort && dir == "asc" ? "desc" : "asc";
            var currUrl = $.format(url, currSort, currDir);
            $(this).click(function () {
                window.open(currUrl, "_self");
            });
            $(this).hover(function () {
                if (currSort != sort) {
                    sortSpan.addClass("ls-sort-asc");
                }
            }, function () {
                if (currSort != sort) {
                    sortSpan.removeClass("ls-sort-asc");
                }
            });
        });
    };
})(jQuery);
