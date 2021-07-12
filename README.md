# VideoDemo
视频详情模块  在根目录build.gradle中加入  allprojects {  repositories {  ...  maven { url 'https://jitpack.io' }  }  }    在app模块下build.gradle添加依赖  dependencies {  implementation 'com.github.aaa31210aaa.VideoDemo:superplayerkit:1.0.0'  }

需要在自己的application里面初始化
OkGoUtils.initOkGo(this);
Utils.init(this,true); //后面的boolean参数代表是否是debug环境

