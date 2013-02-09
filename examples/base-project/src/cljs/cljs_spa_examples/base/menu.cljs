(ns cljs-spa-examples.base.menu
  (:use [jayq.core :only [$ append delegate data bind inner]])
  (:require [cljs-spa.core :refer [def-behaviour def-feature create trigger]]
            [dommy.template :as template]
            [goog.net.XhrIo :as ajax]))

(defn menu-item-ui [item]
  [:li.spa-menu-item
     [:a {:href "#"} ("name" item)]])

(defn menu-ui
  [items]
  (template/node    
   [:ul
    (for [item items]
      (menu-item-ui item))]))

(def-behaviour ::render
  						 :triggers [:render]
               :reaction (fn [obj {:keys [parent menu-items]}]                               		
                                 (if-let [p parent] 
																	 (inner parent (.-outerHTML (menu-ui menu-items)))
                                   (.log js/console (str "No parent element set for obj id " obj " so cannot render")))))

(def-feature ::menu  					
             :triggers []
             :behaviours [::render]
             :init (fn [] (.log js/console "Starting menu...")))

(create ::menu
        ::menu
        :behaviours [] 
        :data {})