package com.mall.mbg;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Generator {
    public static void main(String[] args) throws IOException, XMLParserException,
            InvalidConfigurationException, SQLException, InterruptedException {
        List<String> warnings = new ArrayList<>();
        /*
          以 / 开头从classpath 根目查找绝对路径忽略当前类的包路径。不以 / 开头 从当前类所在包路径开始查找（相对路径），自动将包名中的 . 替换为 /
         */
        InputStream inputStream =  Generator.class.getResourceAsStream("/generatorConfig.xml");
        ConfigurationParser parser = new ConfigurationParser(warnings);
        //解析.xml文件生成配置类实例
        Configuration configuration = parser.parseConfiguration(inputStream);

        DefaultShellCallback shellCallback = new DefaultShellCallback(true);
        MyBatisGenerator generator = new MyBatisGenerator(configuration,shellCallback,warnings);
        generator.generate(null);

        if (inputStream != null) {
            inputStream.close();
        }

        for (String warning : warnings) {
            System.out.println(warning);
        }
    }
}
