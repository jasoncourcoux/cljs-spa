;;;;
;;;; Here we setup the namespace and import the jetty adapter for jetty
;;;; the core compojure library, and the route library for compojure.
;;;;
(ns cljs-spa-examples.base.server
  (:use [ring.adapter.jetty]  
        [ring.middleware.params]        
        [ring.middleware.session]        
        [ring.middleware.keyword-params]        
        [compojure.core])
  (:require [compojure.route :as route]
            [cljs-spa-examples.base.login :as login]
            [clojure.data.json :as json]))

(defroutes app-routes
  (GET "/menu" [] (json/write-str [{:name "Home"} 
                                   {:name "Admin"}]))
  (route/files "/app" {:root "private"})
  (GET "/test" [] "Your logged in"))
  

;;;;
;;;; The defroutes marco is used and we define a simple route to return "Hello world"
;;;; when the base url is requested.
;;;; 
(defroutes main-routes  
  (ANY "/logout" {session :session} (login/logout session))
  (POST "/login" {{user :user password :password} :params session :session} (login/login session user password))  
  (route/files "/resources" {:root "public"})
  (login/wrap-login app-routes)) 

;;;;
;;;; Thread the request through to our defined routes, we use the '# syntax
;;;; to reference the var so evaluating changes to the handler will be immedaiately
;;;; seen in the browser.
;;;;
;;;; In future this will thread the request through to the session/parameter handlers
;;;; in ring, but for now we just reference the main-routes handler.
;;;;
(def app   
  (-> #'main-routes ring.middleware.stacktrace/wrap-stacktrace wrap-keyword-params wrap-params wrap-session))