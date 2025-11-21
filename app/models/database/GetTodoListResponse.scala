package models.database

import io.scalaland.chimney.dsl._
import models.TodoList
import models.results.GetTodoListResult
import play.api.http.Writeable
import play.api.libs.json.{Json, OFormat}

final case class GetTodoListResponse(todoList: Option[TodoList])

object GetTodoListResponse {
  implicit val writes: OFormat[GetTodoListResponse] = Json.format[GetTodoListResponse]
  implicit val writeable: Writeable[GetTodoListResponse] = Writeable.writeableOf_JsValue
    .map(Json.toJson[GetTodoListResponse])

  def fromResult(result: GetTodoListResult): GetTodoListResponse = result.into[GetTodoListResponse].transform
}