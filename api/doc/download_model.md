**Download model**
----
  Download a model file

* **URL**

  /versions/:version/model

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
      **Content:** The file to download

      Header : `Content-Disposition:attachment; filename="model.pb"`

      Header : `Content-Type:application/octet-stream`

* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Reason:** Either model version not found or model.pb not found.

* **Sample Call:**

  `curl http://magritte-lb-941018124.eu-west-1.elb.amazonaws.com/versions/20171017/model`

* **Notes:**

  Trailing slash is optional.