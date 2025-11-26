package repositories

import models.TodoList
import org.mongodb.scala.model.Filters
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.prop.TableDrivenPropertyChecks.forAll
import org.scalatest.prop.Tables.Table
import org.scalatest.{BeforeAndAfterAll, OptionValues}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.test.DefaultPlayMongoRepositorySupport

import java.time.LocalDate
import java.util.UUID

class TodoListRepositoryItSpec
  extends AnyFreeSpecLike
    with DefaultPlayMongoRepositorySupport[TodoList]
    with ScalaFutures
    with IntegrationPatience
    with OptionValues
    with BeforeAndAfterAll {

  private val app = GuiceApplicationBuilder().overrides(
    bind[MongoComponent].toInstance(mongoComponent)
  ).build()

  override protected val repository: TodoListRepositoryImpl = app.injector.instanceOf[TodoListRepositoryImpl]

  override protected def checkTtlIndex = false

  private val todoLists = List(
    TodoList(
      UUID.fromString("d77c8455-5773-41e6-9731-3d8465b0495d"),
      "some task 1",
      "some description test 1",
      None,
      completed = false
    ),
    TodoList(
      UUID.fromString("164acf9a-a70e-4436-a304-0e63257245e6"),
      "some task 2",
      "some description test 2",
      Some(LocalDate.now()),
      completed = true
    ),
    TodoList(
      UUID.fromString("0d9a54c8-5033-4c0e-ba0e-4589c2e9c07b"),
      "some task 3",
      "some description test 3",
      None,
      completed = true
    )
  )

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    deleteAll()
    todoLists.foreach(insert)
  }

  "TodoListRepository" - {
    ".getTodoList" - {
      "should successfully return a TodoList by ID" in {
        val lists = Table("todo-lists", todoLists: _*)

        forAll(lists) { todoList =>
          repository.getTodoList(todoList.id).futureValue mustBe Some(todoList)
        }
      }
    }

    ".getAllTodoLists" - {
      "should successfully return all TodoLists in the collection" in {
        val result = repository.getAllTodoLists.futureValue.toSet

        result.size mustBe 3
        result mustBe todoLists.toSet
      }
    }

    ".upsertTodoList" - {
      "should successfully save a new TodoList" in {
        val lists = Table("todo-lists", todoLists: _*)

        forAll(lists) { todoList =>
          repository.upsertTodoList(todoList).futureValue mustBe todoList.id
        }
      }

      "should successfully update an existing TodoList" in {
        val updatedTodoLists = List(
          TodoList(
            UUID.fromString("d77c8455-5773-41e6-9731-3d8465b0495d"),
            "updated task 1",
            "updated description test 1",
            Some(LocalDate.now()),
            completed = false
          ),
          TodoList(
            UUID.fromString("164acf9a-a70e-4436-a304-0e63257245e6"),
            "updated task 2",
            "updated description test 2",
            Some(LocalDate.now()),
            completed = true
          ),
          TodoList(
            UUID.fromString("0d9a54c8-5033-4c0e-ba0e-4589c2e9c07b"),
            "updated task 3",
            "updated description test 3",
            Some(LocalDate.now()),
            completed = true
          )
        )

        val lists = Table("todo-lists", updatedTodoLists: _*)

        forAll(lists) { todoList =>
          repository.upsertTodoList(todoList).futureValue mustBe todoList.id
        }

        findAll().futureValue.toSet mustBe updatedTodoLists.toSet
      }
    }

    ".deleteTodoList" - {
      "should successfully delete a given TodoList by ID" in {
        val lists = Table("todo-lists", todoLists: _*)

        forAll(lists) { todoList =>
          repository.deleteTodoList(todoList.id).futureValue mustBe true
          find(Filters.equal("id", todoList.id.toString)).futureValue mustBe List.empty
        }
      }
    }

    ".deleteAllTodoLists" - {
      "should successfully delete all TodoLists in the collection" in {
        repository.deleteAllTodoLists().futureValue mustBe true
        findAll().futureValue mustBe List.empty
      }
    }
  }
}
