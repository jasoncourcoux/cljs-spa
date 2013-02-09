(ns cljs-spa-examples.base.header
  (:use [jayq.core :only [$ append delegate data bind inner]])
  (:require [cljs-spa.core :refer [def-behaviour def-feature create trigger]]
            [cljs-spa-examples.base.menu :as menu]
            [dommy.template :as template]))

(defn header-ui
  []
  (template/node 
   [:header.spa-header
    [:hgroup.spa-header-text
     [:h1 "CLJS-SPA Example"]
     [:h4 "A single page web application"]]
    [:div.spa-header-menu
     [:span "Logged in as Jason Courcoux"]
     [:a {:href "/logout"} "Logout"]]
    [:nav.spa-main-menu#spa-main-menu]]))     

(def-behaviour ::render
  						 :triggers [:render]
               :reaction (fn [obj {:keys [parent]}]                               		
                                 (if-let [p parent]                                   
                                   (do 
                                     (inner p (.-outerHTML (header-ui)))
                                     (trigger :cljs-spa-examples.base.menu/menu :render :parent ($ :#spa-main-menu))  )                                   
                                   (.log js/console (str "No parent element set for obj id " obj " so cannot render")))))

(def-feature ::header  					
             :triggers []
             :behaviours [::render]
             :init (fn [] (.log js/console "Starting header...")))

(create ::header
        ::header
        :behaviours [] 
        :data {})