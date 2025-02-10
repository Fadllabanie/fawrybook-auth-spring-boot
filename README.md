# Fawrybook

Fawrybook is a platform that allows users to write, edit, and delete technical blog posts. Users can interact with posts by liking, disliking, and commenting on them. Additionally, users can share their posts as tweets via their Twitter account.

## Features

- **User Authentication**: Register, log in, and manage user profiles.
- **Blog Post Management**: Create, edit, and delete blog posts.
- **Interactions**: Like, dislike, and comment on posts.
- **Post Ratings**: View the average rating of each post.
- **Twitter Integration**: Share blog posts as tweets directly from the platform.
- **Responsive Design**: Built with Angular 16+ for a seamless user experience.

---

## Technologies Used

### Backend
- **Java 17**
- **Spring Boot 3.4.2**
- **Spring Data JPA** for database operations.
- **Spring Security** for authentication and authorization.
- **JWT** for secure token-based authentication.
- **MySQL** as the database.
- **Lombok** for reducing boilerplate code.
- **Maven** for dependency management.

### Frontend
- **Angular 16+** for building the user interface.
- **Angular Material** for UI components.
- **RxJS** for reactive programming.
- **HTTP Client** for API communication.

### Twitter Integration
- **Twitter API** for sharing posts as tweets.
- **OAuth 1.0a** for user authentication with Twitter.

---

## Prerequisites

Before running the project, ensure you have the following installed:

1. **Java Development Kit (JDK) 17**
2. **Node.js** (for Angular)
3. **Angular CLI** (`npm install -g @angular/cli`)
4. **MySQL** (or any preferred database)
5. **Maven** (for backend dependency management)

---

## Setup Instructions

### Backend (Spring Boot)

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/Fawrybook.git
   cd Fawrybook/backend
   ```
   
   2. **Configure the Database**
      1. Update the application.properties file with your MySQL database credentials:

           ```bash
         spring.datasource.url=jdbc:mysql://localhost:3306/fawrybook
         spring.datasource.username=root
         spring.datasource.password=yourpassword
         spring.jpa.hibernate.ddl-auto=update
         ```

      
   3. **Run the Application:**
 ```bash
   mvn clean install
mvn spring-boot:run
   ```

   The backend will start at http://localhost:8080.


## Twitter Integration
Create a Twitter Developer Account:

Go to the Twitter Developer Portal and create a new app.

Obtain the API Key, API Secret Key, Access Token, and Access Token Secret.

Configure Twitter Credentials:

Update the application.properties file with your Twitter credentials:


```bash
twitter.api.key=your_api_key
twitter.api.secret=your_api_secret
twitter.oauth.token=your_access_token
twitter.oauth.secret=your_access_token_secret
```
3. Implement Twitter Sharing:

Use the Twitter API to share posts as tweets. Example:

### API Endpoints

Authentication
POST /api/auth/signup: Register a new user.

POST /api/auth/login: Authenticate a user and return a JWT token.

Blog Posts
GET /api/posts: Get all posts (with pagination and sorting).

GET /api/posts/{id}: Get a specific post by ID.

POST /api/posts: Create a new post.

PUT /api/posts/{id}: Update an existing post.

DELETE /api/posts/{id}: Delete a post.

Interactions
POST /api/posts/{id}/like: Like a post.

POST /api/posts/{id}/dislike: Dislike a post.

POST /api/posts/{id}/comment: Add a comment to a post.

Twitter Integration
POST /api/posts/{id}/tweet: Share a post as a tweet.


### UML Diagrams



### unit test 

````bash
mvn -Dtest=LoginControllerTest test
mvn -Dtest=AuthControllerTest test

````