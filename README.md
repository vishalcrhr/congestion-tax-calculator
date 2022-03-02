# congestion-tax-calculator

- Initial commit is pushed which is of version 1.0 and does not contain externalization of static and constant files.
- Major Bug and Use case is taken care which was missing and also detailed in the code itself through comments.
- Use case of Multiple day tax addition in total fee was missing. So, its adjusted and fixed into the code.
- Also mentioned the queries in questions.md file.


Procedure to run the api:

- Clone the project and run the application via normal java run.
- Hit the url "http://localhost:8081/congestion/tax/v1/calculator" with the below request body:

Request:
{
    "vehicle_type": "Car",
    "dates": [
        "2013-01-14 21:00:00","2013-01-15 21:00:00",
        "2013-02-07 06:23:27","2013-02-07 15:27:00",
        "2013-02-08 06:27:00","2013-02-08 06:20:27",
        "2013-02-08 14:35:00","2013-02-08 15:29:00",
        "2013-02-08 15:47:00","2013-02-08 16:01:00",
        "2013-02-08 16:48:00","2013-02-08 17:49:00",
        "2013-02-08 18:29:00","2013-02-08 18:35:00",
        "2013-03-26 14:25:00","2013-03-28 14:07:27"
    ]
}

Response:
{
    "error": "NA",
    "tax": 97,
    "message": "Tax for the Vehicle :Car Amount : 97",
    "timestamp": 1646170579492
}

