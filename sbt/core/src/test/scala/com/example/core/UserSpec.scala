package com.example.core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class UserSpec extends AnyFlatSpec with Matchers {

  "User.create" should "create a user with valid email" in {
    val result = User.create(1L, "Alice", "alice@example.com")
    result shouldBe Right(User(1L, "Alice", "alice@example.com"))
  }

  it should "return error for invalid email" in {
    val result = User.create(1L, "Alice", "invalid-email")
    result shouldBe Left("Invalid email: invalid-email")
  }

  "User.isValidEmail" should "return true for valid email" in {
    val user = User(1L, "Alice", "alice@example.com")
    user.isValidEmail shouldBe true
  }

  it should "return false for invalid email" in {
    val user = User(1L, "Alice", "invalid-email")
    user.isValidEmail shouldBe false
  }

  "User.displayName" should "format name and email correctly" in {
    val user = User(1L, "Alice", "alice@example.com")
    user.displayName shouldBe "Alice (alice@example.com)"
  }
}