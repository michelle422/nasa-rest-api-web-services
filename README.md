# nasa-rest-api-web-services

This is a REST API service that handles queries from NASA Asteroids API. The queriess that can be don are the following:
- get all asteroids that are near objects to Earth from user input start date and end date
- get the 10 nearest asteroids within the start date-end date range
- get the largest asteroid within the start date-end date range

As of upload time, this REST API does not have data persistence. Data is presently stored in JSON objects and then in a list. An immediate improvement to data storage is to accept each query immediately in the database such as MySQL. In future enhancements, we can put the data in a commit log-based data persistence, because this provides durability. It will ensure that the data is committed and safe even if an interruption or an outage happens. 
