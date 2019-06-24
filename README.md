### 这可能是解耦程度最好的全局Loading框架
##
### 背景
几乎每一个App都会有一个Loading动画，用来让用户知道程序在运作而不是卡死从而缓解用户焦虑，提升用户体验。    
Loading动画结束后一般会有三种情况：
- 操作成功，loading动画消失，显示正确的页面内容
- 操作失败，loading动画消失，显示失败页面，让用户可以点击重试
- 空页面，虽然请求成功但是没有数据返回，所以此时loading动画消失，显示空页面状态

所以总结起来，一般一个页面有以下四种状态
1. 加载状态
2. 成功状态
3. 失败状态
4. 空状态

对于一个设计良好的APP来说一般全局页面会保持一致，比如加载状态，失败状态和空状态等。但是也有可能不同的页面需要不同的各种状态，比如消息页面展示消息为空的空页面，订单页面展示订单为空的空页面；也可能是不同的页面需要不同的失败状态等

### 传统方案
#### 方案一
1. 在页面的xml中写上各种状态的布局
2. 根据页面展现过程中控制各个布局的显示和隐藏

此方案耦合度极高，难以维护和修改，当App页面默认状态需要更改时代价太大，不利于维护

#### 方案二
1. 自定义一个有各种状态的自定义view，
2. 这个自定义view加入到BaseFragment或BaseActivity页面的xml中
3. 在base类中提供showLoading(),showSuccess(),showFaile(),showEmpty()等方法供子类在合适的时机调用

此方案相较于第一种方案有了很大提升，用户可以对自定义的LoadingView样式进行修改，这样全局就可以得到改变，但是此方案仍然需要在xml中添加代码，存在耦合关系，不利于扩展

### 解耦方案
要解耦就先梳理一下我们需要实现的点：
1. 支持不同样式的加载、成功、失败和空状态，例如，可能有多种状态的加载样式，也可能有多种状态的空状态等
2. 整个Loading的相关东西不写到具体的页面中，利于扩展和维护
3. 支持在各种状态时添加重试逻辑，例如失败状态时点击重试，空状态时点击重新请求数据等
4. 能指定区域进行加载，例如局部刷新等

实现思路
1. 要实现第一点，兼顾不同状态可能有各种不同样式，需要根据不同的状态创建不同的view供我们在展示相应状态时使用
2. 实现第二点需要找到一个合适的时机，将我们创建的不同状态的view根据我们创建时的样式展示出来
3. 在用户需要点击重试时，用户可以加入回调，从而可以实现用户自己的逻辑
4. 要实现第四点需要找到需要加载的区域，然后在此区域上加上我们需要的布局即可

### 终极方案 Loading
Loading是一个轻量级，深度解耦的加载框架，完全由用户控制，只有一个Java文件，你甚至可以直接把这个文件源码拷贝到你的工程中使用。

0. **演示**

Activity|view|空状态|失败状态
:---:|:---:|:---:|:---:
<img src="https://raw.githubusercontent.com/ansnail/tc/master/20190624201852.gif" width="200" />|<img src="https://raw.githubusercontent.com/ansnail/tc/master/20190624204201.gif" width="200" />|<img src="https://raw.githubusercontent.com/ansnail/tc/master/20190624204724.gif" width="200" />|<img src="https://raw.githubusercontent.com/ansnail/tc/master/20190624205744.gif" width="200" />
1. 引入

[ ![Download](https://api.bintray.com/packages/androidman/maven/loading/images/download.svg?version=1.0.0) ](https://bintray.com/androidman/maven/loading/1.0.0/link)

```
compile 'top.androidman.loading:loading:1.0.0'
```
2.创建全局的默认Adapter，在Adapter中根据type创建不同的布局实现
```
public class DefaultLoadingAdapter extends Loading.Adapter {

    private Context mContext;

    public DefaultLoadingAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public View generateView(int viewType) {
        View defaultLoadingView = LayoutInflater.from(mContext).inflate(R.layout.layout_default_loading_view,null);
        ImageView image = defaultLoadingView.findViewById(R.id.image);
        TextView text = defaultLoadingView.findViewById(R.id.text);
        int imageResource = -1;
        String textResource = "";

        defaultLoadingView.setVisibility(View.VISIBLE);
        switch (viewType) {
            case Style.LOADING:
                imageResource = R.drawable.loading;
                textResource = "正在加载。。。";
                break;
            case Style.SUCCESS:
                defaultLoadingView.setVisibility(View.GONE);
                break;
            case Style.EMPTY:
                imageResource = R.drawable.icon_empty;
                textResource = "没有内容哦。。。";
                break;
            case Style.FAILED:
                imageResource = R.drawable.icon_failed;
                textResource = "加载失败。。。";
                break;
            case Style.NO_WIFI:
                imageResource = R.drawable.icon_no_wifi;
                textResource = "网络出错啦，点击重试。。。";
                break;
            default:
                break;
        }
        image.setImageResource(imageResource);
        text.setText(textResource);

        return defaultLoadingView;
    }
}
```
当然完全可以创建更加复杂的布局，然后返回即可，另外如果有多种样式的Loading、成功、失败、空状态也可以全部定义出来然后返回即可。

3. 在Application中进行注册
```
Loading.getIns().setAdapter(new DefaultLoadingAdapter(this));
```
4. 然后你就可以在任何地方开心的进行使用了(根据创建View时type进行展示，如果是想整个activity中进行展示，在target方法中传入activity即可，如果只是想在某个view上进行展示，传入这个view就可以啦)
```
Loading.getIns().target(activity).show(Style.LOADING);
```
或者
```
Loading.getIns().target(view).show(Style.SUCCESS);
```
### 高级用法
每个布局都可能会加一些重试的策略在里面，这个时候只需要调用如下方案即可轻松完成
```
Loading.getIns().wrap(view).show(Style.FAILED, new Loading.OnRetryListener() {
    @Override
    public void retry() {
        picUrl = ImageFactory.getNormalImage();
        loadData();
    }
});
```
其实，不仅是失败时候，任何状态下都可以加入这个重试的回调，都可以执行

### 最后的唠叨
可能小伙伴们都注意到了**Style.LOADING**、**Style.SUCCESS**、**Style.EMPTY**、**Style.FAILED**等状态，其实这个状态是小伙伴自己定义的，就是自己定义不同状态创建不同的View，然后在展示的时候根据不同的情况show不同的type即可，所以建议把Style写到一个集中的地方，方便全局调用，例如：
```
public final class Style {

    public static final int LOADING = 0x1;
    public static final int SUCCESS = 0x2;
    public static final int FAILED = 0x3;
    public static final int EMPTY = 0x4;
    public static final int NO_WIFI = 0x5;

    @IntDef({LOADING, SUCCESS, FAILED, EMPTY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Loading {
    }

}
```

当然也可以直接写成全局常量，只要方便调用即可

github传送门：[点这里](https://github.com/ansnail/Loading)

### 鸣谢
此方案思路来自于 https://github.com/luckybilly/Gloading ，对原作者表示感谢