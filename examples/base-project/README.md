Simple Example
==============

A simple example project.

To use:-

Run the following to automatically compile your clojurescript as you edit:
```bash
lein cljsbuild auto
```

Then to start the app:-
```bash
lein ring server-headless 3000
```

Navigate to http://localhost:3000/login.html

Valid logins are:
jason/password
admin/password
