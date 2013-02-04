cljs-spa
========
A small client side framework for creating single page web applications in clojurescript based on features inspired by ideas in the excellent talk here: http://www.youtube.com/watch?v=V1Eu9vZaDYw and the book: http://www.manning.com/mikowski/

See the wiki for a guide on getting started - https://github.com/jasoncourcoux/cljs-spa/wiki/Getting-Started-Guide

Overview
--------
The core of cljs-spa is a simple framework to help manage application state in a single clojure map, and a way for features to interact with the state and one another without having to be dependant upon each other. This allows a very pluggable model, allowing devlopment of standalone components which can be reused across multiple applications.

Features
--------
A feature is a standalone component which can be plugged in to a cljs-spa web application. At a minimum they should contain a namespaced id, a set of behaviours and a function which knows how to initialise itself.

An example feature:-
```clojure
(use 'cljs-spa.core)

(deffeature ::my-feature-id
            :behaviours [::render ::update]
            :init (fn [obj & data]
                    ;;We'll do something with the data here and return it
                    data))
```

Behaviours
----------
Behaviours define what a feature can do and what functionality they perform - an example might be a "render" behaviour which allows a feature to create and insert itself at a particular point in the page structure. These should contain a namespaced id, a set of triggers and a reaction function.

```clojure
(use 'cljs-spa.core)

(defbehaviour ::my-behaviour-id
              :triggers [:render :click]
              :reaction (fn [instance-id {:keys [key-one key-two]}]
                          ;; We should validate the data, and perform an action, the
                          ;; id is passed through as an indication of the specific instance
                          ;; of this feature, to allow multiple feature instances to co-exist
                          instance-id))                          
```

Instances
---------
To actually use a feature an instance must be created, containing a unique namepsaced id (Note, for singletons, it is perfectly valid to use the same id for both a feature and an instance of it). Behaviours specific to this feature maybe added, and any custom state should be passed into the state map.

```clojure
(use 'cljs-spa.core)

(create ::feature-id
        ::instance-id
        ::behaviours []
        ::state {})
```        

License
-------
Copyright © 2012-2013 Jason Courcoux
Distributed under the Eclipse Public License, the same as Clojure.
