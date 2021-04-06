# seckill-mq
基于MQ实现秒杀

所用到的技术栈
- RocketMQ
- Redis
- MySQL
- Dubbo
- Zookeeper
- SpringBoot
- Mybatis-Plus

# 为何要写这个项目

1. 主要是学习RocketMQ在实际项目中的应用,作为一个练手的项目
2. 在项目中使用RocketMQ进行流量的削峰填谷,防止大量请求直接灌入DB
3. 结合Dubbo实现微服务之间的调用

# 待完成的地方

- [ ] 项目的流程时序图,方便理解
- [ ] MQ记录一下消费信息
- [ ] 提供快速开始手册
- [ ] 搭建用户登录模块
- [ ] 整合gateway网关实现鉴权
- [ ] 加入限流,了解一下限流策略