package fr.xebia.model

class Models(implicit s3Client: S3Model) {

  def listAll(): List[Model] = {
    s3Client
      .listModelVersion()
      .map(version => {
        val objects = s3Client.listObjectForModel(version)
        Model(version, objects)
      })
      .filter(_.isDefined)
      .map(_.get)
  }

}