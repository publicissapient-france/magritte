**Describe model**
----
  Describe model content, based on model.json file available on the storage area.

* **URL**

  /versions/:id/data

* **Method:**

  `GET`

*  **URL Params**

   **Required:**

   `id=[string]` The model ID. Should exist in [list versions](./show_versions)

   **Optional:**

   None

* **Header Params**

  None

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:**
```json
{
  "version": 20171017,
  "params": {
    "INPUT_SIZE": 224,
    "IMAGE_MEAN": 224,
    "IMAGE_STD": 224,
    "INPUT_NAME": "input"
  },
  "created_at": "2017/10/17 23:08:10",
  "categories": [
    {
      "name": "fruit",
      "thumbnailurl": "http://placeholder.xebia.fr",
      "classes": {
        "apple": {
          "thumbnailurl": "http://placeholder.xebia.fr",
          "translations": [
            {
              "lang": "fr",
              "value": "pomme"
            },
            {
              "lang": "en",
              "value": "apple"
            }
          ]
        },
        "banana": {
          "thumbnailurl": "http://placeholder.xebia.fr",
          "translations": [
            {
              "lang": "fr",
              "value": "banane"
            },
            {
              "lang": "en",
              "value": "banana"
            }
          ]
        }
      }
    },
    {
      "name": "vegetable",
      "thumbnailurl": "http://placeholder.xebia.fr",
      "classes": {}
    }
  ]
}
```

* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Content:** None

* **Sample Call:**

  `curl http://magritte-lb-941018124.eu-west-1.elb.amazonaws.com/versions/20171017/data`

* **Notes:**

  Trailing slash is optional.