;;;;
;;;; Here we setup the namespace and import the jetty adapter for jetty
;;;; the core compojure library, and the route library for compojure.
;;;;
(ns cljs-spa-examples.base.server
  (:use [ring.adapter.jetty]  
        [compojure.core])
  (:require [compojure.route :as route]))


;;;;
;;;; The defroutes marco is used and we define a simple route to return "Hello world"
;;;; when the base url is requested.
;;;; 
(defroutes main-routes
  (GET "/" [] "Hello World")
  (route/files "/resources" {:root "public"}))

;;;;
;;;; Thread the request through to our defined routes, we use the '# syntax
;;;; to reference the var so evaluating changes to the handler will be immedaiately
;;;; seen in the browser.
;;;;
;;;; In future this will thread the request through to the session/parameter handlers
;;;; in ring, but for now we just reference the main-routes handler.
;;;;
(def app   
  #'main-routes)