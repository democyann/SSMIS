<#setting number_format="0"/> 
<room>
    <name>${data.room.name}</name>   
    <remark>${data.room.remark?default("")}</remark> 
    <members>
        <#list data.members as member>
           <member>
               <name>${member?default("")}</name>
           </member>
        </#list>
    </members>
</room>