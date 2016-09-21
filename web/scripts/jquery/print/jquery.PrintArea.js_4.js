/**
 *  Version 2.1
 *      -Contributors: "mindinquiring" : filter to exclude any stylesheet other than print.
 *  Tested ONLY in IE 8 and FF 3.6. No official support for other browsers, but will
 *      TRY to accomodate challenges in other browsers.
 *  Example:
 *      Print Button: <div id="print_button">Print</div>
 *      Print Area  : <div class="PrintArea"> ... html ... </div>
 *      Javascript  : <script>
 *                       $("div#print_button").click(function(){
 *                           $("div.PrintArea").printArea( [OPTIONS] );
 *                       });
 *                     </script>
 *  options are passed as json (json example: {mode: "popup", popClose: false})
 *
 *  {OPTIONS} | [type]    | (default), values      | Explanation
 *  --------- | --------- | ---------------------- | -----------
 *  @mode     | [string]  | ("iframe"),"popup"     | printable window is either iframe or browser popup
 *  @popHt    | [number]  | (500)                  | popup window height
 *  @popWd    | [number]  | (400)                  | popup window width
 *  @popX     | [number]  | (500)                  | popup window screen X position
 *  @popY     | [number]  | (500)                  | popup window screen Y position
 *  @popTitle | [string]  | ('')                   | popup window title element
 *  @popClose | [boolean] | (false),true           | popup window close after printing
 *  @strict   | [boolean] | (undefined),true,false | strict or loose(Transitional) html 4.01 document standard or undefined to not include at all (only for popup option)
 */
