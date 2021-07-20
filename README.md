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