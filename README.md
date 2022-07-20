# futuremall
# Neural-Style-Transfer For Software Engineering

## 一、项目简介

​		本项目使用python语言编译，结构框架采用PyTorch深度学习框架。给定一张艺术风格图片Figure_style和内容图片Figure_content，我们使用卷积神经网络将图像Figure_content的内容与另一个图像Figure_style的风格相结合，生成具备Figure_style风格和Figure_content内容的图片Figure_mix。

## 二、项目示例

下面是一个风格迁移的实例，将梵高《星夜》的艺术风格映射到我们平时生活中的图片内容中。

​	1、风格图片《星夜》Figure_style:

<div align=center> <img src="https://images.gitee.com/uploads/images/2021/0528/094554_79ca8c94_9113326.jpeg" width="500px" />
</div>

​	2、平时生活中随机拍到的一张内容图片Figure_content：

<div align=center> <img src="https://images.gitee.com/uploads/images/2021/0527/212545_d9737bd9_9113326.jpeg" width="500px" height="650" />
</div>

​	3、经过风格迁移后生成的带有《星夜》风格的生活图片Figure_mix：

<div align=center> <img src="https://images.gitee.com/uploads/images/2021/0527/212624_4b4cd744_9113326.png" width="500px" height="650" />
</div>



## 三、其他测试

​	下面展示一些不同参数设置的风格迁移结果(Test)：

​		Figure_style 

<center class="half">    
    <img src="https://gitee.com/give-it-to-me/softwareEngineering/raw/master/examples/inputs/frida_kahlo.jpg" width="300" height="400"/>    
    <img src="https://gitee.com/give-it-to-me/softwareEngineering/raw/master/examples/inputs/picasso_selfport1907.jpg" width="300" height="400"/> 
    <img src="https://gitee.com/give-it-to-me/softwareEngineering/raw/master/examples/inputs/escher_sphere.jpg" width="300" height="400"/> 
</center>




Figure_content：

<center class="half">    
    <img src="https://gitee.com/give-it-to-me/softwareEngineering/raw/master/examples/inputs/input_fch.jpg" width="300" height="400"/>    
    <img src="https://gitee.com/give-it-to-me/softwareEngineering/raw/master/examples/inputs/input_wj.jpg" width="300" height="400"/> 
    <img src="https://gitee.com/give-it-to-me/softwareEngineering/raw/master/examples/inputs/input_jh.jpg" width="300" height="400"/> 
</center>


​	Figure_mix：

<center class="half">    
    <img src="https://gitee.com/give-it-to-me/softwareEngineering/raw/master/output_fch.png" width="300" height="400"/>    
    <img src="https://gitee.com/give-it-to-me/softwareEngineering/raw/master/output_wj.png" width="300" height="400"/> 
    <img src="https://gitee.com/give-it-to-me/softwareEngineering/raw/master/output_jh.png" width="300" height="400"/> 
</center>





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
