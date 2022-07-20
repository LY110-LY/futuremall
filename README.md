# futuremall
# Neural-Style-Transfer For Software Engineering

## 一、项目简介

>本项目是一套电商系统。基于Spring + SpringBoot + Mybatis-Plus + SpringCloud + SpringCloudAlibaba实现，采用Docker容器部署，搜索引擎使用Elasticsearch。<br>前台商城系统包括：用户登录、注册、商品搜索、商品详情、购物车、下订单流程等模块。后台管理系统包括：系统管理、商品系统、优惠营销、库存系统、订单系统、用户系统、内容管理等七大模块。


## 二、项目演示

后端登录界面部分演示

    1、登录界面

<div align=center> <img src="登录界面.png" width="500px" />
</div>

    2、三级分类

<div align=center> <img src="三级分类.png" width="500px" hight="300"/>
</div>

    3、菜单管理

<div align=center> <img src="菜单管理.png" width="500px" hight="300px"/>
</div>

    4、仓库维护

<div align=center> <img src="仓库维护.png" width="500px" height="600px"/>
</div>

    5、销售属性

<div align=center> <img src="销售属性.png" width="500px" hight="300px"/>
</div>

    6、仓库维护

<div align=center> <img src="仓库维护.png" width="500px" hight="300px"/>
</div>


## 三、项目结构

futuremall<br>
├── futuremall-common -- 工具类及通用代码<br>
├── futuremall-coupon -- 优惠卷服务<br>
├── futuremall-gateway -- 统一配置网关<br>
├── futuremall-order -- 订单服务<br>
├── futuremall-product -- 商品服务<br>
├── futuremall-search -- 检索服务<br>
├── futuremall-third-party -- 第三方服务<br>
├── futuremall-ware -- 仓储服务<br>
├── futuremall-member -- 会员服务<br>
└── renren-generator -- 人人开源项目的代码生成器





## 四、快速开始：

**基本用法**：

```
python neural_style.py -style_image <image.jpg> -content_image <image.jpg>
```

**cuDNN在NIN模型中的用法**：

```
python neural_style.py -style_image examples/inputs/picasso_selfport1907.jpg -content_image examples/inputs/brad_pitt.jpg -output_image profile.png -model_file models/nin_imagenet.pth -gpu 0 -backend cudnn -num_iterations 1000 -seed 123 -content_layers relu0,relu3,relu7,relu12 -style_layers relu0,relu3,relu7,relu12 -content_weight 10 -style_weight 500 -image_size 512 -optimizer adam
```

**1、下载预训练的vgg网络，并放入到项目的根目录中**

​	vgg-19下载地址：https://web.eecs.umich.edu/~justincj/models/vgg19-d01eb7cb.pth

​	vgg-16下载地址：https://web.eecs.umich.edu/~justincj/models/vgg16-00b39a1b.pth

​	NIN下载地址：https://raw.githubusercontent.com/ProGamerGov/pytorch-nin/master/nin_imagenet.pth

**2、选定风格图片和内容图片，放入项目根目录下的examples文件夹中**

​	a.在项目根目录下的examples文件夹中，有很多张图片，即原始的内容图片。

​	b.如果只是使用默认图片测试模型，这里可以不做任何操作。

​	c.如果要测试自定义的图片，请使用自定义的内容图片和/或风格图片替换该目录下的内容图片和/或风格图片，请保持命名与默认一致，或者在settings.py中修改路径及名称。

**3、开始生成图片**

​	a.运行项目中的page.py文件，进行训练。在训练过程中，程序会定期提示进度，并保存过程图片。

​	b.当训练结束后，保存最终生成图片。

​	c.所有生成的图片均直接保存在项目根目录下中。

## 五、更多设置

​	在settings.py文件中存在多种配置项，可根据需求进行配置。

- Dependencies:

  PyTorch

- Optional dependencies:

  For CUDA backend:

  - CUDA 7.5 or above

  For cuDNN backend:

  - cuDNN v6 or above
