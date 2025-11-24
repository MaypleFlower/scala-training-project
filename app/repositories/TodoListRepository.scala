package repositories

import models.TodoList
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model._
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import java.time.LocalDate
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

trait TodoListRepository {
  def getTodoList(id: UUID): Future[Option[TodoList]]

  def getAllTodoLists: Future[Seq[TodoList]]

  def upsertTodoList(todoList: TodoList): Future[UUID]

  def deleteTodoList(id: UUID): Future[Boolean]

  def deleteAllTodoLists(): Future[Boolean]
}

@Singleton
class TodoListRepositoryImpl @Inject()(mongoComponent: MongoComponent)(implicit ex: ExecutionContext) extends PlayMongoRepository[TodoList](
  collectionName = "todo-lists",
  mongoComponent = mongoComponent,
  domainFormat = TodoList.todoListFormat,
  indexes = Seq(
    IndexModel(
      Indexes.ascending("id"),
      IndexOptions()
        .name("id")
        .unique(true)
        .background(false)
        .expireAfter(5000, TimeUnit.SECONDS)
    )
  ),
  replaceIndexes = true
)
  with TodoListRepository {

  def getTodoList(id: UUID): Future[Option[TodoList]] = {
    collection.find(Filters.equal("id", id.toString)).headOption
  }

  def getAllTodoLists: Future[Seq[TodoList]] = {
    collection.find(BsonDocument()).toFuture().map(_.toSeq)
  }

  def upsertTodoList(todoList: TodoList): Future[UUID] = {
    val updates = Updates.combine(
      Updates.set("id", todoList.id.toString),
      Updates.set("task", todoList.task),
      Updates.set("description", todoList.description),
      Updates.set("dueDate", todoList.dueDate.map(_.toString).getOrElse(LocalDate.now().toString)),
      Updates.set("complete", todoList.completed)
    )

    collection
      .findOneAndUpdate(
        Filters.equal("id", todoList.id.toString),
        updates,
        FindOneAndUpdateOptions().upsert(true))
      .toFuture().map(_ => todoList.id)
  }

  def deleteTodoList(id: UUID): Future[Boolean] = {
    collection.deleteOne(Filters.equal("id", id.toString)).toFuture().map(_.wasAcknowledged)
  }

  def deleteAllTodoLists(): Future[Boolean] = {
    collection.deleteMany(BsonDocument()).toFuture().map(_.wasAcknowledged)
  }

}
