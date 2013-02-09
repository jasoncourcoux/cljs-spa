(ns cljs-spa-examples.base.header
  (:use [jayq.core :only [$ append delegate data bind inner]])
  (:require [cljs-spa.core :refer [def-behaviour def-feature create trigger]]
            [cljs-spa-examples.base.menu :as menu]
            [goog.net.XhrIo :as ajax]
            [dommy.template :as template]))

(defn header-ui
  [user]
  (template/node 
   [:header.spa-header
    [:hgroup.spa-header-text
     [:h1 "CLJS-SPA Example"]
     [:h4 "A single page web application"]]
    [:div.spa-header-menu
     [:span (str "Logged in as: " user)]
     [:a {:href "/logout"} "Logout"]]
    [:nav.spa-main-menu#spa-main-menu]]))     

(defn render-header [parent reply]
    (let [v (js->clj (.getResponseJson (.-target reply)))] ;v is a Clojure data structure      
      (inner parent (.-outerHTML (header-ui ("user" v))))
      (trigger :cljs-spa-examples.base.menu/menu :render :parent ($ :#spa-main-menu) :menu-items ("menu" v))))

(def-behaviour ::render
  						 :triggers [:render]
               :reaction (fn [obj {:keys [parent]}]                               		
                                 (if-let [p parent]                                   
                                   (do 
                                     (.send goog.net.XhrIo "/shell/header" (partial render-header p))                                                                                                                                                       )                                   
                                   (.log js/console (str "No parent element set for obj id " obj " so cannot render")))))

(def-feature ::header  					
             :triggers []
             :behaviours [::render]
             :init (fn [] (.log js/console "Starting header...")))

(create ::header
        ::header
        :behaviours [] 
        :data {})