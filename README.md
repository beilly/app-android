##vcreditapp
======

注意:

1、没有特殊说明，所有的目录都是相对项目的根目录。

###版本更新

1、当app迭代新的版本的时候，修改build.gradle的ext节点下面:

- versionCode(版本号);
- versionName(版本名称);
- appName(app名称)
- projectName(项目打包名称)
- applicationId(appid)

###多渠道打包

1、在config/markets.txt中添加对应的渠道编号

2、执行下面命令
```
apkrelease.sh
```
