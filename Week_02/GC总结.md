#### 环境说明
jvm.gc.GCLogAnalysis 只默认跑一秒钟
#### 串行GC(SerialGC)
SerialGC 组合是：Serial + Serial Old组合
vm启动参数：-XX:+UseSerialGC
##### 串行gc分析
###### 设置128m内存
```
java -Xms128m -Xmx128m -XX:+UseSerialGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps jvm.gc.GCLogAnalysis

//最终堆空间
Heap
 def new generation   total 39296K, used 39025K [0x00000007b8000000, 0x00000007baaa0000, 0x00000007baaa0000)
  eden space 34944K, 100% used [0x00000007b8000000, 0x00000007ba220000, 0x00000007ba220000)
  from space 4352K,  93% used [0x00000007ba660000, 0x00000007baa5c5c0, 0x00000007baaa0000)
  to   space 4352K,   0% used [0x00000007ba220000, 0x00000007ba220000, 0x00000007ba660000)
 tenured generation   total 87424K, used 87045K [0x00000007baaa0000, 0x00000007c0000000, 0x00000007c0000000)
   the space 87424K,  99% used [0x00000007baaa0000, 0x00000007bffa1528, 0x00000007bffa1600, 0x00000007c0000000)
 Metaspace       used 2734K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 298K, capacity 386K, committed 512K, reserved 1048576K
```
程序总功运行了300ms,gc的次数不少30次，gc的原因都是Allocation Failure，最后程序oom.
gc出现以下几个阶段：
1. DefNew新生代回收,每次gc发现整个堆的空间都在变大
2. DefNew、Tenured、Metaspace,这三个区同时回收，DefNew、Tenured都没可回收的数据
3. Full GC，只回收Tenured、Metaspace两个区，基本上没有课回收的

###### 设置256m内存
共生成对象次数:4258
最终堆空间
```
Heap
 def new generation   total 78656K, used 78209K [0x00000007b0000000, 0x00000007b5550000, 0x00000007b5550000)
  eden space 69952K, 100% used [0x00000007b0000000, 0x00000007b4450000, 0x00000007b4450000)
  from space 8704K,  94% used [0x00000007b4cd0000, 0x00000007b54e07a8, 0x00000007b5550000)
  to   space 8704K,   0% used [0x00000007b4450000, 0x00000007b4450000, 0x00000007b4cd0000)
 tenured generation   total 174784K, used 174669K [0x00000007b5550000, 0x00000007c0000000, 0x00000007c0000000)
   the space 174784K,  99% used [0x00000007b5550000, 0x00000007bffe36c8, 0x00000007bffe3800, 0x00000007c0000000)
 Metaspace       used 2710K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 295K, capacity 386K, committed 512K, reserved 1048576K
```
通过查看gc日志，也很平凡,次数和128m是差不多，但是这次没有oom，也是上边三个阶段。说明256m也就刚刚够。

###### 设置512m内存
共生成对象次数:6243
最终堆空间
```
Heap
 def new generation   total 157248K, used 131264K [0x00000007a0000000, 0x00000007aaaa0000, 0x00000007aaaa0000)
  eden space 139776K,  93% used [0x00000007a0000000, 0x00000007a80302b8, 0x00000007a8880000)
  from space 17472K,   0% used [0x00000007a9990000, 0x00000007a9990000, 0x00000007aaaa0000)
  to   space 17472K,   0% used [0x00000007a8880000, 0x00000007a8880000, 0x00000007a9990000)
 tenured generation   total 349568K, used 310174K [0x00000007aaaa0000, 0x00000007c0000000, 0x00000007c0000000)
   the space 349568K,  88% used [0x00000007aaaa0000, 0x00000007bd987b30, 0x00000007bd987c00, 0x00000007c0000000)
 Metaspace       used 2710K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 295K, capacity 386K, committed 512K, reserved 1048576K
```
这次gc次数少了很多，只有前面两个阶段的gc,没有FullGc.但是有一个问题，gc的平均时间要比以前大。最后对空间其实也使用了剩下的也不多了。

###### 设置1g内存
共生成对象次数:6967
随着后边内存的不断增加，只会产生新生代的回收，创建对象也不会随着增加，反而gc的平均耗时在不断的变大。1g->70ms;2g->110s

