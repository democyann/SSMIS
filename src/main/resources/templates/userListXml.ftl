<#setting number_format="0"/> 
<users>
<#list data.users as user>
   <user>
       <id>${user.getId()?default("")}</id>
       <name>${user.getName()?default("")}</name>        
   </user>
</#list>
</users>