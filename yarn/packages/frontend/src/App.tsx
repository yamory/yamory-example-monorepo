import React, { useEffect, useState } from 'react'
import { Todo, User, ApiResponse } from '@workspace/shared'
import axios from 'axios'
import './App.css'

interface TodoItemProps {
  todo: Todo
  onToggle: (id: string, completed: boolean) => void
}

const TodoItem: React.FC<TodoItemProps> = ({ todo, onToggle }) => {
  return (
    <div className={`todo-item ${todo.completed ? 'completed' : ''}`}>
      <input
        type="checkbox"
        checked={todo.completed}
        onChange={(e) => onToggle(todo.id, e.target.checked)}
      />
      <span className="todo-title">{todo.title}</span>
      <span className="todo-date">
        {new Date(todo.createdAt).toLocaleDateString()}
      </span>
    </div>
  )
}

const App: React.FC = () => {
  const [todos, setTodos] = useState<Todo[]>([])
  const [users, setUsers] = useState<User[]>([])
  const [newTodoTitle, setNewTodoTitle] = useState('')
  const [selectedUserId, setSelectedUserId] = useState<string>('')
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    fetchUsers()
    fetchTodos()
  }, [selectedUserId])

  const fetchUsers = async () => {
    try {
      const response = await axios.get<ApiResponse<User[]>>('/api/users')
      if (response.data.success && response.data.data) {
        setUsers(response.data.data)
        if (!selectedUserId && response.data.data.length > 0) {
          setSelectedUserId(response.data.data[0].id)
        }
      }
    } catch (error) {
      console.error('Error fetching users:', error)
    }
  }

  const fetchTodos = async () => {
    try {
      setLoading(true)
      const params = selectedUserId ? { userId: selectedUserId } : {}
      const response = await axios.get<ApiResponse<Todo[]>>('/api/todos', { params })
      if (response.data.success && response.data.data) {
        setTodos(response.data.data)
      }
    } catch (error) {
      console.error('Error fetching todos:', error)
    } finally {
      setLoading(false)
    }
  }

  const addTodo = async () => {
    if (!newTodoTitle.trim() || !selectedUserId) return

    try {
      const response = await axios.post<ApiResponse<Todo>>('/api/todos', {
        title: newTodoTitle,
        userId: selectedUserId
      })
      if (response.data.success && response.data.data) {
        setTodos(prev => [...prev, response.data.data!])
        setNewTodoTitle('')
      }
    } catch (error) {
      console.error('Error adding todo:', error)
    }
  }

  const toggleTodo = async (id: string, completed: boolean) => {
    try {
      const response = await axios.put<ApiResponse<Todo>>(`/api/todos/${id}`, {
        completed
      })
      if (response.data.success && response.data.data) {
        setTodos(prev =>
          prev.map(todo => (todo.id === id ? response.data.data! : todo))
        )
      }
    } catch (error) {
      console.error('Error updating todo:', error)
    }
  }

  const currentUser = users.find(user => user.id === selectedUserId)

  return (
    <div className="app">
      <header className="app-header">
        <h1>📝 Yarn Workspace Todo App</h1>
        <p>フロントエンド・バックエンド・共通ライブラリを使用</p>
      </header>

      <main className="app-main">
        <div className="user-selector">
          <label htmlFor="user-select">ユーザー選択：</label>
          <select
            id="user-select"
            value={selectedUserId}
            onChange={(e) => setSelectedUserId(e.target.value)}
          >
            <option value="">ユーザーを選択</option>
            {users.map(user => (
              <option key={user.id} value={user.id}>
                {user.name} ({user.email})
              </option>
            ))}
          </select>
        </div>

        {currentUser && (
          <div className="current-user">
            <h2>👤 {currentUser.name}のTodo</h2>
          </div>
        )}

        <div className="add-todo">
          <input
            type="text"
            value={newTodoTitle}
            onChange={(e) => setNewTodoTitle(e.target.value)}
            placeholder="新しいタスクを入力"
            onKeyPress={(e) => e.key === 'Enter' && addTodo()}
          />
          <button onClick={addTodo} disabled={!newTodoTitle.trim() || !selectedUserId}>
            追加
          </button>
        </div>

        <div className="todos-container">
          {loading ? (
            <div className="loading">読み込み中...</div>
          ) : todos.length === 0 ? (
            <div className="empty-state">タスクがありません</div>
          ) : (
            <div className="todos-list">
              {todos.map(todo => (
                <TodoItem
                  key={todo.id}
                  todo={todo}
                  onToggle={toggleTodo}
                />
              ))}
            </div>
          )}
        </div>

        <div className="stats">
          <p>
            完了: {todos.filter(t => t.completed).length} /
            総数: {todos.length}
          </p>
        </div>
      </main>
    </div>
  )
}

export default App