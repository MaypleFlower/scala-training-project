package controllers

import models.TodoList
import models.api.{CreateTodoListRequest, UpdateTodoListRequest}
import models.database.{CreateTodoListResponse, GetAllTodoListsResponse, GetTodoListResponse, UpdateTodoListResponse}
import models.results.GetTodoListResult
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.{TodoListService, UUIDGeneratorService}

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class TodoListController @Inject()(
                                    val controllerComponents: ControllerComponents,
                                    todoListService: TodoListService,
                                    uuidGenerator: UUIDGeneratorService
                                  )(implicit ec: ExecutionContext)
  extends BaseController {

  def getTodoList(id: UUID): Action[AnyContent] = Action.async{_ =>
    todoListService.getTodoList(id).map(result => Ok(GetTodoListResponse.fromResult(result)))
  }

  def getAllTodoLists(): Action[AnyContent] = Action.async{_ =>
    todoListService.getAllTodoLists().map(result => Ok(GetAllTodoListsResponse.fromResult(result)))
  }

  def createTodoList(): Action[CreateTodoListRequest] = Action.async(parse.json[CreateTodoListRequest]) { implicit request =>
    val list = TodoList(request.body)(uuidGenerator)
    todoListService.createTodoList(list).map(result => Ok(CreateTodoListResponse.fromResult(result)))
  }

  def updateTodoList(id: UUID): Action[UpdateTodoListRequest] = Action.async(parse.json[UpdateTodoListRequest]) { implicit request =>
    val list = TodoList(id, request.body)
    todoListService.updateTodoList(list).map(id => Ok(UpdateTodoListResponse(id)))
  }

  def deleteTodoList(id: UUID): Action[AnyContent] = Action.async {_ =>
    todoListService.deleteTodoList(id).map(success => Ok(Json.obj("success" -> success)))
  }

  def deleteAllTodoLists() = Action.async {_ =>
    todoListService.deleteAllTodoLists().map(success => Ok(Json.obj("success" -> success)))
  }
}
