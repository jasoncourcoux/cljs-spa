(ns cljs-spa.core      
  (:require [clojure.set :as set]            
            [dommy.template :as template]
            [cljs-spa.internal :refer [return create-type-bind update-application-data app-data add-feature-to-state
                                       init-instance add-feature-behaviours-to-state add-instance-behaviours-to-state
                                       fire-behaviours add-instance-state]]))


(defn def-feature [id & more]
  "Defines a new feature and stores it in the application state map.
  
  returns the id if successful, nil if not."
  (let [[v state] ((-> (return {:id id :args more})
                       (create-type-bind :features)
                        update-application-data) { :data @app-data })]
    (:id v)))

(defn def-behaviour [id & more]
  (let [[v state] ((-> (return {:id id :args more})
                       (create-type-bind :behaviours)
                        update-application-data) { :data @app-data })]
    (:id v)))

(defn create [feature id & more]
  (let [args (concat more (list :feature feature))
        [v s] ((-> (return {:id id :feature feature :args args})
                   add-feature-to-state
                   (create-type-bind :instances)
                   update-application-data
                   init-instance       
                   add-feature-behaviours-to-state
                   add-instance-behaviours-to-state
                   (fire-behaviours :init)
                   ) {:data @app-data})]      
    (:id v)))    

(defn trigger [instance-id trigger-key & args]
  (let [[v s] ((-> (return {:id instance-id :trigger trigger-key :args args}) ;;Initial value containing trigger details
                   add-feature-behaviours-to-state
                   add-instance-behaviours-to-state
                   (fire-behaviours trigger-key)) {:data @app-data})] ;;pass application data as state
    s)) 

(defn add-state [instance-id newstate]
  (let [[v s] ((-> (return {:id instance-id :data newstate })
                   add-instance-state
                   update-application-data) { :data @app-data })]
    (:id v)))

(defn get-property [instance-id property-key]
  (-> @app-data :instances instance-id :data property-key))

 ; (defn property 
 ;   "Getter/Setter function for setting properties on a specific instance."
 ;   [instance-id prop value]   
;	(if value 
;    (swap! data assoc-in [:instances instance-id :data prop] value)
;    ((-> @data :instances instance-id :data) prop))) 
; 
  
;  (defn to-html [id contents]
;    (.-outerHTML (template/node 
;     [:div.feature-container { :cljsid id }
;      contents])))

  ;(defn get-instance [id]
  ;  (if id
  ;  	(-> @data :instances id)
  ;    nil))
  
 ; (defn get-behaviour [id]
 ;   (-> @data :behaviours id))
  
 ; (defn available-behaviours [id]
 ;   (keys (-> @data :behaviours)))
   