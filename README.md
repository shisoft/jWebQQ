jWebQQ
======

A Web QQ Library based on iqq's work (http://code.google.com/p/iqq/). Redesigned to capable of using as a library.

To use is in your own project,add jWebQQ to your Java SE or EE project, follow the sample in 'iqq.demo' package to get started.

关于修改的部分：
这个类库主要由 364530827 提取逻辑代码，shisoft 重构成类库。原作者几乎为所有的共享变量和函数都设定为静态，在重构的过程中已经修改为成员变量和函数。同时去除了使用sun内部库的图像处理函数采用 ImageIO 代替。所有信息不再使用文件进行缓存，而将直接保存在内存中。联系人头像目前不加载，下次更新。新添加删除联系人函数，添加和查找联系人下次更新。