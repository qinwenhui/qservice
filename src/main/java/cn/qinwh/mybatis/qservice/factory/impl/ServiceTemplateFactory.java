package cn.qinwh.mybatis.qservice.factory.impl;


import cn.qinwh.mybatis.qservice.common.BaseService;
import cn.qinwh.mybatis.qservice.factory.TemplateFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @program: qbook
 * @description: Service文件生成
 * @author: qinwh
 * @create: 2020-11-20 12:45
 **/
public class ServiceTemplateFactory implements TemplateFactory {

    private String packaged;
    private String modelPackage;
    private String model;

    public ServiceTemplateFactory(String packaged, String modelPackage, String model){
        this.packaged = packaged;
        this.modelPackage = modelPackage;
        this.model = model;
    }

    @Override
    public File buildTmplate(String targetFileName){
        File file = new File(targetFileName);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            String tmplateContent = load();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(tmplateContent.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("生成service错误!"+e.getMessage());
        }
        return file;
    }


    /**
     * 将参数装载到模板中 返回装载后的字符串
     */
    private String load() {
        StringBuffer sb = new StringBuffer();
        sb.append("package "+ packaged + ";");
        sb.append("\n");
        sb.append("\n");
        sb.append("import "+ modelPackage + ";");
        sb.append("\n");
        sb.append("import "+ BaseService.class.getName() + ";");
        sb.append("\n");
        sb.append("\n");
        sb.append("public interface "+ model + "Service extends BaseService<"+ model +"> {");
        sb.append("\n");
        sb.append("}");
        return sb.toString();
    }
}
