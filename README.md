# Vocalist Repertoire

Vocalist Repertoire is an easy-to-use system that allows multiple vocalists to store their complete repertoire with song details, composer information, learning/performed status, and additional notes/links.

## Built With

* [IntelliJ IDEA Ultimate](https://www.jetbrains.com/idea/download/#section=mac)
* [Java SDK 11](https://www.oracle.com/java/technologies/javase-downloads.html)
* [Maven](https://maven.apache.org/)
* [Spring Boot 2](https://spring.io/projects/spring-boot)
* [Spring Security](https://spring.io/projects/spring-security)
* [MySQL Server 8](https://dev.mysql.com/downloads/mysql/)
* [Postman](https://www.postman.com/) - Used for feature testing
* [Swagger 2](https://swagger.io/) - Documentation API

## Features

### User Functionality

* Upon sign up, users are stored with encrypted passwords and given access to features using Basic Auth through Spring Security (todo: upgrade to JWT)
* In order to more easily add song/composer info to their repertoire, users can search what has already been added to the database where results are paginated for scalability using Spring Data Pages. If their specific song/composer is not present, user can add their own entry.
* Users have their own repertoire list of songs and their composers, which they can search through in various ways according to their need - song language, musical epoch, and status.

### Additional Entity Attributes - Songs/Composers/Notes

* Composers are matched to their compositions through relational mapping with Hibernate when added to the database.
* Songs include details that can be searched through a database search or used as a filter for searching a user's own repertoire.
* For each song in the user's repertoire, they can add personal notes and a learning/performed status to that they can later filter their repertoire by.

### Data Transfer Objects

* DTOs are used for all data being transferred to the front-end layer in order to supply only the necessary data

### Error Handling

* Implemented custom error handling to provide a custom message when errors are encountered - specific entities not found, username already exists, etc.

## Operations

Once the repo has been downloaded and the application is running, the Swagger UI can be accessed at http://localhost:8080/swagger-ui.html.

Below are sample JSONs for POST actions to run through Swagger.

### Create a User

`POST` `/api/user`
```json
{
   "fach":"SOPRANO",
   "username":"testUser",
   "password":"testPassword",
   "confirmPassword":"testPassword"
}
```

### Add New Composer

`POST` `/api/composer/add`
```json
{
    "name": "Giacomo Puccini",
    "birthDate": "1858-12-22",
    "deathDate": "1924-11-29",
    "epoch": "LATE_ROMANTIC"
}
```

### Add New Song With Existing Composer

`POST` `/api/song/add`
```json
{
    "title": "Porgi amor qualche ristoro",
    "composer": {
        "id": "1"
        },
    "containingWork": "Le nozze di Figaro",
    "duration": "4 minutes",
    "language": "ITALIAN",
    "type": "ARIA"
}
```

## Testing

All testing is run with code coverage to achieve a goal of at least 80%. This project uses Mockito and JUnit5 as its testing libraries.

## ToDo List

- [x] Make results pageable
- [ ] Upgrade Basic Auth to JWT
- [ ] Add logging features
- [ ] Unit testing for database access layer
- [ ] Collaborate with UI engineer to add front-end layer
- [ ] Host project on a domain

## Motivation

As a vocalist myself, I remember struggling to create the repertoire part of my artistic resume. I would have to search through pages of songs and type up titles, composers, if I performed the song, etc. This (painful) process inspired me to create something that can store all this information for me so that when I needed it, I could easily get all the information necessary. Using my app, I can now easily show people which songs I can sing in Italian, how many arias I know, and which songs by Mozart I have performed.

I plan to continue building upon this project and eventually release it to help other vocalists who may be struggling with keeping tabs on the vast array of songs in their repertoire.
