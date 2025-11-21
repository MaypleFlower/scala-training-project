package models.database

import io.scalaland.chimney.dsl._
import models.TodoList
import models.results.GetAllTodoListsResult
import play.api.http.Writeable
import play.api.libs.json.{Json, OFormat}

final case class GetAllTodoListsResponse(items: List[TodoList])

object GetAllTodoListsResponse {
  implicit val writes: OFormat[GetAllTodoListsResponse] = Json.format[GetAllTodoListsResponse]
  implicit val writeable: Writeable[GetAllTodoListsResponse] = Writeable.writeableOf_JsValue
    .map(Json.toJson[GetAllTodoListsResponse])

  def fromResult(result: GetAllTodoListsResult): GetAllTodoListsResponse = result.into[GetAllTodoListsResponse].transform
}