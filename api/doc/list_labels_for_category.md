**List labels**
----
  List all labels available for a category

* **URL**

  /versions/:version/labels?category=:category

* **Method:**

  `GET`

*  **URL Params**

   **Required:**

   `version=[string]` The model version

   `category=[string]` The category to list labels on

* **Header Params**

  **Required:**

  None

  **Optional:**

  `accept: mime` The format to retrieve content.
  Supported are
    * `application/json`
    * `application/octet-stream`

  Default is `application/json`

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:**
```json
[
  {
    "index": 0,
    "value": "kiwi"
  },
  {
    "index": 1,
    "value": "grape"
  },
  {
    "index": 2,
    "value": "apple"
  }
]
```

   OR

  * **Code:** 200 <br />
    **Content:** The file to download

    Header : `Content-Disposition:attachment; filename="labels_category.txt"`

    Header : `Content-Type:application/octet-stream`


* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Reason:** Either model version does not exists or label file does not
    exists for this category in this model version

* **Sample Call:**

  `curl --header "accept: application/octet-stream" http://magritte-lb-941018124.eu-west-1.elb.amazonaws.com/versions/20171017/labels?category=fruit`

* **Notes:**

  Trailing slash is optional.