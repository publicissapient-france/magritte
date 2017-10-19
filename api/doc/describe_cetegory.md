**Describe category**
----
  Describe a category within a model

* **URL**

  /versions/:version/categories/:category

* **Method:**

  `GET`

*  **URL Params**

   **Required:**

   `version=[string]` The model version

   `category=[string]` The category to describe

   **Optional:**

   None

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:**
```json
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
}
```


* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Reason:** Either model version does not exists or category does not
    exists for this model version

* **Sample Call:**

  `curl http://magritte-lb-941018124.eu-west-1.elb.amazonaws.com/versions/20171017/categories/fruit`

* **Notes:**

  Trailing slash is optional