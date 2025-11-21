package models.results

import models.TodoList
import play.api.libs.json.{Json, OFormat}

final case class GetAllTodoListsResult(items: Seq[TodoList])

object GetAllTodoListsResult {
  implicit val format: OFormat[GetTodoListResult] = Json.format[GetTodoListResult]
}
