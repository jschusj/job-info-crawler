# Selenium拉勾网爬虫

## 启动

1. 运行sql文件job.sql (在data目录下,截止2019.3.28有1671条杭州招聘信息，目前只采集杭州招聘信息)
2. 修改application.properties中的mysql连接(用户名和密码，默认用户名:root 密码:password)
3. 修改application.properties中的chromedriver.path值，对应本机上的chromedriver.exe路径
4. 启动WebApplication
5. 微信扫码登录拉钩网(暂时未做自动登录功能)
6. 开始采集

## 备注

- chromedriver.exe和job.sql在data目录下

# 作者

## 联系方式

- 手机：18268100534
- Email：18268100534@163.com
- 微信：18268100534