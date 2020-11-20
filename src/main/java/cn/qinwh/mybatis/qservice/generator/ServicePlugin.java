package cn.qinwh.mybatis.qservice.generator;

import cn.qinwh.mybatis.qservice.factory.TemplateFactory;
import cn.qinwh.mybatis.qservice.factory.impl.ServiceImplTemplateFactory;
import cn.qinwh.mybatis.qservice.factory.impl.ServiceTemplateFactory;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import tk.mybatis.mapper.util.StringUtil;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * @program: qbook
 * @description: 自定义mybatis逆向插件
 * @author: qinwh
 * @create: 2020-11-20 10:41
 **/
public class ServicePlugin extends PluginAdapter {

    private String targetPackage;
    private String targetProject;
    private String className;
    private String packageName;
    private String modelReference;

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String packageStr = this.properties.getProperty("targetPackage");
        String projectStr = this.properties.getProperty("targetProject");
        if(StringUtil.isEmpty(packageStr)){
            throw new RuntimeException("Service插件缺少必要的targetPackage属性!");
        }else if(StringUtil.isEmpty(projectStr)){
            throw new RuntimeException("Service插件缺少必要的targetProject属性!");
        }else{
            this.targetPackage = packageStr;
            this.targetProject = projectStr;
            this.packageName = packageStr.replaceAll("\\.", File.separatorChar=='/'?"/":"\\\\");
        }
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.className = topLevelClass.getType().getShortName();
        this.modelReference = topLevelClass.getType().getFullyQualifiedName();
        try {
            createService();
            createServiceImpl();
        } catch (Exception e) {
            throw new RuntimeException("生成service文件失败!"+e.getMessage());
        }
        return true;
    }

    private void createService() throws Exception{
        String filename = getPath()+File.separatorChar+className+"Service.java";
        TemplateFactory tmplate = new ServiceTemplateFactory(targetPackage, modelReference, className);
        tmplate.buildTmplate(filename);
    }

    private void createServiceImpl() throws Exception{
        String filename = getPath()+File.separatorChar+"impl"+File.separatorChar+className+"ServiceImpl.java";
        TemplateFactory tmplate = new ServiceImplTemplateFactory(targetPackage, modelReference, className);
        tmplate.buildTmplate(filename);
    }

    private String getPath(){
        //获取输出路径
        String objectPath = System.getProperty("user.dir");
        String filePath = objectPath+File.separatorChar+targetProject+File.separatorChar+packageName;
        if('/' == File.separatorChar){
            filePath = filePath.replaceAll("\\\\","/");
        }else{
            filePath = filePath.replaceAll("/","\\\\");
        }
        return filePath;
    }
}
