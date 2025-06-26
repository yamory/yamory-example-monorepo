package com.example.main

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.example.api.UserRoutes
import com.example.core.{User, UserService}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Main extends App {
  implicit val system: ActorSystem = ActorSystem("user-service")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  // Initialize services
  val userService = new UserService()
  val userRoutes = new UserRoutes(userService)

  // Add some sample data
  val sampleUsers = List(
    User.create(1L, "Alice", "alice@example.com"),
    User.create(2L, "Bob", "bob@example.com"),
    User.create(3L, "Charlie", "charlie@example.com")
  )

  sampleUsers.foreach {
    case Right(user) => userService.addUser(user)
    case Left(error) => println(s"Failed to add sample user: $error")
  }

  // Define routes
  val routes = concat(
    pathPrefix("api") {
      userRoutes.routes
    },
    path("health") {
      get {
        complete("OK")
      }
    }
  )

  val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)

  println(s"Server online at http://localhost:8080/")
  println("Available endpoints:")
  println("  GET    http://localhost:8080/health")
  println("  GET    http://localhost:8080/api/users")
  println("  GET    http://localhost:8080/api/users/{id}")
  println("  POST   http://localhost:8080/api/users")
  println("  PUT    http://localhost:8080/api/users/{id}")
  println("  DELETE http://localhost:8080/api/users/{id}")
  println("Press RETURN to stop...")

  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}