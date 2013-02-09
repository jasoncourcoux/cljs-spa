(ns cljs-spa.features.logging  
  (:require [dommy.template :as template]
            [cljs-spa.core :refer [def-behaviour def-feature create trigger]]
            [jayq.core :refer [$ bind]]))

(def-behaviour ::log
  						 :triggers [:log]
               :reaction (fn [obj {:keys [message]}]                                                               
                          (.log js/console message)))

(def-behaviour ::debug
  						 :triggers [:debug]
               :reaction (fn [obj {:keys [message]}]                                                               
                          (.log js/console message)))

(def-behaviour ::alert
  						 :triggers [:alert]
               :reaction (fn [obj {:keys [message]}]                                                               
                          (.log js/console message)))

(def-feature ::logging-handler
  					 :behaviours [::log ::debug ::alert]
  					 :init (fn []
                     (.log js/console "Starting logging feature")))

(create ::logging-handler 
        ::logging-handler
        :behaviours [] 
        :data {})

