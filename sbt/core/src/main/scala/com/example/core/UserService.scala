package com.example.core

import scala.collection.mutable

class UserService {
  private val users = mutable.Map[Long, User]()

  def addUser(user: User): Either[String, User] = {
    if (users.contains(user.id)) {
      Left(s"User with id ${user.id} already exists")
    } else {
      users += (user.id -> user)
      Right(user)
    }
  }

  def getUser(id: Long): Option[User] = users.get(id)

  def getAllUsers: List[User] = users.values.toList

  def updateUser(id: Long, name: String, email: String): Either[String, User] = {
    User.create(id, name, email).flatMap { updatedUser =>
      if (users.contains(id)) {
        users += (id -> updatedUser)
        Right(updatedUser)
      } else {
        Left(s"User with id $id not found")
      }
    }
  }

  def deleteUser(id: Long): Either[String, Unit] = {
    if (users.contains(id)) {
      users -= id
      Right(())
    } else {
      Left(s"User with id $id not found")
    }
  }
}