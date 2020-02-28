

[![Build Status](https://github.com/JantHsueh/EmojiRain/workflows/RELEASE_CI/badge.svg?branch=androidx)](https://github.com/JantHsueh/EmojiRain/workflows/RELEASE_CI/badge.svg?branch=androidxn)
[![Download](https://api.bintray.com/packages/jantxue/maven/Emoji-Rain/images/download.svg) ](https://bintray.com/jantxue/maven/Emoji-Rain/_latestVersion)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0)

# 表情雨 ViewGroup

微信聊天中的表情雨效果一样。内部使用了资源缓存复用机制，在无限循环中，不会出现占用内存增长的问题，所以不会GC（除非），更流畅

先来看下效果，太阳雨

![](/gif/rain_sun.gif)

# 使用教程


在项目的build.gradle 添加引用

```
    implementation 'me.xuexuan:emoji-rain:1.0.0'
	//为了减少版本冲突问题，库中没有引用constraintlayout，所以在自己的项目添加引用
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
```



例如：在布局文件中使用

```xml
<me.xuexuan.RainViewGroup
    android:id="@+id/testView"
    android:layout_width="match_parent"
    android:layout_height="match_parent">


</me.xuexuan.RainViewGroup>
```
它是一个viewGroup，继承ConstraintLayout。所以可以把它当做ConstraintLayout来使用



## 使用

获取到RainViewGroup的对象实例（例如：在java代码中，通过findViewById 获取）

调用start()，开启表情雨
```java
    rainViewGroup = findViewById(R.id.testView);
    rainViewGroup.start();
```
调用stop()，关闭表情雨
```java
    rainViewGroup.stop();
```



## 高级使用

项目提供了一些函数来实现自定义的效果

### 设置表情雨资源

`setImgResId(mImgResId: Int)`

### 设置表情缩放比例

`setScale(scaleRandom: Int, scaleOffset: Int)` 默认值是 `setScale(30, 70)`


在代码的实现`var i = （mRandom.nextInt(xxxRandom) + xxxOffset）.toFloat() / 100f`

例如：`setScale(30, 70)` 那么i的取值范围就是[0.7,1)


### 设置屏幕最多显示的表情数量
屏幕最多的表情数量，以下简称单屏数量，可以理解为，表情雨的密度

`setAmount(num: Int = mAmount)` 默认值是 20


### 设置表情雨次数
这里的次数，是指上面设置的表情数量的次数。在保持密度不变的情况下，设置下落数量。


	 
`setTimes(times: Int = INFINITE)` 默认值是 1


例如：
- A:单屏数量（密度） = 50，次数 = 3，总共数量150。
- B:单屏数量（密度） = 50，次数 = 1，总共数量50。
- c:单屏数量（密度） = 150，次数 = 1，总共数量150。



|   | 表情密度  | 表情数量  | 下落时间
| ------------ | ------------ |------------ |------------ |
| A B  | 相同  | A = 3\*B | A = 3\*B
| A C  | C = 3\*A  | 相同 | A = 3\*B
| B C  | C = 3\*B  | C = 3\*B| 相同



### 设置x，y轴偏移方向

设置x/y轴的偏移，每一次onDraw(),在上一次位置的基础上，在x/y轴上增加的偏移量。

```java
    setX(xRandom: Int, xOffset: Int)
    setY(yRandom: Int, yOffset: Int)
```

默认值是`setX(6，-3)` `setY(5，10)`


在代码中的实现`var i = mRandom.nextInt(xxxRandom) + xxxOffset`

例如：`setX(6，-3)`， i的取值范围就是 [-3,3)


### 设置初始的横坐标位置

设置表情初始位置，x值的范围，也就在屏幕宽度指定的范围内落下。避免表情x = 0（x = 屏幕宽度）时，xOffset为负值（正值），落下很小的距离，表情就划出屏幕的问题。

`setWidth(widthRandom: Int, widthOffset: Int)` 默认值是`setWidth(200，100)`

在代码中的实现`var i = mRandom.nextInt(getWidth() - xxxRandom) + xxxOffset`，
例如：屏幕宽度是1080，`setWidth(200，100)`， i的取值范围就是 [980,100)

# LICENSE
````
Copyright 2020 XueXuan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
````







