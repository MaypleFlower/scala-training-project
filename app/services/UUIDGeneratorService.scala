package services

import java.util.UUID
import javax.inject.Singleton

trait UuidGenerator {
  def generate: UUID
}

@Singleton
class UUIDGeneratorService extends UuidGenerator{
  def generate: UUID = UUID.randomUUID()
}
