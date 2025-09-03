# My Vue App

This is a Vue.js application that implements a frontend with login, logout, and permission control functionalities. The application interacts with specified backend APIs to manage user authentication and authorization.

## Project Structure

- **public/index.html**: Main HTML file serving as the entry point for the Vue application.
- **src/assets**: Directory for static assets such as images, fonts, and stylesheets.
- **src/components**:
  - **Login.vue**: Component for user login, including a form and methods for handling login requests.
  - **Logout.vue**: Component for user logout, including a method to handle logout.
  - **PermissionControl.vue**: Component for managing user permissions and displaying content based on user roles.
- **src/router/index.js**: Sets up Vue Router with routes for Home, Login, and Dashboard views.
- **src/store/index.js**: Sets up Vuex for state management, managing authentication state and user permissions.
- **src/views**:
  - **Home.vue**: Landing page of the application.
  - **Login.vue**: View displaying the Login component and handling the login process.
  - **Dashboard.vue**: View displaying user-specific content based on permissions.
- **src/App.vue**: Root component of the Vue application, including the router-view.
- **src/main.js**: Entry point of the Vue application, initializing the Vue instance and mounting the app.
- **package.json**: Configuration file for npm, listing dependencies and scripts.
- **babel.config.js**: Configuration file for Babel, specifying presets and plugins for transpiling code.

## Installation

To install the project dependencies, run:

```
npm install
```

## Running the Application

To start the development server, use:

```
npm run serve
```

The application will be available at `http://localhost:8080`.

## Features

- User authentication with login and logout functionalities.
- Role-based permission control to manage user access to different parts of the application.
- Responsive design for a seamless user experience.

## License

This project is licensed under the MIT License.