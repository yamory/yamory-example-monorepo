import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { User, Task, ApiResponse, formatDate } from '@workspace/shared';

const API_BASE_URL = '/api';

function App() {
  const [users, setUsers] = useState<User[]>([]);
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const [success, setSuccess] = useState<string>('');

  // タスク作成フォーム用の状態
  const [newTaskTitle, setNewTaskTitle] = useState('');
  const [newTaskDescription, setNewTaskDescription] = useState('');
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);

  // データを取得
  const fetchData = async () => {
    try {
      setLoading(true);
      const [usersResponse, tasksResponse] = await Promise.all([
        axios.get<ApiResponse<User[]>>(`${API_BASE_URL}/users`),
        axios.get<ApiResponse<Task[]>>(`${API_BASE_URL}/tasks`),
      ]);

      if (usersResponse.data.success && usersResponse.data.data) {
        setUsers(usersResponse.data.data);
      }

      if (tasksResponse.data.success && tasksResponse.data.data) {
        setTasks(tasksResponse.data.data);
      }
    } catch (err) {
      setError('データの取得に失敗しました');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // 新しいタスクを作成
  const createTask = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!newTaskTitle.trim() || !selectedUserId) {
      setError('タイトルとユーザーを選択してください');
      return;
    }

    try {
      const response = await axios.post<ApiResponse<Task>>(`${API_BASE_URL}/tasks`, {
        title: newTaskTitle,
        description: newTaskDescription,
        userId: selectedUserId,
      });

      if (response.data.success && response.data.data) {
        setTasks([...tasks, response.data.data]);
        setNewTaskTitle('');
        setNewTaskDescription('');
        setSelectedUserId(null);
        setSuccess('タスクが作成されました');
        setError('');
      }
    } catch (err) {
      setError('タスクの作成に失敗しました');
      console.error(err);
    }
  };

  // タスクの完了状態を切り替え
  const toggleTaskComplete = async (taskId: number, completed: boolean) => {
    try {
      const response = await axios.put<ApiResponse<Task>>(`${API_BASE_URL}/tasks/${taskId}`, {
        completed,
      });

      if (response.data.success && response.data.data) {
        setTasks(tasks.map(task =>
          task.id === taskId ? response.data.data! : task
        ));
      }
    } catch (err) {
      setError('タスクの更新に失敗しました');
      console.error(err);
    }
  };

  // タスクを削除
  const deleteTask = async (taskId: number) => {
    if (!window.confirm('このタスクを削除しますか？')) {
      return;
    }

    try {
      await axios.delete(`${API_BASE_URL}/tasks/${taskId}`);
      setTasks(tasks.filter(task => task.id !== taskId));
      setSuccess('タスクが削除されました');
    } catch (err) {
      setError('タスクの削除に失敗しました');
      console.error(err);
    }
  };

  // ユーザー名を取得
  const getUserName = (userId: number) => {
    const user = users.find(u => u.id === userId);
    return user ? user.name : '不明なユーザー';
  };

  useEffect(() => {
    fetchData();
  }, []);

  // 成功・エラーメッセージを自動で消去
  useEffect(() => {
    if (success) {
      const timer = setTimeout(() => setSuccess(''), 3000);
      return () => clearTimeout(timer);
    }
  }, [success]);

  useEffect(() => {
    if (error) {
      const timer = setTimeout(() => setError(''), 5000);
      return () => clearTimeout(timer);
    }
  }, [error]);

  if (loading) {
    return (
      <div className="loading">
        <h2>読み込み中...</h2>
      </div>
    );
  }

  return (
    <div>
      <header className="header">
        <div className="container">
          <h1>タスク管理アプリ</h1>
        </div>
      </header>

      <div className="container">
        {error && <div className="error">{error}</div>}
        {success && <div className="success">{success}</div>}

        {/* 新しいタスク作成フォーム */}
        <div className="card">
          <h2>新しいタスクを作成</h2>
          <form onSubmit={createTask}>
            <div className="form-group">
              <label htmlFor="taskTitle">タスクタイトル</label>
              <input
                type="text"
                id="taskTitle"
                value={newTaskTitle}
                onChange={(e) => setNewTaskTitle(e.target.value)}
                placeholder="タスクのタイトルを入力してください"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="taskDescription">説明（オプション）</label>
              <textarea
                id="taskDescription"
                value={newTaskDescription}
                onChange={(e) => setNewTaskDescription(e.target.value)}
                placeholder="タスクの詳細説明を入力してください"
              />
            </div>

            <div className="form-group">
              <label htmlFor="assignedUser">担当者</label>
              <select
                id="assignedUser"
                value={selectedUserId || ''}
                onChange={(e) => setSelectedUserId(Number(e.target.value) || null)}
                required
              >
                <option value="">担当者を選択してください</option>
                {users.map(user => (
                  <option key={user.id} value={user.id}>
                    {user.name}
                  </option>
                ))}
              </select>
            </div>

            <button type="submit" className="btn btn-primary">
              タスクを作成
            </button>
          </form>
        </div>

        {/* タスクリスト */}
        <div className="card">
          <h2>タスク一覧</h2>
          {tasks.length === 0 ? (
            <p>タスクがありません。新しいタスクを作成してください。</p>
          ) : (
            <div>
              {tasks.map(task => (
                <div
                  key={task.id}
                  className={`task-item ${task.completed ? 'completed' : ''}`}
                >
                  <input
                    type="checkbox"
                    className="task-checkbox"
                    checked={task.completed}
                    onChange={(e) => toggleTaskComplete(task.id, e.target.checked)}
                  />

                  <div className="task-content">
                    <div className="task-title">{task.title}</div>
                    {task.description && (
                      <div className="task-description">{task.description}</div>
                    )}
                    <div className="task-meta">
                      <small>
                        担当者: {getUserName(task.userId)} |
                        作成日: {formatDate(new Date(task.createdAt))}
                      </small>
                    </div>
                  </div>

                  <div className="task-actions">
                    <button
                      onClick={() => deleteTask(task.id)}
                      className="btn btn-danger"
                    >
                      削除
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* ユーザー一覧 */}
        <div className="card">
          <h2>ユーザー一覧</h2>
          {users.length === 0 ? (
            <p>ユーザーが登録されていません。</p>
          ) : (
            <ul>
              {users.map(user => (
                <li key={user.id}>
                  <strong>{user.name}</strong> ({user.email})
                  <br />
                  <small>登録日: {formatDate(new Date(user.createdAt))}</small>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;