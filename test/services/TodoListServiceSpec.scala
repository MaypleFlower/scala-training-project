package services

import models.TodoList
import models.results.{CreateTodoListResult, GetAllTodoListsResult, GetTodoListResult}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import repositories.TodoListRepository

import java.time.LocalDate
import java.util.UUID
import scala.concurrent.{ExecutionContextExecutor, Future}

class TodoListServiceSpec extends AnyFreeSpecLike with MockFactory with ScalaFutures {

  private implicit val ec: ExecutionContextExecutor = scala.concurrent.ExecutionContext.global

  private val mockRepository = mock[TodoListRepository]
  private val id = UUID.fromString("2ee9141f-204d-4147-81ba-d851c42e7343")
  private val list = TodoList(id, "task", "description", Some(LocalDate.now), false)

  val underTest = new TodoListService(mockRepository)

  "TodoListService" - {
    ".createTodoList" in {
      (mockRepository.upsertTodoList(_: TodoList)).expects(list).returning(Future.successful(id))
      underTest.createTodoList(list).futureValue shouldBe CreateTodoListResult(id)
    }

    ".updateTodoList" in {
      (mockRepository.upsertTodoList(_: TodoList)).expects(list).returning(Future.successful(id))
      underTest.updateTodoList(list).futureValue shouldBe id
    }

    ".getTodoList" in {
      (mockRepository.getTodoList(_: UUID)).expects(id).returning(Future.successful(None))
      underTest.getTodoList(id).futureValue shouldBe GetTodoListResult(None)
    }

    ".getAllTodoLists" in {
      (() => mockRepository.getAllTodoLists).expects().returning(Future.successful(List.empty))
      underTest.getAllTodoLists().futureValue shouldBe GetAllTodoListsResult(List.empty)
    }

    ".deleteTodoList" in {
      (mockRepository.deleteTodoList(_: UUID)).expects(id).returning(Future.successful(true))
      underTest.deleteTodoList(id).futureValue shouldBe true
    }

    ".deleteAllTodoLists" in {
      (() => mockRepository.deleteAllTodoLists()).expects().returning(Future.successful(true))
      underTest.deleteAllTodoLists().futureValue shouldBe true
    }
  }
}
