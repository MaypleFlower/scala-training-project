package services

import models.TodoList
import models.results.{CreateTodoListResult, GetAllTodoListsResult, GetTodoListResult}
import repositories.TodoListRepository

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class TodoListService @Inject()(repository: TodoListRepository)(implicit ec: ExecutionContext) {

  def createTodoList(todoList: TodoList): Future[CreateTodoListResult] = repository.upsertTodoList(todoList).map(CreateTodoListResult(_))

  def getTodoList(id: UUID): Future[GetTodoListResult] = repository.getTodoList(id).map(GetTodoListResult(_))

  def getAllTodoLists(): Future[GetAllTodoListsResult] = repository.getAllTodoLists.map(GetAllTodoListsResult(_))

  def updateTodoList(todoList: TodoList): Future[UUID] = repository.upsertTodoList(todoList)

  def deleteTodoList(id: UUID): Future[Boolean] = repository.deleteTodoList(id)

  def deleteAllTodoLists(): Future[Boolean] = repository.deleteAllTodoLists()
}
