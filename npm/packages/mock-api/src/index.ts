import express from "express";
import cors from "cors";
import { User, Todo, ApiResponse, generateId } from "@workspace/shared";

const app = express();
const PORT = process.env.PORT || 3001;

app.use(cors());
app.use(express.json());

// Mock data
const users: User[] = [
  {
    id: "user1",
    name: "田中太郎",
    email: "tanaka@example.com",
    createdAt: new Date("2024-01-01"),
  },
  {
    id: "user2",
    name: "佐藤花子",
    email: "sato@example.com",
    createdAt: new Date("2024-01-02"),
  },
];

let todos: Todo[] = [
  {
    id: "todo1",
    title: "プロジェクトの企画書作成",
    description: "新しいプロジェクトの企画書を作成する",
    completed: false,
    userId: "user1",
    createdAt: new Date("2024-01-01"),
    updatedAt: new Date("2024-01-01"),
  },
  {
    id: "todo2",
    title: "ミーティングの準備",
    description: "明日のミーティング資料を準備する",
    completed: true,
    userId: "user1",
    createdAt: new Date("2024-01-02"),
    updatedAt: new Date("2024-01-02"),
  },
];

// Routes
app.get("/api/users", (req, res) => {
  const response: ApiResponse<User[]> = {
    success: true,
    data: users,
  };
  res.json(response);
});

app.get("/api/users/:id", (req, res) => {
  const user = users.find((u) => u.id === req.params.id);
  if (!user) {
    const response: ApiResponse<null> = {
      success: false,
      error: "User not found",
    };
    return res.status(404).json(response);
  }

  const response: ApiResponse<User> = {
    success: true,
    data: user,
  };
  res.json(response);
});

app.get("/api/todos", (req, res) => {
  const { userId } = req.query;
  let filteredTodos = todos;

  if (userId) {
    filteredTodos = todos.filter((todo) => todo.userId === userId);
  }

  const response: ApiResponse<Todo[]> = {
    success: true,
    data: filteredTodos,
  };
  res.json(response);
});

app.post("/api/todos", (req, res) => {
  const { title, description, userId } = req.body;

  if (!title || !userId) {
    const response: ApiResponse<null> = {
      success: false,
      error: "Title and userId are required",
    };
    return res.status(400).json(response);
  }

  const newTodo: Todo = {
    id: generateId(),
    title,
    description,
    completed: false,
    userId,
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  todos.push(newTodo);

  const response: ApiResponse<Todo> = {
    success: true,
    data: newTodo,
  };
  res.json(response);
});

app.put("/api/todos/:id", (req, res) => {
  const todoIndex = todos.findIndex((t) => t.id === req.params.id);
  if (todoIndex === -1) {
    const response: ApiResponse<null> = {
      success: false,
      error: "Todo not found",
    };
    return res.status(404).json(response);
  }

  const { title, description, completed } = req.body;
  const updatedTodo = {
    ...todos[todoIndex],
    ...(title && { title }),
    ...(description !== undefined && { description }),
    ...(completed !== undefined && { completed }),
    updatedAt: new Date(),
  };

  todos[todoIndex] = updatedTodo;

  const response: ApiResponse<Todo> = {
    success: true,
    data: updatedTodo,
  };
  res.json(response);
});

app.delete("/api/todos/:id", (req, res) => {
  const todoIndex = todos.findIndex((t) => t.id === req.params.id);
  if (todoIndex === -1) {
    const response: ApiResponse<null> = {
      success: false,
      error: "Todo not found",
    };
    return res.status(404).json(response);
  }

  todos.splice(todoIndex, 1);

  const response: ApiResponse<null> = {
    success: true,
  };
  res.json(response);
});

app.listen(PORT, () => {
  console.log(`Mock API server running on port ${PORT}`);
});
