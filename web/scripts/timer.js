
function CountDown() {
    if (maxtime >= 0) {
        minutes = Math.floor(maxtime / 60);
        seconds = Math.floor(maxtime % 60);
        msg = minutes + " 分 " + seconds + " 秒";

        $(".countdown").html(msg);
        if (maxtime == 5 * 60) alert('注意，还有5分钟!');
        --maxtime;
    }
    else {
        clearInterval(timer);
        alert("时间到，结束!");
    }
}


