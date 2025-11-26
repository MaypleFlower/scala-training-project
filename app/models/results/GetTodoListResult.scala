package models.results

import models.TodoList
import play.api.libs.json.{Json, OFormat}

final case class GetTodoListResult(todoList: Option[TodoList])

object GetTodoListResult {
  implicit val format: OFormat[GetTodoListResult] = Json.format[GetTodoListResult]
}
