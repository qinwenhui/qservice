package cn.qinwh.mybatis.qservice.factory.impl;

import cn.qinwh.mybatis.qservice.common.BaseServiceImpl;
import cn.qinwh.mybatis.qservice.factory.TemplateFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @program: qservice
 * @description: serviceImpl生成
 * @author: qinwh
 * @create: 2020-11-20 13:56
 **/
public class ServiceImplTemplateFactory implements TemplateFactory {

    private String packaged;
    private String modelPackage;
    private String model;
    private String modelServicePackage;

    public ServiceImplTemplateFactory(String packaged, String modelPackage, String model) {
        this.packaged = packaged;
        this.modelPackage = modelPackage;
        this.model = model;
        this.modelServicePackage = packaged + "." + model + "Service";
    }

    @Override
    public File buildTmplate(String targetFileName) {
        File file = new File(targetFileName);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            String tmplateContent = load();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(tmplateContent.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("生成serviceImpl错误!"+e.getMessage());
        }
        return file;
    }

    /**
     * 将参数装载到模板中 返回装载后的字符串
     */
    private String load() {
        StringBuffer sb = new StringBuffer();
        sb.append("package "+ packaged + ".impl;");
        sb.append("\n");
        sb.append("\n");
        sb.append("import "+ modelPackage + ";");
        sb.append("\n");
        sb.append("import "+ modelServicePackage + ";");
        sb.append("\n");
        sb.append("import "+ BaseServiceImpl.class.getName() + ";");
        sb.append("\n");
        sb.append("import org.springframework.stereotype.Service;");
        sb.append("\n");
        sb.append("import org.springframework.transaction.annotation.Transactional;");
        sb.append("\n");
        sb.append("\n");
        sb.append("@Service");
        sb.append("\n");
        sb.append("@Transactional(rollbackFor = {RuntimeException.class, Exception.class})");
        sb.append("\n");
        sb.append("public class "+ model + "ServiceImpl extends BaseServiceImpl<"+ model +"> implements "+ model +"Service {");
        sb.append("\n");
        sb.append("}");
        return sb.toString();
    }
}
