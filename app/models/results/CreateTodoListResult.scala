package models.results

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

final case class CreateTodoListResult(id: UUID)

object CreateTodoListResult {
  implicit val writes: OFormat[CreateTodoListResult] = Json.format[CreateTodoListResult]
}
