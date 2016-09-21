function doAfterLoad() {
    if (typeof $ == "undefined") {
        setTimeout(doAfterLoad, 1000);
        return;
    }
    $(function(){
        //clear blank in tips
        $(".ellipsis").each(function () {
            var oldTitle = $(this).attr("title");
            $(this).attr("title", $.trim(oldTitle));
        });

        //add reset button for search
        if($(".x-toolbar form").length==1 && $(".x-toolbar :reset,.x-toolbar :button[value=重置]").length==0){
        }

        $(".resetButton").bind("click",function(){
            //如果页面中有自订的重载url,优先
            if(typeof reloadURL === "undefined")
                reloadURL = $(".x-toolbar form").attr("action");
            location.href = reloadURL;
        })
    });
}
doAfterLoad();
