package models.database

import play.api.http.Writeable
import play.api.libs.json.{Json, OFormat}

import java.util.UUID

final case class UpdateTodoListResponse(id: UUID, updated: Boolean)

object UpdateTodoListResponse {
  implicit val writes: OFormat[UpdateTodoListResponse] = Json.format[UpdateTodoListResponse]
  implicit val writeable: Writeable[UpdateTodoListResponse] = Writeable.writeableOf_JsValue
    .map(Json.toJson[UpdateTodoListResponse])

  def apply(id: UUID): UpdateTodoListResponse = UpdateTodoListResponse(id, updated = true)
}