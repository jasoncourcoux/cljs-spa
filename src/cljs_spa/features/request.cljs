(ns cljs-spa.features.request  
  (:require [cljs-spa.core :refer [def-behaviour def-feature create trigger]]
            [goog.net.XhrIo :as ajax]))


;(defn callback [success args reply]
;  (.log js/console (str reply))
;    (let [v (js->clj (.getResponseJson (.-target reply)))] ;v is a Clojure data structure      
;      (success reply args)))      

(defn callback [success reply]
  (let [v (js->clj (.getResponseJson (.-target reply)))] ;v is a Clojure data structure      
    (.log js/console (str v))
    (success v)))

(def-behaviour ::get
  						 :triggers [:get]
               :reaction (fn [obj {:keys [url success args]}]                                                               
                           (.send goog.net.XhrIo url (partial callback (partial success args)))))

(def-feature ::request
  					 :behaviours [::get]
  					 :init (fn []
                     (.log js/console "Starting request feature")))

(create ::request
        ::request
        :behaviours [] 
        :data {})
