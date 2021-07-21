# 一、使用HTTP Client测试接口
## 步骤
### 1、新建package:http

### 2、新建test.http文件
    GET http://localhost/hello
    Accept:application/json

## 注意点
### 一个http文件可以测试多个


# 二、配置文件介绍
## 1、默认支持的配置文件
* 支持application.properties
* 支持application.yaml
* 支持resources下的config文件
* 单个springboot项目不支持boostrap配置文件,springcloud架构下的springboot支持

## 2、自定义配置项
    @Value("${test.hello}")
    private String test;

# 三、热部署
## 1、添加依赖
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
    </dependency>
## 2.settings-->compiler-->build project automatically

## 3.双击shift-->registry-->compiler.automake.allow.when.app.running

## 4.绿色小锤子戳一下

# 四、Mybatis 代码生成器
## 1步骤
* 添加pom依赖
  
        <!-- mybatis generator 自动生成代码插件 -->
        <plugin>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-maven-plugin</artifactId>
            <version>1.3.7</version>
            <configuration>
                <configurationFile>src/main/resources/generator/generator-config.xml</configurationFile>
                <overwrite>true</overwrite>
                <verbose>true</verbose>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql-connector-java</artifactId>
                    <version>5.1.47</version>
                </dependency>
            </dependencies>
        </plugin>
* 添加配置文件 generator-config.xml
  
        <?xml version="1.0" encoding="UTF-8"?>
        <!DOCTYPE generatorConfiguration
                PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
                "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
        
        <generatorConfiguration>
        <context id="Mysql" targetRuntime="MyBatis3" defaultModelType="flat">
        <!-- 自动检查关键字，为关键字增加反引号 -->
        <property name="autoDelimitKeywords" value="true"/>
        <property name="beginningDelimiter" value="`"/>
        <property name="endingDelimiter" value="`"/>

        <!--覆盖生成XML文件-->
        <plugin type="org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin" />
        <!-- 生成的实体类添加toString()方法 -->
        <plugin type="org.mybatis.generator.plugins.ToStringPlugin"/>

        <!-- 不生成注释 -->
        <commentGenerator>
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>

        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://rm-bp13w2tqj2kymz02rdo.mysql.rds.aliyuncs.com/wiki?characterEncoding=UTF8&amp;autoReconnect=true&amp;serverTimezone=Asia/Shanghai&amp;allowMultiQueries=true"
                        userId="sys_role"
                        password="Jrt011022!">
        </jdbcConnection>

        <!-- domain类的位置 -->
        <javaModelGenerator targetProject="src\main\java"
                            targetPackage="com.futureport.wiki.entity"/>

        <!-- mapper xml的位置 -->
        <sqlMapGenerator targetProject="src\main\resources"
                         targetPackage="mapper"/>

        <!-- mapper类的位置 -->
        <javaClientGenerator targetProject="src\main\java"
                             targetPackage="com.futureport.wiki.mapper"
                             type="XMLMAPPER"/>

        <!--<table tableName="demo" domainObjectName="Demo"/>-->
        <!--<table tableName="ebook"/>-->
        <!--<table tableName="category"/>-->
        <!--<table tableName="doc"/>-->
        <!--<table tableName="content"/>-->
        <!--<table tableName="user"/>-->
        <!--        <table tableName="ebook_snapshot"/>-->
        <table tableName="test"></table>
    </context>
    </generatorConfiguration>

* 添加maven,命名为mybatis-generator
    edit configurations-->maven-->mybatis-generator:generate -e

* 执行mybatis-generator

# 五、封装请求参数和返回参数
## 1、封装请求对象Req,选择需要的请求信息,例如Ebook的部分信息(id,name)
## 2、封装响应对象Resp,选择需要返回给前台的字段信息。
## 3、根据请求的属性信息,模糊查询，获得List<Ebook>对象,并将Ebook转成EbookResp对象,可以实现隐藏用户的密码信息等功能
    public List<EbookResp> findAll(EbookReq req){
        EbookExample example = new EbookExample();
        EbookExample.Criteria criteria = example.createCriteria();
        criteria.andNameLike("%"+req.getName()+"%");
        List<Ebook> list = ebookMapper.selectByExample(example);

        List<EbookResp> lists = new LinkedList<>();
        /**
         * 遍历Ebook集合，转成EbookResp
         */
        for (Ebook ebook : list) {
            EbookResp ebookResp = new EbookResp();
            BeanUtils.copyProperties(ebook,ebookResp);
            lists.add(ebookResp);
        }
        return lists;
    }

## 4、编写controller层代码,controller层中不出现Ebook等对象
      @RequestMapping("/list")
    public CommonResp ebook(EbookReq ebookReq){
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        List<EbookResp> lists =  ebookService.findAll(ebookReq);
        resp.setContent(lists);
        return resp;
    }
