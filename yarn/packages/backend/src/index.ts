import express from "express";
import cors from "cors";
import helmet from "helmet";
import morgan from "morgan";
import { ApiResponse, Todo, User, generateId } from "@workspace/shared";

const app = express();
const PORT = process.env.PORT || 3001;

// Middleware
app.use(helmet());
app.use(cors());
app.use(morgan("combined"));
app.use(express.json());

// Mock data
const users: User[] = [
  {
    id: "1",
    name: "John Doe",
    email: "john@example.com",
    createdAt: new Date(),
  },
];

const todos: Todo[] = [
  {
    id: "1",
    title: "Learn TypeScript",
    completed: false,
    userId: "1",
    createdAt: new Date(),
    updatedAt: new Date(),
  },
  {
    id: "2",
    title: "Build Yarn workspace app",
    completed: true,
    userId: "1",
    createdAt: new Date(),
    updatedAt: new Date(),
  },
];

// Routes
app.get("/api/health", (req, res) => {
  const response: ApiResponse<{ status: string }> = {
    success: true,
    data: { status: "OK" },
  };
  res.json(response);
});

app.get("/api/users", (req, res) => {
  const response: ApiResponse<User[]> = {
    success: true,
    data: users,
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
  const { title, userId } = req.body;

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
  res.status(201).json(response);
});

app.put("/api/todos/:id", (req, res) => {
  const { id } = req.params;
  const { completed } = req.body;

  const todoIndex = todos.findIndex((todo) => todo.id === id);

  if (todoIndex === -1) {
    const response: ApiResponse<null> = {
      success: false,
      error: "Todo not found",
    };
    return res.status(404).json(response);
  }

  todos[todoIndex] = {
    ...todos[todoIndex],
    completed: completed ?? todos[todoIndex].completed,
    updatedAt: new Date(),
  };

  const response: ApiResponse<Todo> = {
    success: true,
    data: todos[todoIndex],
  };
  res.json(response);
});

app.listen(PORT, () => {
  console.log(`🚀 Backend server running on http://localhost:${PORT}`);
});