##### 串行GC总结
1.当分配的内存空间比较小的时候，虽然每次的gc比较短，但是gc很频繁，容易造成程序崩溃.
2.分配过大的内存，造成gc的时间边变长，不会明显提高吞吐量。
3.分配过大的内存，也同时导致延迟变大，容易导致耗时的毛刺。
4.gc是单线程的

#### 并行GC(ParallelGC)
SerialGC 组合是：Parallel Scavenge + Parallel Old组合
vm启动参数：-XX:+UseParallelGC -XX:ParallelGCThreads=m -XX:ConcGCThreads=n
也是java8的默认gc
##### 并行gc分析
###### 设置128m内存
和串行gc一样，gc和频繁，会发生oom。但是gc平均时间都在10ms一下，比串行快。
gc的阶段：
1.PSYoungGen，原因是Allocation Failure
2.Full GC，PSYoungGen，ParOldGen，Metaspace三个区的回收，原因是Ergonomics

###### 设置256m内存
这个内存还是比较小，偶尔也还会有oom的发生，gc也是比较频繁，

###### 设置512m内存
共生成对象次数:7653
会有几次的Full GC, gc其实也挺平凡的

###### 设置1g内存
共生成对象次数:9481
吞吐量提高了，耗时提高也不是很明显，但是偶尔还会出现一次Full GC
###### 设置2g内存
共生成对象次数:9841
吞吐量没有多少提高，也没有FUll GC，gc平局耗时也60ms左右，通过设置gc线程数稍微能提高一点

##### 并行GC 总结
1. 和串行一样，随着内存分配的越大，gc的时间都会变长
2. 总体来说平行的gc比串行的gc还是耗时端，吞吐量也比串行大，2g内存平均gc耗时也在60ms

#### CMS GC
组合是：ParNew + CMS组合
vm启动参数：-XX:+UseConcMarkSweepGC -XX:ParallelGCThreads=m -XX:ConcGCThreads=n

###### 设置128m内存
同样oom, gc的阶段：
1.ParNew，
2. FULL GC,但是一个几个阶段不一定每次都完整执行的
a. CMS Initial Mark
b. CMS-concurrent-mark-start
c. CMS-concurrent-mark
d. CMS-concurrent-preclean-start
e. CMS-concurrent-preclean
f. CMS-concurrent-abortable-preclean-start
g. CMS-concurrent-abortable-preclean
h. CMS Final Remark
i. CMS-concurrent-sweep-start
j. CMS-concurrent-sweep
k. CMS-concurrent-reset-start
l. CMS-concurrent-reset

##### CMS 总结
1. 随着内存的不断增大，gc的数量之前的gc也是一样的，gc时间都会变大。Full GC越来越少
2. CMS GC的gc操作增加了很多阶段，和Parallel c比较，同样的内存下，CMS gc的次数比Parallel的多，但是耗时要稍微断点。
3. 由于gc的次数比Parallel多，耗时没有明显减少的，所以导致吞吐反而有点下降

#### G1 GC
组合是：ParNew + CMS组合
vm启动参数：-XX:+UseG1GC 

###### 设置128m内存
gc阶段：
1. G1 Evacuation Pause（young）
2. G1 Humongous Allocation(young)(initial-mark)
a. concurrent-root-region-scan-start
b. concurrent-root-region-scan-end
c. concurrent-mark-start
d. concurrent-mark-end
e. remark
f. cleanup
3. Evacuation Pause(mixed)
4. Full GC (Allocation Failure)
当最后会退化到第四部Full GC,是Serial GC

##### G1 总结
1. G1内存分配到2g的时候，平均耗时也都在20ms，但是吞吐量还是没有Parallel高，但是减少的不是很明显。


#### 总结
##### 内存分配
1. 如果分配的空间过小，容易造成oom,容易引起频繁的gc，大部分时间用来gc,吞吐也不高。
2. 如果分配的空间过大，造成gc的平均耗时会增加，导致延迟增高，G1收集器耗时不会明显增加。
3. 所以要经过压测，测试出一个合理的内存值，可以在单位时间内gc不要太频繁，偶尔可以有少量的Full GC。

##### 收集器的选择
1. 单核的机器，不用说肯定选择Serial GC
2. 多核机器，需要内存大，延迟低，建议使用G1(推荐)
3. 多核机器，内存小，要求吞吐高，建议使用parallel GC
4. 多核机器，延迟和吞吐找平衡点的话，建议用CMS











