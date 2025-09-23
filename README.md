# 📚 Database_principle 项目简介

欢迎来到 **CS307 数据库原理 Project**！

本项目围绕数据库原理课程展开，覆盖了数据库设计、实现与实际操作，主要采用 Java ☕ 结合 PostgreSQL 🐘 技术栈进行开发与实验。

---

## 🗂️ 仓库信息

- **项目名称**：Database_principle
- **主要语言**：Java ☕ & PostgreSQL 🐘
- **适用课程**：CS307 数据库原理
- **典型框架**：Spring Boot、JDBC
- **数据库**：PostgreSQL 16

---

## ✨ 项目亮点

- 💡 结合理论与实践，提供从数据库 schema 设计到数据导入、用户权限、业务接口实现等全流程代码。
- 🛠️ 代码结构清晰，模块划分明确，方便查阅与复用。
- 📄 提供标准化数据导入脚本 [`GoodLoader`](project2/submit/GoodLoader.java)，可保证本地测试环境与评测环境一致。
- 🧩 支持批量数据导入、数据清理（truncate）、用户权限管理等数据库常用操作。
- 🔒 涉及数据库用户创建、登录与 PL/pgSQL 脚本执行，贴合实际业务场景。

---

## 🚀 快速开始

1. **环境准备**
   - 安装 [PostgreSQL](https://www.postgresql.org/docs/16/index.html) 数据库
   - 安装 [Java 17+](https://adoptium.net/) 及 [Gradle](https://gradle.org/)
2. **克隆仓库**
   ```bash
   git clone https://github.com/csgrace/Database_principle.git
   ```
3. **配置数据库连接**
   - 按需修改 `GoodLoader.java`、`DatabaseCleaner.java` 等文件中的数据库连接信息（host、user、password 等）。
4. **数据导入**
   - 使用 `GoodLoader` 脚本将标准数据文件（如 `pubmed24n.ndjson`）导入 PostgreSQL。
5. **运行后端服务**
   - 进入相关子项目目录，使用 Gradle 进行构建和启动。

---

## 🗃️ 目录结构概览

- `project1/`：第一阶段实验代码，包括数据库清理、数据插入、SQL 测试等
- `project2/`：第二阶段，包含后端接口、业务实现、批量数据导入等
- `project2/submit/GoodLoader.java`：标准数据导入脚本（支持自定义 schema）
- `project2/Project2_code（backend_to_frontend）/`：Spring Boot 后端、接口实现
- `README.md`、`requirements.md`：项目说明与需求文档

---

## 🔗 相关资料

- [PostgreSQL 16 官方文档](https://www.postgresql.org/docs/16/index.html)
- [Spring Boot 2.7 文档](https://docs.spring.io/spring-boot/docs/2.7.16/reference/htmlsingle/)
- [Gradle 用户手册](https://docs.gradle.org/8.3/userguide/userguide.html)
- [Lombok 特性](https://projectlombok.org/features/)

---



