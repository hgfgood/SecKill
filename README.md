# 秒杀系统设计

主要关注秒杀系统的秒杀功能实现，具体的前期的用户登录和后期发货处理细节不处理。

## 面临的主要问题
1. 突然增加的大量网络流量：使用cdn加速，将页面静态化。
2. 秒杀url只能在秒杀开始后可以访问到：
3. 控制前端秒杀按钮点亮：静态页面中使用javascript点亮
4. 多台服务器均衡负载
5. 防止机器秒杀：专用验证码和URL

## 主要使用的技术和框架

- 使用c3p0实现数据库链接池。
- 使用mybatis+spring框架实现后台。
- 使用lsf4j+logback实现日志处理，使用mysql数据库。
- JUnit4测试
- 使用maven管理项目依赖关系。使用git管理项目的版本。


## 框架整合的核心思想

使用c3p0数据库连接池作为数据源，mybatis操作数据库，并且实现dao接口（我们只需要编写接口，mabatis负责对接口实现），


- 设置logback日志配置。

### dao层
- 使用spring的resource loader加载数据库链接配置。
- 使用c3p0作为数据库的连接池，并使用spring读取的配置作为数据库配置，并设置链接池的属性。
- 使用c3p0作为数据源初始化mybatis的`sqlSessionfactory`，使用xml配置mybatis的映射配置文件。
- 利用mybatis自动扫描entry包和dao接口并注入到springIoc容器，**节省实现Dao接口**。

### service

**从使用者的角度设计接口**
- 使用spring声明式事务，
- 使用dto传输层负责后台数据与web层交互。

### web层

- 在web.xml中配置启动spring mvc的分发器，并设置spring MVC配置参数，拦截所有请求。
- 在spring的mvc中配置springmvc 的默认servlet处理器，ModelAndView的类别和返回会文件路径。


## 数据库设计
秒杀系统主要包括两个表：
1. 秒杀商品表secitem[商品id，商品名，秒杀数量，开始秒杀时间，结束秒杀时间，创建记录时间]
2. 秒杀记录表seckill[秒杀商品，秒杀用户手机号，秒杀时间，秒杀状态]
其中使用seckill表是组合主键（item_id和phone），秒杀状态表示用户的购物状态，-1表示没有秒杀到，0表示秒杀成功，1表示付款，2表示发货。。等等。

```sql
CREATE TABLE secitem(
  item_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  name VARCHAR(120) NOT NULL  COMMENT '商品名',
  number INT NOT NULL COMMENT '商品数量',
  create_time TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE CURRENT_TIMESTAMP COMMENT '秒杀商品创建时间',
  start_time TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '秒杀开始时间',
  end_time TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '秒杀结束时间',
  PRIMARY KEY (item_id),
  KEY index_start_time(start_time),
  KEY index_end_time(end_time),
  KEY index_create_time(create_time)

)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT ='秒杀库存表';


```
添加秒杀商品库存：
```sql
INSERT INTO secitem(name,number,start_time,end_time)
    VALUES
      ('iphone6s',10,'2016-06-01 00:00:00','2016-06-02 00:00:00'),
      ('iphone5s',9,'2016-06-01 00:00:00','2016-06-02 00:00:00'),
      ('iphone4s',8,'2016-06-01 00:00:00','2016-06-02 00:00:00'),
      ('ipad4',7,'2016-06-01 00:00:00','2016-06-02 00:00:00');
```


```sql
CREATE TABLE seckill(
  item_id BIGINT NOT NULL COMMENT '秒杀商品ID',
  phone BIGINT NOT NULL COMMENT '秒杀用户电话',
  status TINYINT NOT NULL DEFAULT 0 COMMENT '秒杀状态：-1无效，1成功，2付款',
  kill_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '秒杀成功时间',
  PRIMARY KEY (item_id,phone),
  KEY index_create_time(kill_time)
)ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT '秒杀记录表';

```

> 在上面设置timestap类型数据如果使用了not null，那么default必须是要显示填写的，并且填写的时间必须为有效时间，有效范围在`'1970-01-01 00:00:01' UTC to '2038-01-19 03:14:07' UTC.`

## 数据访问层

### 设置实体

将数据库的每个表对应一个实体类，并实现getter和setter，还有toString，方便测试。

### 设置dao接口

将需要对每个表的操作，用接口函数的形式表示出来。
> 函数的返回类型，确定使用数据库查询的结果，参数可以表示查询条件，函数名可以表现出操作的实际意义。

> 由于在mybatis实现接口的过程中，会使用arg0，arg1,arg2,等，代替接口函数的参数名，需要**显示的使用Param注解**，定义实现类对应函数的参数。这样才能将mapper配置的参数和实现类的参数对应上。


### 配置数据库链接池

使用c3p0的`ComboPooledDataSource`类实现数据库连接池。并使用spring注解和依赖注入，将需要的数据库林按揭参数注入到`ComboPooledDataSource`类中。
设置的主要参数有两类：
1. 数据库链接参数，包括jdbc driver, url, 数据库user和对应密码
2. c3p0配置参数：数据库连接池的大小范围maxPoolSize和minPoolSize，并且关闭链接断开自动commit的设置，设置默认的充实次数和等待数据库连接的时间（默认为0,表示一直等待）。

