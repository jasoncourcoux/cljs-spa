(ns cljs-spa.features.keyboard  
  (:require [dommy.template :as template]
            [cljs-spa.core :refer [def-behaviour def-feature create trigger add-state get-property]]
            [jayq.core :refer [$ bind]]))

(def $body ($ :body))

(def-behaviour ::register-shortcut
  :description ""
  :triggers [:register-shortcut]
  :reaction (fn [obj {:keys [target keycode trigger]}]
                 (.log js/console "Registering shortcut")
              (add-state obj {(keyword keycode) (fn [] (trigger))})))

(def-behaviour ::handle-keypress
  						 :triggers [:keypress]
               :reaction (fn [obj {:keys [event]}]  
                           (let [keycode (str (.-keyCode event))]
                             (.log js/console (str "Got keyboard event with keycode: " keycode))
                          (if-let [trigger (get-property obj (keyword keycode))]
                            (trigger)))))

(def-feature ::keyboard-handler
  					 :behaviours [::register-shortcut ::handle-keypress]
  					 :init (fn []
                     (bind $body :keypress (fn [e]
                                             (trigger ::keyboard-handler :keypress :event e)))))

(create ::keyboard-handler 
        ::keyboard-handler
        :behaviours [] 
        :data {})

;(def $body ($ :body))

;(defbehaviour ::register-shortcut
  						:description "Registers a new keyboard shortcut, and maps it to a trigger"
;;  						:expects { :target "The target jQuery element to bind the keycode to" 
;                         :keycode "The keycode to bind"
;                         :trigger "The function to be called in response to a keycode" }
;  		        :triggers [:register-shortcut]
;              :reaction (fn [obj {:keys [target keycode trigger]}]
;                          (let []                                 
;                            (property obj keycode (fn [] (trigger))))))

;(defbehaviour ::handle-keypress
;              :triggers [:keypress]
;              :reaction (fn [obj {:keys [event]}]                                                               
;                          (if-let [trigger (object/property obj (.-keyCode event) nil)]
;                            (trigger))))

;(deffeature ::keyboard-handler
;  					:triggers [:register-shortcut]
;  					:behaviours [::register-shortcut ::handle-keypress]
;            :init (fn []
;                    (do 
;                      (bind $body :keypress (fn [e] (trigger ::keyboard-handler :keypress :event e) )))))

;(create ::keyboard-handler 
;        ::keyboard-handler
;        :behaviours [] 
;        :data {})

;(defn shortcut [keycode reaction] 
;  (trigger ::keyboard-handler 
;           :register-shortcut 
;           :target $body 
;           :keycode keycode 
;           :trigger reaction)
;  (.log js/console (str "Registered " keycode)))

;((keyword 190) (-> @data :instances ::keyboard-handler  :data))