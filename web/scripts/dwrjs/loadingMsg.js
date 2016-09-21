dwr.util._disabledZoneUseCount = 0;
dwr.util.useLoadingMessage = function(message) {
 
  dwr.engine.setPreHook(function() {
    var disabledZone = dwr.util.byId('disabledImageZone');
    disabledZone.style.visibility = 'visible';
    disabledZone.style.display = '';
    dwr.util._disabledZoneUseCount++;

  });
  dwr.engine.setPostHook(function() {
    dwr.util._disabledZoneUseCount--;
    if (dwr.util._disabledZoneUseCount == 0) {      
      var disabledZone = dwr.util.byId('disabledImageZone');
      disabledZone.style.visibility = 'hidden';
      disabledZone.style.display = 'none';
    }
  });
};

if (window.attachEvent) {  
    window.attachEvent('onload', function() {dwr.util.useLoadingMessage();});  
}else if(window.addEventListener){
    window.addEventListener('load', function() {dwr.util.useLoadingMessage();}, false );  
}