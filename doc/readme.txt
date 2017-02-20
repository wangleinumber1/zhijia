注意事项：

1、关于地图的key
编译Android的时候请一定要用doc目录下的debug.keystore，否则会导致地图api报错。
也可以参考百度地图开发者文档，自己在百度开发者上申请自己的地图key，替换AndroidManifest.xml中的
<meta-data android:name="com.baidu.lbsapi.API_KEY" android:value="LDr2qo4K1vgSbY6AckAd2ZjR"/>
如果出现网格就更新一下key密钥
在AndroidManifest.xml里把value换一下就可以了
<meta-data
   android:name="com.baidu.lbsapi.API_KEY"
   android:value="TsXizVcR3GY6KmSNeG3eXIUC" />
2、发布APK的时候，请一定要用混淆，否则编译出来的APK可以被轻易的反编译，容易导致数据安全问题。具体如何混淆请参考百度。

3、代码结构非常清晰，原始代码是在Idea下开发的，Eclipse下也没有问题，记得导入lib目录使用的jar。
   com.zhijia下分3个包：
    service目录存放的是与后台通信的数据模型，以及相关的工具类；
    ui是APK的整体Activity，按照模块划分子目录；
    util是工具类。
    Global里放的是全局信息。

4、zhijia.keystore是发布用的签名，所有密码和用户都是zhijia。