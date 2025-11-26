package models.api

import play.api.libs.json.{Json, OFormat}

import java.time.LocalDate

final case class UpdateTodoListRequest(task: String, description: String, dueDate: Option[LocalDate], completed: Boolean)

object UpdateTodoListRequest {
  implicit val apiTodoListFormat: OFormat[UpdateTodoListRequest] = Json.format[UpdateTodoListRequest]
}