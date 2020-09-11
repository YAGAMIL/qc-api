#兑吧——服务调研

###1 主要原理

***
兑吧主要服务为H5服务，需要通过小程序或app嵌套展示，由我方需求决定是否仅对登陆用户展示，
或可展示给未登录用户以吸引用户注册使用。
***

###2 接入方式

***
兑吧服务主要又三种接入方式，分别是微信接入，客户端接入，PC端接入
 1. 客户端：
    +  H5接入：纯H5页面接入，因此需要调用App中的WebView来加载积分商城页面
 2. 微信端：
    + 公众号： 
        1. 微信授权的对接方式，采用微信openID为用户体系，由兑吧服务器管理用户及用户积分，适用于无开发资源且没有自己的积分用户体系的公众号开发者。可联系兑吧技术支持开通
        2. 常规对接，对接免登和积分接口，适用于有自己服务器且有自己的用户及积分体系的公众号开发者
    
    + 小程序接入：
        1. 通过小程序的< web-view />组件来加载。
        2. 需要添加业务域名。
  3. PC端：
      + 由数据接口和商品页面两部分组成，支持基本的商品积分兑换功能。需我方开发商品列表页，兑吧提供数据接口查询上架的商品信息，如商品名，商品页面地址等，并且不支持活动、秒杀和加钱购商品。

###3 交互流程
1. 接口交互流程
![接口调用流程](https://upload-images.jianshu.io/upload_images/19159596-c6a0783ca8d6080e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

2. 完整交互流程
  +  登录状态：
![完整交互流程-登陆状态](https://upload-images.jianshu.io/upload_images/19159596-f67e543ff466d629.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ 游客状态
![完整交互流程-游客状态](https://upload-images.jianshu.io/upload_images/19159596-ce8198229460a694.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

###4 兑换流程
1. 普通商品兑换流程

![普通商品兑换流程](https://upload-images.jianshu.io/upload_images/19159596-cda614332792e18e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
2. 虚拟商品兑换流程

![虚拟商品兑换流程](https://upload-images.jianshu.io/upload_images/19159596-d2177978fb0c2825.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

###示例
![免登录地址生成](https://upload-images.jianshu.io/upload_images/19159596-362221db54315b34.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



