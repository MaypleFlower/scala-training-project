package models.database
import io.scalaland.chimney.dsl._
import models.results.CreateTodoListResult
import play.api.http.Writeable
import play.api.libs.json.{Json, OFormat}

import java.util.UUID

final case class CreateTodoListResponse(id: UUID)

object CreateTodoListResponse {
  implicit val writes: OFormat[CreateTodoListResponse] = Json.format[CreateTodoListResponse]
  implicit val writeable: Writeable[CreateTodoListResponse] = Writeable.writeableOf_JsValue
    .map(Json.toJson[CreateTodoListResponse])

  def fromResult(result: CreateTodoListResult): CreateTodoListResponse = result.into[CreateTodoListResponse].transform
}