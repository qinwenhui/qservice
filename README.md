# qservice
依赖于mybatis和tk.maybatis的通用Service生成插件  
#使用方式  
在mybatis逆向工程配置generationConfig.xml文件里加入该插件即可  
例如：  
`<plugin type="cn.qinwh.mybatis.qservice.generator.ServicePlugin">  
  <property name="targetPackage" value="cn.qinwh.qbooksystem.service"/>  
  <property name="targetProject" value="qbook-system/src/main/java"/>  
</plugin>`  
