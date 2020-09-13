Crawler
===

To send request:
1. sbt run 
2. request example
```
   curl --location --request POST 'http://localhost:8080/titles' \
     --header 'Content-Type: application/json' \
     --data-raw '{
         "urlList": ["https://on.pleer.ru", "https://yandex.ru", "http://rrr.ru"]
     }'
```
