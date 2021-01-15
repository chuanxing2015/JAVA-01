#### 本机环境
1. 内存：8g 
2. 物理cpu:2
3. 逻辑cpu:4
4. jdk:1.8.0_131

#### 默认情况
1. 无参数启动
```
java -jar gateway-server-0.0.1-SNAPSHOT.jar
```
2. jps查看进程号
```
zhangchuan@ gc$ jps
2526 Jps
2079 jar
```
3. 通过jstat实时查看内存信息
```
zhangchuan@ gc$ jstat -gcutil 2079 1000 1000
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT   
  0.00   0.00   1.72  25.52  95.31  91.77      9    0.081     2    0.106    0.187
  0.00   0.00   1.72  25.52  95.31  91.77      9    0.081     2    0.106    0.187
  0.00   0.00   1.72  25.52  95.31  91.77      9    0.081     2    0.106    0.187
  0.00   0.00   1.72  25.52  95.31  91.77      9    0.081     2    0.106    0.187
  
  
   S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT   
13312.0 11264.0  0.0    0.0   233984.0  4020.1   69120.0    17640.9   35456.0 33794.1 4736.0 4346.4      9    0.081   2      0.106    0.187
13312.0 11264.0  0.0    0.0   233984.0  4020.1   69120.0    17640.9   35456.0 33794.1 4736.0 4346.4      9    0.081   2      0.106    0.187
13312.0 11264.0  0.0    0.0   233984.0  4020.1   69120.0    17640.9   35456.0 33794.1 4736.0 4346.4      9    0.081   2      0.106    0.187
13312.0 11264.0  0.0    0.0   233984.0  4020.1   69120.0    17640.9   35456.0 33794.1 4736.0 4346.4      9    0.081   2      0.106    0.187
13312.0 11264.0  0.0    0.0   233984.0  4020.1   69120.0    17640.9   35456.0 33794.1 4736.0 4346.4      9    0.081   2      0.106    0.187
```
4. 通过jinfo查看内存分配的信息
```
VM Flags:
Non-default VM flags: 
-XX:CICompilerCount=3 //编译线程数
-XX:InitialHeapSize=134217728 //初始化堆大小128M
-XX:MaxHeapSize=2147483648 //最大堆大小2g
-XX:MaxNewSize=715653120   //最大新生代大小682.5M
-XX:MinHeapDeltaBytes=524288 
-XX:NewSize=44564480      //62.5M
-XX:OldSize=89653248      //85.5M
-XX:+UseCompressedClassPointers 
-XX:+UseCompressedOops 
-XX:+UseFastUnorderedTimeStamps 
-XX:+UseParallelGC //是并行gc
```
5. 通过jmap查看堆的信息
```
zhangchuan@ gc$ jmap -heap 2079
Attaching to process ID 2079, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.131-b11

using thread-local object allocation.
Parallel GC with 4 thread(s) //有4个gc线程，默认值为cpu核心数，通过这里知道就是逻辑cpu数据量，可以通过-XX:ParallelGCThreads=N来指定

Heap Configuration:
   MinHeapFreeRatio         = 0
   MaxHeapFreeRatio         = 100
   MaxHeapSize              = 2147483648 (2048.0MB)//默认物理内存的1/4
   NewSize                  = 44564480 (42.5MB)
   MaxNewSize               = 715653120 (682.5MB)
   OldSize                  = 89653248 (85.5MB) //默认新生代：老年代= 1：2，可以通过-Xmn参数指定新生代大小
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
PS Young Generation
Eden Space:
   capacity = 142606336 (136.0MB)
   used     = 35035736 (33.412681579589844MB)
   free     = 107570600 (102.58731842041016MB)
   24.56814822028665% used
From Space:
   capacity = 524288 (0.5MB)
   used     = 131072 (0.125MB)
   free     = 393216 (0.375MB)
   25.0% used
To Space:
   capacity = 524288 (0.5MB)
   used     = 0 (0.0MB)
   free     = 524288 (0.5MB)
   0.0% used
PS Old Generation
   capacity = 80216064 (76.5MB)
   used     = 51714016 (49.318328857421875MB)
   free     = 28502048 (27.181671142578125MB)
   64.468403735192% used
```

6. 设置内存初始值和最大值
```
//启动程序
java -jar -Xms512m -Xmx512m -XX:-UseAdaptiveSizePolicy gateway-server-0.0.1-SNAPSHOT.jar

//堆内存信息
using thread-local object allocation.
Parallel GC with 4 thread(s)

Heap Configuration:
   MinHeapFreeRatio         = 40
   MaxHeapFreeRatio         = 70
   MaxHeapSize              = 536870912 (512.0MB)
   NewSize                  = 178782208 (170.5MB)
   MaxNewSize               = 178782208 (170.5MB)
   OldSize                  = 358088704 (341.5MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
PS Young Generation
Eden Space:
   capacity = 134742016 (128.5MB)
   used     = 71361768 (68.0558853149414MB)
   free     = 63380248 (60.444114685058594MB)
   52.96177845520732% used
From Space:
   capacity = 22020096 (21.0MB)
   used     = 14677280 (13.997344970703125MB)
   free     = 7342816 (7.002655029296875MB)
   66.65402367001488% used
To Space:
   capacity = 22020096 (21.0MB)
   used     = 0 (0.0MB)
   free     = 22020096 (21.0MB)
   0.0% used
PS Old Generation
   capacity = 358088704 (341.5MB)
   used     = 10066032 (9.599716186523438MB)
   free     = 348022672 (331.90028381347656MB)
   2.811044271309938% used
```
如果没有参数-XX:-UseAdaptiveSizePolicy，eden,s0,s1的比例不确定，但是eden和s区的大比例是不变的。开启之后eden:s0:s1约等于8：1：1


