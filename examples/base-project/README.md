Simple Example
==============

A simple example project containing a feature which logs to the console when it is created and then triggers a behaviour which logs another message.

To use:-

Run the following to automatically compile your clojurescript as you edit:
```bash
lein cljsbuild auto
```

Then to start the app:-
```bash
lein ring server-headless 3000
```

Navigate to http://localhost:3000/resources/index.html and check the console.
