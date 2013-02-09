(ns cljs-spa-examples.base.login
  (:require [compojure.route :as route]
            [ring.util.response :as response]))

(defn get-user
  [request]
  (-> request :session :user))

(get-user {:session {:user "Jason"}})

(defn valid-login? [username password]
  (and (= username "admin") (= password "password")))

(defn login [session user password]  
  (if (valid-login? user password)
  	(assoc (response/redirect "/app/index.html") :session (assoc session :user user))
    (response/redirect "/resources/login.html")))

(defn logged-in? [request] 
  (if (get-user request) true false))

(defn wrap-login [handler]
  (fn [request]
    (if (logged-in? request) 
      (handler request)
      (response/redirect "/resources/login.html"))))	

(defn logout [session]
  (assoc (response/redirect "/resources/login.html") :session (dissoc session :user)))