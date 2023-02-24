# PracticeApp
This mobile client is based on [Zulip API](https://zulip.com/api).
 The implementation took place in several stages:
- Creating a **customView** and **customViewGroup**
- Writing your own **Flexbox Layout** 
- Creating a chat screen using **RecyclerView** with the ability to leave reactions to messages in **BottomSheetDialog** 
- Create a screen with **Fragments** of streams, list of contacts, user profile data using **BottomNavigationView (Navigation Architecture Component)**.
- Implementation of asynchronous calls to the network / database with stub data using **RxJava**, searching for messages, working with **DiffUtils**.
- Requests to the network via **OkHttp+Retrofit**.
- Implementation of caching of streams, topics, messages in the **Room** database, using **Pagination** to download messages from the network.
- Implementation of **Clean+MVI architecture (with ViewModel)**.
- Implementation of dependency injection with **Dagger2**

The application implements:
- CustomView
- Navigation Architecture Component
- Fragment
- ViewModel
- ViewBinding
- Room
- OkHttp
- Retrofit
- Glide
- RxJava
- MVI+Clean
- Dagger2
## Preview
![test1](https://user-images.githubusercontent.com/7962882/221133086-307029ba-9661-4165-a487-188934fe1f5c.gif)
![test2](https://user-images.githubusercontent.com/7962882/221133117-6468096a-1bea-4030-bf3a-a575d1b56d3b.gif)
