package models

import models.api.{CreateTodoListRequest, UpdateTodoListRequest}
import org.scalamock.scalatest.MockFactory
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import services.UUIDGeneratorService

import java.util.UUID

class TodoListSpec extends AnyFreeSpecLike with MockFactory {

  implicit val mockUuidGeneratorService: UUIDGeneratorService = mock[UUIDGeneratorService]
  private val createRequest: CreateTodoListRequest = new CreateTodoListRequest("task", "description", None)
  private val updateRequest: UpdateTodoListRequest = new UpdateTodoListRequest("task", "description", None, false)
  private val id: UUID = UUID.fromString("dc8badd4-c8ae-4480-be7e-23bfc295fcd4")
  private val list: TodoList = new TodoList(id, "task", "description", None, false)

  "TodoList" - {
    ".apply" - {
      "should create a TodoList from a CreateTodoListRequest" in {
        (() => mockUuidGeneratorService.generate).expects().returning(id)
        TodoList.apply(createRequest) shouldBe list
      }

      "should create a TodoList from an ID and an UpdateTodoListRequest" in {
        TodoList.apply(id, updateRequest) shouldBe list
      }
    }
  }
}
