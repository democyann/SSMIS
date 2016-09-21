<files>
<#list data.files as file>
   <file>
       <filename>${file.cnName?default("")}</filename>
       <size>${file.size?default("0")}</size>
       <id>${file.id?c}</id>
   </file>
</#list>
</files>