package fr.xebia.model

import java.io.File
import java.nio.file.{Files, Path}
import java.util.stream.Collectors

import scala.collection.JavaConversions._

class Models(path: String) {
  // "src/test/resources/models"
  def listAll(): List[Model] = {
    val listInDir: List[Path] = Files.list(new File(path).toPath).collect(Collectors.toList()).toList

    listInDir.map(path =>
      Model(path.toFile)
    )
  }
}