package config

import com.google.inject.AbstractModule
import repositories.{TodoListRepository, TodoListRepositoryImpl}

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[TodoListRepositoryImpl]).asEagerSingleton()
    bind(classOf[TodoListRepository])
      .to(classOf[TodoListRepositoryImpl])
      .asEagerSingleton()
  }
}