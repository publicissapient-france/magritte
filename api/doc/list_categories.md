**List Categories**
----
  List available categories for a model

* **URL**

  /versions/:version/categories

* **Method:**

  `GET`

*  **URL Params**

   **Required:**

   `version=[string]` the model version

   **Optional:**

   None

* **Data Params**

   None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:**
```json
[
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
```

* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Reason:** Model version not found

* **Sample Call:**

  `curl http://magritte-lb-941018124.eu-west-1.elb.amazonaws.com/versions/20171017/categories`

* **Notes:**

  Trailing slash is optional.