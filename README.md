
# LLM Call Guard

> 🚧 A lightweight, annotation-driven **LLM call governance** starter for Spring Boot.

**LLM Call Guard** 是一个面向业务系统的大模型调用治理组件，
通过 **注解方式** 为 LLM 调用提供 **并发控制、去重合并、超时重试、fallback 兜底** 等运行时保护能力。

它的目标很简单：

> **让业务只关心“调用模型做什么”，而不必关心“如何安全、稳定地调用模型”。**

---

## ✨ Features

* ✅ **Annotation-driven**：一行注解即可启用
* ✅ **Automatic Key Generation**：无需手写 key，默认智能生成
* ✅ **Concurrency Guard**：同 key 并发互斥，防止请求风暴
* ✅ **Singleflight**：同 key 共享一次真实调用，避免重复消耗
* ✅ **Merge Window（可选）**：连续触发合并为一次调用
* ✅ **Timeout & Retry**：防止模型拖垮主业务
* ✅ **Fallback Handler**：模型失败时优雅降级
* ✅ **Low Intrusion**：不绑定任何 LLM Provider
* ✅ **Graceful Degradation**：无 Redis 时自动降级为本地实现

---

## 🚀 Quick Start (5 Minutes)

### 1. 引入依赖

```xml
<dependency>
  <groupId>io.github.yourname</groupId>
  <artifactId>llm-call-guard-starter</artifactId>
  <version>0.1.0</version>
</dependency>
```

> Spring Boot 2.7+ / 3.x 均可使用

---

### 2. 在 LLM 调用方法上加注解

```java
@Service
public class TicketAiService {

  @LLMGuard(scene = "ticket_autofill")
  public AiResult extract(TicketReq req) {
    return llmProvider.call(req);
  }
}
```

🎉 **就这么多。**

默认你已经获得：

* 自动 key 生成
* 并发互斥
* singleflight
* 8s 超时
* 结构化日志

---

## 🧠 Why LLM Call Guard?

在真实业务中，直接调用大模型通常会遇到这些问题：

* 用户连续操作，**同一会话多次触发模型**
* 瞬时并发高，**模型被打爆**
* 模型超时 / 报错，**影响主流程**
* 每个项目都要重复写：

  * 锁
  * 幂等
  * 重试
  * 降级

这些问题 **与业务逻辑无关**，却几乎每个团队都会踩。

**LLM Call Guard** 将这些通用问题抽离为一个 **独立、可复用的运行时治理组件**。

---

## 🧩 Core Concepts

### Scene

业务场景标识，用于区分不同 LLM 调用语义。

```java
@LLMGuard(scene = "ticket_autofill")
```

---

### Key（自动生成）

默认按以下优先级生成：

1. 注解中显式指定的 `key`（SpEL）
2. 方法参数中自动提取：

   * `sessionId`
   * `userId`
   * `tenantId`
3. 兜底：`scene + methodSignature`

> **80% 场景无需手写 key**

---

### Singleflight

同一个 key 的并发请求：

* 只会有 **一次真实模型调用**
* 其他请求共享结果

---

### Merge Window（可选）

适合对话 / 连续触发场景：

```java
@LLMGuard(
  scene = "chat_analysis",
  mergeWindowMs = 2000
)
```

2 秒内的多次触发 → 合并为一次调用。

---

## ⚙️ Advanced Usage

### Custom Timeout / Retry

```java
@LLMGuard(
  scene = "ticket_autofill",
  timeoutMs = 10000,
  retries = 1
)
public AiResult extract(TicketReq req) {
  return llmProvider.call(req);
}
```

---

### Fallback Handler

当模型超时或异常时返回兜底结果：

```java
@Component("ticketFallback")
public class TicketFallback implements FallbackHandler {

  @Override
  public Object fallback(CallContext ctx, Throwable cause) {
    return AiResult.degraded("AI 繁忙，请稍后重试");
  }
}
```

```java
@LLMGuard(
  scene = "ticket_autofill",
  fallback = "ticketFallback"
)
public AiResult extract(TicketReq req) {
  return llmProvider.call(req);
}
```

---

## 🛠 Configuration

```yaml
llm:
  guard:
    enabled: true

    defaults:
      timeoutMs: 8000
      retries: 0
      backoffMs: 200
      mergeWindowMs: 0
      lockEnabled: true
      lockTtlMs: 15000
      singleflightEnabled: true

    observability:
      logEnabled: true
      includeArgs: false
      includeResult: false
```

---

## 📊 Observability (MVP)

每次调用会输出结构化日志：

```json
{
  "traceId": "c1a9...",
  "scene": "ticket_autofill",
  "key": "ticket_autofill:session123",
  "latencyMs": 1320,
  "status": "SUCCESS",
  "singleflight": true,
  "merged": false
}
```

> 默认不打印请求参数和返回值，避免敏感信息泄露。

---

## 🧱 Architecture Overview

```text
Business Method
      ↓
   @LLMGuard (AOP)
      ↓
   CallContext + Plan
      ↓
  GuardExecutor
   ├─ LockManager
   ├─ Singleflight
   ├─ MergeWindow
   ├─ TimeoutRunner
   ├─ RetryRunner
   └─ FallbackHandler
      ↓
   LLM Provider Call
```

---

## ❌ Non-Goals

* ❌ 不负责模型选择
* ❌ 不统计 Token / 成本
* ❌ 不提供 UI 控制台
* ❌ 不绑定 OpenAI / Claude / 任意 Provider

---

## 🗺 Roadmap

* [ ] Redis-based distributed lock (Redisson)
* [ ] More merge strategies (APPEND)
* [ ] Event publishing (for cost / monitoring integration)
* [ ] Kotlin / Reactive support

---

## 🤝 Contributing

PR / Issue welcome ❤️
如果你也在真实业务中使用 LLM，欢迎一起把这个组件打磨成 **工程级解决方案**。

---

## 📄 License

MIT License

---