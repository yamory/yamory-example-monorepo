package com.example.core

import cats.syntax.all._

case class User(id: Long, name: String, email: String) {
  def isValidEmail: Boolean = email.contains("@")

  def displayName: String = s"$name ($email)"
}

object User {
  def create(id: Long, name: String, email: String): Either[String, User] = {
    val user = User(id, name, email)
    if (user.isValidEmail) {
      user.asRight
    } else {
      s"Invalid email: $email".asLeft
    }
  }
}