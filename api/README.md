# Scala API

## Build

```
sbt test # run tests
sbt docker:publishLocal # build image in local repository
```

Run in local mode :
* Set env var : S3_LOCAL and S3_LOCAL_BUCKET
* Run test class `fr.xebia.LocalMagritteAPI`

## Manual deploy on S3

Create a folder with your version and add following files:

- `model.pb`: set `Content-Type` as `application/octet-stream`
- `model.json`: check current exiting versions for example, define your categories inside it
- `labels_${category_name}.txt`: label file for your category