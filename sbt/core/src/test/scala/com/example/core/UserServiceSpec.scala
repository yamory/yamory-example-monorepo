package com.example.core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class UserServiceSpec extends AnyFlatSpec with Matchers {

  "UserService.addUser" should "add a new user successfully" in {
    val service = new UserService()
    val user = User(1L, "Alice", "alice@example.com")

    val result = service.addUser(user)
    result shouldBe Right(user)
    service.getUser(1L) shouldBe Some(user)
  }

  it should "return error when adding duplicate user" in {
    val service = new UserService()
    val user = User(1L, "Alice", "alice@example.com")

    service.addUser(user)
    val result = service.addUser(user)
    result shouldBe Left("User with id 1 already exists")
  }

  "UserService.getUser" should "return existing user" in {
    val service = new UserService()
    val user = User(1L, "Alice", "alice@example.com")
    service.addUser(user)

    service.getUser(1L) shouldBe Some(user)
  }

  it should "return None for non-existing user" in {
    val service = new UserService()
    service.getUser(999L) shouldBe None
  }

  "UserService.updateUser" should "update existing user" in {
    val service = new UserService()
    val user = User(1L, "Alice", "alice@example.com")
    service.addUser(user)

    val result = service.updateUser(1L, "Alice Updated", "alice.updated@example.com")
    result shouldBe Right(User(1L, "Alice Updated", "alice.updated@example.com"))
    service.getUser(1L) shouldBe Some(User(1L, "Alice Updated", "alice.updated@example.com"))
  }

  it should "return error for non-existing user" in {
    val service = new UserService()
    val result = service.updateUser(999L, "Alice", "alice@example.com")
    result shouldBe Left("User with id 999 not found")
  }

  "UserService.deleteUser" should "delete existing user" in {
    val service = new UserService()
    val user = User(1L, "Alice", "alice@example.com")
    service.addUser(user)

    val result = service.deleteUser(1L)
    result shouldBe Right(())
    service.getUser(1L) shouldBe None
  }

  it should "return error for non-existing user" in {
    val service = new UserService()
    val result = service.deleteUser(999L)
    result shouldBe Left("User with id 999 not found")
  }

  "UserService.getAllUsers" should "return all users" in {
    val service = new UserService()
    val user1 = User(1L, "Alice", "alice@example.com")
    val user2 = User(2L, "Bob", "bob@example.com")

    service.addUser(user1)
    service.addUser(user2)

    val users = service.getAllUsers
    users should contain allOf(user1, user2)
    users should have size 2
  }
}