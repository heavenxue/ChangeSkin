
ChangeSkin
==========
  一种完全无侵入的换肤方式，支持插件式和应用内，无需重启Activity.</br>
  
效果图
---
![github](https://github.com/heavenxue/ChangeSkin/raw/master/doc/1.png "github")
![github](https://github.com/heavenxue/ChangeSkin/raw/master/doc/2.png "github")
![github](https://github.com/heavenxue/ChangeSkin/raw/master/doc/3.png "github")

特点
---
  * 插件式换肤
  * 应用内换肤
  * 支持插件或者应用内多套皮肤
  * 支持动态生成addView
  * 无需重启Activity
  
### 注意
  本demo是将自己生成的应用skin_plugin-debug.apk放在sd卡的根目录上进行测试的\<h3\><br />

### 说明

    tag属性分为3部分组成：

    skin
    资源的名称，即插件包中资源的名称，需要与当前app内使用的资源名称一致。
    支持的属性，目前支持src,background,textColor,支持扩展。
    3部分，必须以:分隔拼接。

    对于一个View多个属性需要换肤的，android:tag="skin:item_text_color:textColor|skin:icon:src" 同样使用|进行分隔。

    简言之：如果你哪个View需要换肤，就添加tag属性，tag值按照上述方式设置即可。
    
### 使用

    插件式：
    SkinManager.getInstance().changeSkin(mSkinPkgPath, "com.lixue.aibei.skin_plugin", new ISkinChangingCallback() {
                        @Override
                        public void onStart() {
                        }
    
                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(getActivity(), "换肤失败", Toast.LENGTH_SHORT).show();
                        }
    
                        @Override
                        public void onComplete() {
                            Toast.makeText(getActivity(), "换肤成功", Toast.LENGTH_SHORT).show();
                        }
                    });
    应用内：
    SkinManager.getInstance().changeSkin(suffix);
    应用内多套皮肤以后缀就行区别，比如：main_bg，皮肤资源可以为：main_bg_red,main_bg_green等。
    
    换肤时，直接传入后缀，例如上面描述的red,green。
    