//document.write('<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.3"></script>');
//document.write('<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false&language=zh-CN"></script>');
loadScript("http://api.map.baidu.com/getscript?v=1.3&key=&services=&t=",typeof executeLocation==="undefined"?null:executeLocation);


function locationInBaidu(latitude, longitude,item) {
    var myGeo = new BMap.Geocoder();
    myGeo.getLocation(new BMap.Point(longitude, latitude), function (result) {
        if (result) {
            item.html(result.address);
            _setCookie("userLocation",result.address);
            //item.html('<div class="address-div">'+result.address+'</div>');
        }
        else
        {

        }
    });
}

function pointInBaidu(address,func,city,noAlert){
    var myGeo = new BMap.Geocoder();
    myGeo.getPoint(address, function(point){
        if (point) {
            func(point);
        }
        else{
            if(noAlert){
                func(null);
            }
            else
                alert("未查询到位置信息!")
        }
    }, city);
}

/*    function locationInGoogle(latitude, longitude,item) {
    var geocoder = new google.maps.Geocoder();
    var address = latitude + "," + longitude;
    geocoder.geocode({ 'address':address, "language":"zh_cn"}, function (results, status) {
        if (status == google.maps.GeocoderStatus.OK) {
            if (results[0]) {
                item.html( results[0].formatted_address);
            }
        } else {
            //alert("Geocode was not successful for the following reason: " + status);
        }
    });
}*/




