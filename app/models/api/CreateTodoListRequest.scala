package models.api

import play.api.libs.json.{Json, OFormat}

import java.time.LocalDate

final case class CreateTodoListRequest(task: String, description: String, dueDate: Option[LocalDate])

object CreateTodoListRequest {
  implicit val apiTodoListFormat: OFormat[CreateTodoListRequest] = Json.format[CreateTodoListRequest]
}