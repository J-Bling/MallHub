package com.mall.mbg;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Properties;

public class CommentGenerator extends DefaultCommentGenerator {
    private boolean addRemarkComments;
    private static final String EXAMPLE_SUFFIX="Example";
    private static final String MAPPER_SUFFIX="Mapper";
    private static final String API_MODEL_PROPERTY_FULL_CLASS_NAME="io.swagger.annotations.ApiModelProperty";


    @Override
    public void addConfigurationProperties(Properties props){
        super.addConfigurationProperties(props);
        this.addRemarkComments= StringUtility.isTrue(props.getProperty("addRemarkComments"));
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn){
        String remarks = introspectedColumn.getRemarks();
        if(this.addRemarkComments && remarks!=null && !remarks.isEmpty()){
            if(remarks.contains("\"")){
                remarks = remarks.replace("\"","'");
            }
//            field.addJavaDocLine("@ApiModelProperty(value = \""+remarks+"\")");
        }
    }

    private void addFieldJavaDoc(Field field, String remarks) {
        //文档注释开始
        field.addJavaDocLine("/**");
        //获取数据库字段的备注信息
        String[] remarkLines = remarks.split(System.lineSeparator());
        for(String remarkLine:remarkLines){
            field.addJavaDocLine(" * "+remarkLine);
        }
        addJavadocTag(field, false);
        field.addJavaDocLine(" */");
    }

//    @Override
//    public void addJavaFileComment(CompilationUnit compilationUnit){
//        super.addJavaFileComment(compilationUnit);
//        String fullyQualifiedName = compilationUnit.getType().getFullyQualifiedName();
//        if (!fullyQualifiedName.contains(MAPPER_SUFFIX) && !fullyQualifiedName.contains(EXAMPLE_SUFFIX)){
//            compilationUnit.addImportedType(new FullyQualifiedJavaType(API_MODEL_PROPERTY_FULL_CLASS_NAME));
//        }
//    }
}
