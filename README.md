# Notes
This is a project that I've started in 2022, for a interview, as discussed, I've left the user
listing functionality as it was, and added the new functionality for displaying the contact
details based on the provided requirements document.

## The user listing functionality
- Uses `public-api` instead of `public/v2/` (public-api contains some extra metadata)
- Each user items displays a timer, representing the time since the user was created. But
  since
  there's no `createdAt` coming from the API, I've just used the app start time instead.
- Long tap on a user to show the Delete confirmation
- Tap on the FAB to show the Add user Dialog
- Both `Delete` and `Add User` functionalities are designed to be decoupled from the User
  listing
  screen and could be reused in other parts of the app
- Tech stack
    - Retrofit + OkHttp + Moshi
    - RxJava
    - MVI + Compose
## The contact details functionality
- Uses `public-api` instead of `public/v2/` (public-api contains some extra metadata)
- Implements all the design and functional requirements stated in the requirements document
- Tech stack
    - Retrofit + OkHttp + Moshi
    - Coroutines + Flow
    - MVVM + Compose

# Setup and build
- I've used Android Studio Meerkat Feature Drop | 2024.3.2
- Just import and run the app

# Trade-offs
The dependencies are mostly outdated, and I tried to do the best without updating them. After 3
years, it would have taken a lot of time figuring out the compatible dependencies and adapt the
breaking changes in the code.

##Example:
For `compose-navigation`, the version I've used doesn't support type safe navigation,
making it very hard to pass the user info (from list to detail). In order to make it work, I've
introduced an `UsersInMemCache` that stores the users and provides a way to get the user details,
based on the userId. The good outcome, though is that I was able to showcase the approach of
handling data from  multiple sources in the `UserDetailsViewModel`.

# Overall fulfilled requirements
- Language: Kotlin ✅
- UI: Jetpack Compose ✅
- Architecture: MVVM and MVI ✅
- Networking: Retrofit + OkHttp ✅
- Async: Coroutines and RxJava ✅
- Dependency injection: Hilt ✅
- Image Loading: Glide ✅
- Responsive UI – supports multiple screen sizes ✅
- Loading states – show a loading indicator during API calls ✅
- Error handling – gracefully handle API failures or no internet ✅
- Extra: Dark theme ✅