var get = {
 byId: function(id) {
  return document.getElementById(id)
 },
 byClass: function(sClass, oParent) {
  var aClass = [];
  var reClass = new RegExp("(^| )" + sClass + "( |$)");
  var aElem = this.byTagName("*", oParent);
  for (var i = 0; i < aElem.length; i++) reClass.test(aElem[i].className) && aClass.push(aElem[i]);
  return aClass
 },
 byTagName: function(elem, obj) {
  return (obj || document).getElementsByTagName(elem)
 }
};
function menujs()
{
 var oNav = get.byId("nav");
 var aLi = get.byTagName("td", oNav);
 var aSubNav = get.byClass("subnav", oNav);
 var oSubNav = oEm = timer = null;
 var i = 0;
 
 for (i = 0; i < aLi.length-2; i++)
 {
  aLi[i].onmouseover = function ()
  {
   //���������Ӳ˵�
   for (i = 0; i < aSubNav.length; i++)aSubNav[i].style.display = "none";
   
   //��ȡ�����µ��Ӳ˵�
   oSubNav = get.byClass("subnav", this)[0];
   
   //��ȡ�����µ�ָʾ��ͷ
   oEm = get.byTagName("em", this)[0];
   
   //��ʾ�����µ��Ӳ˵�
   oSubNav.style.display = "block";
   
   //�ж���ʾ����
   oNav.offsetWidth - this.offsetLeft > oSubNav.offsetWidth ? 
   
   //�������ʾ��Χ������ʾ
   oSubNav.style.left = this.offsetLeft + "px" :
   
   //������ʾ��Χ������ʾ
   oSubNav.style.right = 0;
   
   //����ָ���ͷ��ʾλ��
   oEm.style.left = this.offsetLeft - oSubNav.offsetLeft + 40 + "px";
   clearTimeout(timer);
   
   //��ֹ�¼�ð��
   oSubNav.onmouseover = function (event)
   {
    (event || window.event).cancelBubble = true;
    clearTimeout(timer)
   }
  };
  
  aLi[i].onmouseout = function ()
  {
   timer = setTimeout(function () {
    oSubNav.style.display = "none"
   },300) 
  }
 }
};