(function ($) {
    var counter = 0;
    var modes = { iframe:"iframe", popup:"popup" };
    var defaults = {
        mode:modes.popup,
        popHt:500,
        popWd:400,
        popX:200,
        popY:200,
        popTitle:'',
        popClose:true
    };

    var settings = {};//global settings

    $.fn.printArea = function (options) {
        $.extend(settings, defaults, options);

        counter++;
        var idPrefix = "printArea_";
        $("[id^=" + idPrefix + "]").remove();
        var ele = getFormData($(this));

        settings.id = idPrefix + counter;

        var writeDoc;
        var printWindow;

        switch (settings.mode) {
            case modes.iframe :
                var f = new Iframe();
                writeDoc = f.doc;
                printWindow = f.contentWindow || f;
                break;
            case modes.popup :
                printWindow = new Popup();
                writeDoc = printWindow.doc;
        }

        writeDoc.open();
        writeDoc.write(docType() + "<html>" + getHead() + getBody(ele) + "</html>");
        writeDoc.close();

        printWindow.focus();
        printWindow.print();

        if (settings.mode == modes.popup && settings.popClose)
            printWindow.close();
    }

    function docType() {
        if (settings.standard == true)
            return "<!doctype html>";
        else return "";
    }

    function getHead() {
        var head = "<head><title>" + settings.popTitle + "</title>";
        //link
        $(document).find("link")
            .filter(function () {
                return $(this).attr("rel").toLowerCase() == "stylesheet";
            })
            .filter(function () { // this filter contributed by "mindinquiring"
                var media = $(this).attr("media");
                return (typeof media === "undefined" || media.toLowerCase() == "" || media.toLowerCase() == "print")
            })
            .each(function () {
                head += '<link type="text/css" rel="stylesheet" href="' + $(this).attr("href") + '" >';
            });
        head += '<link type="text/css" rel="stylesheet" href="/zjimis/scripts/jquery/print/print.css"/>';
        head += "</head>";

        //style
        if (settings.pageStyle == true) {
            $(document).find("style")
                .filter(function () {
                    var media = $(this).attr("media");
                    return (typeof media === "undefined" || media.toLowerCase() == "" || media.toLowerCase() == "print")
                })
                .each(function () {
                    head += '<style>' + $(this).html() + '</style>'
                });
        }
        return head;
    }

    function getBody(printElement) {
        if(typeof settings.printMore==="undefined" || settings.printMore=="1"){
            return '<body><div class="' + $(printElement).attr("class") + '">' + $(printElement).html() + '</div></body>';
        }
        else if(settings.printMore=="2"){
            var content1 = '<div class="print-area-height ' + $(printElement).attr("class") + '">' + $(printElement).html() + '</div>';
            var content2 = '<div class="' + $(printElement).attr("class") + '">' + $(printElement).html() + '</div>';
            return '<body>'+content1+content2+'</body>';
        }
        else if(settings.printMore=="3"){
            var content1 = '<div class="print-area-height-2 ' + $(printElement).attr("class") + '">' + $(printElement).html() + '</div>';
            var content2 = '<div class="' + $(printElement).attr("class") + '">' + $(printElement).html() + '</div>';
            return '<body>'+content1+content1+content2+'</body>';
        }
    }

    function getFormData(ele) {
        $("input,select,textarea", ele).each(function () {
            // In cases where radio, checkboxes and select elements are selected and deselected, and the print
            // button is pressed between select/deselect, the print screen shows incorrectly selected elements.
            // To ensure that the correct inputs are selected, when eventually printed, we must inspect each dom element
            var type = $(this).attr("type");
            if (type == "radio" || type == "checkbox") {
                if ($(this).is(":not(:checked)")) this.removeAttribute("checked");
                else this.setAttribute("checked", true);
            }
            else if (type == "text")
                this.setAttribute("value", $(this).val());
            else if (type == "select-multiple" || type == "select-one")
                $(this).find("option").each(function () {
                    if ($(this).is(":not(:selected)")) this.removeAttribute("selected");
                    else this.setAttribute("selected", true);
                });
            else if (type == "textarea") {
                var v = $(this).attr("value");
                if ($.browser.mozilla) {
                    if (this.firstChild) this.firstChild.textContent = v;
                    else this.textContent = v;
                }
                else this.innerHTML = v;
            }
        });
        return ele;
    }

    function Iframe() {
        var frameId = settings.id;
        var iframeStyle = 'border:0;position:absolute;width:0px;height:0px;left:0px;top:0px;';
        var iframe;
        try {
            iframe = document.createElement('iframe');
            document.body.appendChild(iframe);
            $(iframe).attr({ style:iframeStyle, id:frameId, src:"" });
            iframe.doc = null;
            iframe.doc = iframe.contentDocument ? iframe.contentDocument : ( iframe.contentWindow ? iframe.contentWindow.document : iframe.document);
        }
        catch (e) {
            throw e + ". iframes may not be supported in this browser.";
        }
        if (iframe.doc == null) throw "Cannot find document.";

        return iframe;
    }

    function Popup() {
        var windowAttr = "location=yes,statusbar=no,directories=no,menubar=no,titlebar=no,toolbar=no,dependent=no";
        windowAttr += ",width=" + settings.popWd + ",height=" + settings.popHt;
        windowAttr += ",resizable=yes,screenX=" + settings.popX + ",screenY=" + settings.popY + ",personalbar=no,scrollbars=no";
        var newWin = window.open("", "_blank", windowAttr);
        newWin.doc = newWin.document;

        return newWin;
    }
})(jQuery);


//因为ie不能用iframe打印,所以只能用弹出窗口的方式
$(function () {
    if (location.href.search(/view\.do/i)>-1) {
        $(".x-panel-body :button[value=返回],.x-panel-body :button[value=关闭]").before('<input type="button" value="打印" class="button printButton">&nbsp;');
    }
    $(".printButton").bind("click", function () {
        $('.x-panel-body').printArea({
            standard:true, //默认用标准模式打印,兼容模式虽然可以缩放完整显示,但最好还是自己设计一下.
            pageStyle:true,//默认会加载所有页面中定义的样式
            printMore:$("#printCount").length==0?"1":$("#printCount").val()
        });
        return false;
    });
});
