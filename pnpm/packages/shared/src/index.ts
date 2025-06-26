// 共通の型定義
export interface User {
  id: number;
  name: string;
  email: string;
  createdAt: Date;
}

export interface Task {
  id: number;
  title: string;
  description: string;
  completed: boolean;
  userId: number;
  createdAt: Date;
}

// 共通のユーティリティ関数
export function formatDate(date: Date): string {
  return date.toLocaleDateString("ja-JP", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });
}

export function validateEmail(email: string): boolean {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

export function generateId(): number {
  return Math.floor(Math.random() * 1000000);
}

// API レスポンスの型
export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  error?: string;
}

export function createSuccessResponse<T>(data: T): ApiResponse<T> {
  return {
    success: true,
    data,
  };
}

export function createErrorResponse(error: string): ApiResponse<never> {
  return {
    success: false,
    error,
  };
}
