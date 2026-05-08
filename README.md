# SkyTakeOut - 智能外卖订单管理系统

基于 Spring Boot 3 + Spring AI 的外卖全流程业务平台，覆盖 C 端用户下单、B 端商家接单、管理端运营全场景。

## 技术栈

- **核心框架**: Spring Boot 3.5.7、MyBatis 3.0.5
- **AI 能力**: Spring AI 1.1.4（ChatModel、Embedding、VectorStore）
- **数据存储**: MySQL 8.0、Redis 7.x
- **其他**: WebSocket、Apache POI、PageHelper、JWT、Aliyun OSS、WeChat Pay

## 项目结构

```
SkyTakeOut/
├── sky-common/          # 公共模块（工具类、常量、异常等）
├── sky-pojo/            # 实体对象（DTO、Entity、VO）
└── sky-server/          # 业务服务
    ├── ai/              # AI 增强功能（订单备注解析、差评回复等）
    ├── controller/      # 控制器层（admin/user）
    ├── service/         # 服务层
    ├── mapper/          # 数据访问层
    └── task/            # 定时任务
```

## 核心功能

### 传统外卖功能
- **用户端**: 微信登录、菜品浏览、购物车、下单支付、订单查询、评价系统
- **管理端**: 员工管理、菜品/套餐管理、订单处理、数据统计、报表导出
- **性能优化**: Redis 多级缓存（QPS 提升 340%）、WebSocket 实时推送、定时任务自动处理超时订单

### AI 智能增强
- **订单备注智能解析**: 自动解析自然语言备注（如"不要辣、放门卫"），提取结构化标签
- **差评智能回复（RAG）**: AI 分析情感 + RAG 检索模板 + 自动生成回复草稿，商家审核后发布
- **智能客服**: 基于知识库的自动问答系统
- **向量存储分离**: 不同业务使用独立的 SimpleVectorStore Bean，物理隔离

## 快速开始

### 环境要求
- JDK 17+、MySQL 8.0+、Redis 7.0+、Maven 3.6+

### 配置步骤
1. 复制 `application-dev.yml.example` 为 `application-dev.yml`，修改数据库、Redis、AI API Key 等配置
2. 初始化数据库（执行 SQL 脚本）
3. 运行 `mvn spring-boot:run` 启动应用

## 设计原则

- **规则优先**: 90% 业务逻辑用传统代码，AI 仅做补位
- **零侵入**: 不修改原有核心流程，通过扩展字段/表实现
- **异步降级**: AI 调用设置超时，失败时降级为规则匹配

---

Made with ❤️ by Jiang
