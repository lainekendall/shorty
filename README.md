# Shorty
Shorty is an API that shortens urls. It does this by String hashing the url and saving this all in an in-memory
database. The reason I chose an in-memory database over a database like MySql was to reduce complexity and maximize
developer workflow speed. This API is built on top of [Java Spring](https://spring.io) including Persistence
RestController, and tests.

## Setup
### To run
In order to run this API locally on your machine run `mvn spring-boot:run` on the command line.
This will start the app on port `8000` locally. you can then visit https://localhost:8000 to see the
index page of the api.

### To test
There are unit tests written for each route of the controller. In order to run the tests you can run
`mvn test`.

## Controller Routes
### Create
If you would like to add a short link to Shorty's database go to http://localhost:8000/create?url=google.com.
This action will create a short link and save it to the in-memory database. Click the link to get redirected to your
url.

### Redirect
To get redirected to your newly created link, visit localhost:8000/myHash. This will redirect you to your original
url.

### Custom
If you would like to enter custom urls to be redirected, you can by visiting localhost:8000/custom?url=google.com&custom=my-custom-link.

## To Do
Unfortunately because of the roughly 4 hour time constraint on this project, I couldn't complete several things
I would have liked to. Here are improvements that can be made:
- Use Mysql instead of an in memory database to mimic a production environment
- Change the data model to allow redirection from custom short links. Currently only redirects are possible
from autogenerated hash values and this is because of how the data model is represented. Hash being the key we search on,
we should combine the concept of hash and custom so that we can have many custom values mapped to a single
url and all of them will get redirected.

## Design
I decided to use [Spring Boot](https://spring.io/projects/spring-boot) in order to get this up and running quickly. Spring Boot is a great
framework that allows you to run quality apps quickly. The customization is endless but the "Autowiring" of components makes it really
easy to write a REST API. I'm also familiar with this framework and enjoy working with it a lot. The database is an in-memory [Hibernate](https://hibernate.org)
database. This was chosen for ease of use and fast development.
