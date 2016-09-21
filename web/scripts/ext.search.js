$(function(){
    $(".x-toolbar input:submit").after('&nbsp;<input class="button ext-search-button" type="button" value="更多条件"/>');
    $(".ext-search-button").toggle(function(){
        $(".ext-search-tr").show();
    },function(){
        $(".ext-search-tr").hide();
    });
});