### mybatis和spring整合

1. 注入SqlSessionFactory。
	- 主要参数：
	- 使用的数据源
	- 实体类
	- 实体和数据表的映射关系xml文件 
2. 配置mybatis自动扫描dao接口，并注入到spring中。


### 测试

利用JUnit4测试dao有关的接口是否符合预期。

主要使用@RunWith添加spring启动和@ContextConfiguration配置spring启动参数。

使用JUnit启动Spring，然后Spring自动扫描所有组件并添加到Ioc容器，使用mybatis实现的包扫描自动扫描dao，并将对应的实现类添加到springde Ioc容器。


## dto层

dto数据传输层，主要实现业务操作的结果传输。

## service层
主要实现业务逻辑。

### spring事务

spring的事务回滚

在发生`RunTimeException`的时候发生事务回滚，其他编译器的异常不会回滚。

> 注意try-catch语句：当时用try - catch将异常处理掉后，spring 不能察觉一场的存在，默认会commit，提交事务。

注解事务的优点：
1. 开发团队达成一致，明确标注事务方法的编程风格。
2. 保证事务方法执行时间尽可能短，不要操作其他网络（RPC/http）操作（防止堵塞数据库）。如果还是需要，剥离到事物方法外。
3. 不是所有的方法都需要事务。没有并发场景不需要事务（只读操作不需要事务）


## RestFul规范

- get 查询操作
- post 添加/修改操作
- put 修改操作（幂等操作）
- delete 删除操作

## web层

主要**使用springMVC实现restful接口！**

### 设计URL

- 获取系统时间  GET /seckill/time/now
- 获取秒杀商品列表 GET /seckill/list
- 获取商品详情 GET /seckil/{id}/detail
- 暴露秒杀 POST /seckill/{id}/exposer
- 秒杀商品 POST /seckill/{id}/{md5}/execution

### springMVC 映射技巧

@RequestMapping注解：
- 支持标准的URL
- 支持Ant风格URL（?匹配一个字符;\*匹配任意字符;\*\* 匹配任意URL
- 带{×××}占位符的URL


### 请求方法的处理细节

1. 请求参数绑定
	使用`{xxx}`带占位符的方式请求URL并利用springmvc 的@PathVariable注解参数，从请求url上获取参数。
2. 请求方法限制
	在RequestMapping中添加请求方法的设置，`method= RequestMethod.GET/POST...`
3. 请求转发和重定向
	在return的时候，使用`return "redirect:url"`重定向到url;使用`return "forward:url"`转发到url;
4. 数据模型赋值
	后台使用spring MVC提供的Modal，addAttribute方法设置参数，传递到web层。web层使用EL表达式获取返回值。
5. 返回json数据
	使用`@ResponseBody`并在RequestMapping中设置返回contenttype为`application/json`。
6. cookie访问
	在spring mvc中使用`@CookieValue` 获取cookie中的键值对。

### 前端

1. 使用jquery的countdown插件倒计时
		默认会对html中的插件添加一个countdown的方法，使用该插件的countdown(到达时间，计时响应时间[每隔一秒]).on('finish-countdown', 结束计时响应事件)。
2. 使用jquery的cookie插件从前端操作cookie
	使用`$.cookie('#id')`获取cookie中的值。
	使用`$.cookie('key', 'value',{cookie属性：expire，path等})`

## 并发优化

主要的秒杀流程：
1. 查看秒杀商品详情页
2. 查询时间
3. 倒计时
4. 暴露秒杀地址
5. 执行秒杀
    - 插入秒杀记录
    - 减库存

优化分析：
- 详情页：使用cdn静态页面展示，不需要优化
- 查询时间：java访问内存的时间大约是10ns，每秒可访问1亿次，不需要优化
- 倒计时：在客户端，不需要优化
- 暴露秒杀地址：
    - 使用缓存缓存秒杀地址，如reids，memorycache等，大约1秒10万qps
    - 使用protostuff从redis中获取秒杀商品，加速序列化和网络传输
- 执行秒杀：
    - 主要的时间消耗在数据库的事务上
           - 访问数据库的网络延时（需要两次访问数据库：减库存和插入秒杀记录）
           - 可能的GC时间
           - 此时只有一次访问
           - 一次访问至少也得2ms
           - 1s只有1000qps
    - 单独在数据库端执行事务，据说有40000qps，我的pc上有1400qps  ->说明服务器数据库执行数据库事务速度还是比较可观的，但是数据库事务时间主要被花数据库访问（网络延时）
    - 使用存储过程，将秒杀的事务操作放置在数据库端[避免了原来获取事务后，通过网络传输sql请iqu，然后获取结果的时间延时],减少事务行级锁持有时间[简单的逻辑可以使用存储过程]。


### 存在的问题
1. 调用expose url方法，当传入的是错误的itemid，没有处理[一般itemid是由前面的操作请求得到的，但是也有可能**用户直接调用URL**，加重系统的负载，但是对业务逻辑没有太大影响]