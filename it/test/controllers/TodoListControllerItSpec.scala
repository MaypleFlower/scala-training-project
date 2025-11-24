package controllers

import models.TodoList
import models.api.{CreateTodoListRequest, UpdateTodoListRequest}
import models.database.{CreateTodoListResponse, GetTodoListResponse, UpdateTodoListResponse}
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsArray, Json}
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.TodoListRepositoryImpl
import services.{TodoListService, UUIDGeneratorService}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.test.DefaultPlayMongoRepositorySupport

import java.util.UUID
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContextExecutor, Future}

class TodoListControllerItSpec extends AnyFreeSpecLike with DefaultPlayMongoRepositorySupport[TodoList] {

  private val app = GuiceApplicationBuilder().overrides(
    bind[MongoComponent].toInstance(mongoComponent)
  ).build()

  override protected val repository: TodoListRepositoryImpl = app.injector.instanceOf[TodoListRepositoryImpl]

  override protected def checkTtlIndex = false

  private implicit val ec: ExecutionContextExecutor = scala.concurrent.ExecutionContext.global

  implicit private val uuidGeneratorService: UUIDGeneratorService = new UUIDGeneratorService {
    override def generate: UUID = UUID.fromString("70a39097-4830-435b-8c19-489efdd1b364")
  }

  private val todoListService: TodoListService = new TodoListService(repository)

  private val underTest = new TodoListController(stubControllerComponents(), todoListService, uuidGeneratorService)

  val id1: UUID = UUID.fromString("7db55b80-4315-4e38-9142-38e9f4a4d7cf")
  val id2: UUID = UUID.fromString("5d2847a3-bed0-4190-aa59-bfebe20fdc7c")
  val id3: UUID = UUID.fromString("bb36b10d-d64b-4d0b-9366-6d42261d7de6")
  val todoList1 = TodoList(id1, "task 1", "description 1", None, false)
  val todoList2 = TodoList(id2, "task 2", "description 2", None, true)
  val todoList3 = TodoList(id3, "task 3", "description 3", None, false)
  val todoLists = ListBuffer(todoList1, todoList2, todoList3)

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    deleteAll()
    todoLists.foreach(insert)
  }

  "TodoListController" - {
    ".getTodoList" - {
      "should successfully process a request for a TodoList given an ID" in {
        val result: Future[Result] = underTest.getTodoList(id1)(FakeRequest("GET", s"/get/$id1"))

        status(result) mustBe OK
        contentAsJson(result) mustBe Json.toJson(GetTodoListResponse(Some(todoList1)))
      }
    }

    ".getAllTodoLists" - {
      "should successfully process a request for all TodoLists in the collection" in {
        val expected = Json.toJson(todoLists).as[JsArray].value.toSet

        val result: Future[Result] = underTest.getAllTodoLists()(FakeRequest("GET", "/getAll"))

        status(result) mustBe OK
        (contentAsJson(result) \ "items").as[JsArray].value.toSet.size mustBe 3
        (contentAsJson(result) \ "items").as[JsArray].value.toSet mustBe expected
      }
    }

    ".createTodoList" - {
      "should successfully process a CreateTodoListRequest" in {
        val createRequest = CreateTodoListRequest("some task", "some description", None)
        val id = UUID.fromString("70a39097-4830-435b-8c19-489efdd1b364")

        val fakeRequest = FakeRequest("POST", "/create")
          .withBody(Json.toJson(createRequest))
          .withHeaders("Content-Type" -> "application/json")

        val result: Future[Result] = underTest.createTodoList()(fakeRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe Json.toJson(CreateTodoListResponse(id))
      }
    }

    ".updateTodoList" - {
      "should successfully process an UpdateTodoListRequest" in {
        val updateRequest = UpdateTodoListRequest("updated task", "updated description", None, false)

        val fakeRequest = FakeRequest("POST", s"/update/$id1")
          .withBody(Json.toJson(updateRequest))
          .withHeaders("Content-Type" -> "application/json")

        val result: Future[Result] = underTest.updateTodoList(id1)(fakeRequest)

        status(result) mustBe OK
        contentAsJson(result) mustBe Json.toJson(UpdateTodoListResponse(id1))
      }
    }

    ".deleteTodoList" - {
      "should successfully process a request to delete a TodoList given an ID" in {
        val result: Future[Result] = underTest.deleteTodoList(id1)(FakeRequest("DELETE", s"/delete/$id1"))

        status(result) mustBe OK
      }
    }

    ".deleteAllTodoLists" - {
      "should successfully process a request to delete all TodoLists in the collection" in {
        val result: Future[Result] = underTest.deleteAllTodoLists()(FakeRequest("DELETE", "/deleteAll"))

        status(result) mustBe OK
      }
    }
  }
}
