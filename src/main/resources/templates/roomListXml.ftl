<#setting number_format="0"/> 
<rooms>
<#list data.rooms as room>
   <room>
       <name>${room.getName()?default("")}</name>
       <master>${room.getMaster()?default("")}</master>
       <masterName>${room.getMasterName()?default("")}</masterName>
       <members>${room.getMembers()?default("")}</members>
       <status>${room.getStatus()?default("")}</status>
   </room>
</#list>
</rooms>