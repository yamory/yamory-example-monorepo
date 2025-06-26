import express from "express";
import cors from "cors";
import {
  User,
  Task,
  ApiResponse,
  createSuccessResponse,
  createErrorResponse,
  generateId,
  validateEmail,
} from "@workspace/shared";

const app = express();
const PORT = process.env.PORT || 3001;

// ミドルウェア
app.use(cors());
app.use(express.json());

// インメモリデータストア（実際のアプリではDBを使用）
let users: User[] = [
  {
    id: 1,
    name: "田中太郎",
    email: "tanaka@example.com",
    createdAt: new Date(),
  },
  {
    id: 2,
    name: "佐藤花子",
    email: "sato@example.com",
    createdAt: new Date(),
  },
];

let tasks: Task[] = [
  {
    id: 1,
    title: "プロジェクト資料作成",
    description: "来週のプレゼン用資料を作成する",
    completed: false,
    userId: 1,
    createdAt: new Date(),
  },
  {
    id: 2,
    title: "ミーティング準備",
    description: "チームミーティングのアジェンダを準備する",
    completed: true,
    userId: 2,
    createdAt: new Date(),
  },
];

// ユーザー関連のAPI
app.get("/api/users", (req, res) => {
  res.json(createSuccessResponse(users));
});

app.get("/api/users/:id", (req, res) => {
  const userId = parseInt(req.params.id);
  const user = users.find((u) => u.id === userId);

  if (!user) {
    return res
      .status(404)
      .json(createErrorResponse("ユーザーが見つかりません"));
  }

  res.json(createSuccessResponse(user));
});

app.post("/api/users", (req, res) => {
  const { name, email } = req.body;

  if (!name || !email) {
    return res
      .status(400)
      .json(createErrorResponse("名前とメールアドレスは必須です"));
  }

  if (!validateEmail(email)) {
    return res
      .status(400)
      .json(createErrorResponse("有効なメールアドレスを入力してください"));
  }

  const newUser: User = {
    id: generateId(),
    name,
    email,
    createdAt: new Date(),
  };

  users.push(newUser);
  res.status(201).json(createSuccessResponse(newUser));
});

// タスク関連のAPI
app.get("/api/tasks", (req, res) => {
  res.json(createSuccessResponse(tasks));
});

app.get("/api/tasks/:id", (req, res) => {
  const taskId = parseInt(req.params.id);
  const task = tasks.find((t) => t.id === taskId);

  if (!task) {
    return res.status(404).json(createErrorResponse("タスクが見つかりません"));
  }

  res.json(createSuccessResponse(task));
});

app.post("/api/tasks", (req, res) => {
  const { title, description, userId } = req.body;

  if (!title || !userId) {
    return res
      .status(400)
      .json(createErrorResponse("タイトルとユーザーIDは必須です"));
  }

  const user = users.find((u) => u.id === userId);
  if (!user) {
    return res
      .status(400)
      .json(createErrorResponse("指定されたユーザーが存在しません"));
  }

  const newTask: Task = {
    id: generateId(),
    title,
    description: description || "",
    completed: false,
    userId,
    createdAt: new Date(),
  };

  tasks.push(newTask);
  res.status(201).json(createSuccessResponse(newTask));
});

app.put("/api/tasks/:id", (req, res) => {
  const taskId = parseInt(req.params.id);
  const { title, description, completed } = req.body;

  const taskIndex = tasks.findIndex((t) => t.id === taskId);
  if (taskIndex === -1) {
    return res.status(404).json(createErrorResponse("タスクが見つかりません"));
  }

  tasks[taskIndex] = {
    ...tasks[taskIndex],
    ...(title && { title }),
    ...(description !== undefined && { description }),
    ...(completed !== undefined && { completed }),
  };

  res.json(createSuccessResponse(tasks[taskIndex]));
});

app.delete("/api/tasks/:id", (req, res) => {
  const taskId = parseInt(req.params.id);
  const taskIndex = tasks.findIndex((t) => t.id === taskId);

  if (taskIndex === -1) {
    return res.status(404).json(createErrorResponse("タスクが見つかりません"));
  }

  tasks.splice(taskIndex, 1);
  res.json(createSuccessResponse({ message: "タスクが削除されました" }));
});

// ヘルスチェック
app.get("/health", (req, res) => {
  res.json({ status: "OK", timestamp: new Date().toISOString() });
});

app.listen(PORT, () => {
  console.log(`🚀 Backend server is running on http://localhost:${PORT}`);
});
