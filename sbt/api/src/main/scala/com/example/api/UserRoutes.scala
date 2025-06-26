package com.example.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.json._
import com.example.core.{User, UserService}

class UserRoutes(userService: UserService) {

  // JSON formatters
  implicit val userFormat: RootJsonFormat[User] = jsonFormat3(User.apply)

  case class CreateUserRequest(name: String, email: String)
  implicit val createUserRequestFormat: RootJsonFormat[CreateUserRequest] = jsonFormat2(CreateUserRequest)

  case class UpdateUserRequest(name: String, email: String)
  implicit val updateUserRequestFormat: RootJsonFormat[UpdateUserRequest] = jsonFormat2(UpdateUserRequest)

  val routes: Route =
    pathPrefix("users") {
      concat(
        // GET /users
        get {
          pathEnd {
            complete(userService.getAllUsers)
          }
        },
        // GET /users/{id}
        get {
          path(LongNumber) { id =>
            userService.getUser(id) match {
              case Some(user) => complete(user)
              case None => complete(StatusCodes.NotFound, s"User with id $id not found")
            }
          }
        },
        // POST /users
        post {
          pathEnd {
            entity(as[CreateUserRequest]) { request =>
              val id = System.currentTimeMillis() // Simple ID generation
              User.create(id, request.name, request.email) match {
                case Right(user) =>
                  userService.addUser(user) match {
                    case Right(addedUser) => complete(StatusCodes.Created, addedUser)
                    case Left(error) => complete(StatusCodes.Conflict, error)
                  }
                case Left(error) => complete(StatusCodes.BadRequest, error)
              }
            }
          }
        },
        // PUT /users/{id}
        put {
          path(LongNumber) { id =>
            entity(as[UpdateUserRequest]) { request =>
              userService.updateUser(id, request.name, request.email) match {
                case Right(user) => complete(user)
                case Left(error) => complete(StatusCodes.NotFound, error)
              }
            }
          }
        },
        // DELETE /users/{id}
        delete {
          path(LongNumber) { id =>
            userService.deleteUser(id) match {
              case Right(_) => complete(StatusCodes.NoContent)
              case Left(error) => complete(StatusCodes.NotFound, error)
            }
          }
        }
      )
    }
}