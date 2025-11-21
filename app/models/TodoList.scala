package models

import io.scalaland.chimney.dsl.TransformationOps
import models.api.{CreateTodoListRequest, UpdateTodoListRequest}
import play.api.libs.json.{Json, OFormat}
import services.UUIDGeneratorService

import java.time.LocalDate
import java.util.UUID

final case class TodoList(id: UUID, task: String, description: String, dueDate: Option[LocalDate], completed: Boolean)

object TodoList {

  def apply(request: CreateTodoListRequest)(implicit uuidGeneratorService: UUIDGeneratorService): TodoList =
    request.into[TodoList].withFieldConst(_.id, uuidGeneratorService.generate).withFieldConst(_.completed, false).transform

  def apply(id: UUID, request: UpdateTodoListRequest): TodoList =
    request.into[TodoList].withFieldConst(_.id, id).transform

  implicit val todoListFormat: OFormat[TodoList] = Json.format[TodoList]
}