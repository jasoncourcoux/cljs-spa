(ns cljs-spa.core    
  (:require [clojure.set :as set]
            [dommy.template :as template]))

;;;;
;;;; Holds all application state
;;;;
(def data   
  (atom { :tags {}						;; tags - allow grouping objects to apply behaviours to a group.
          :behaviours {} 			
					:features {} 				
					:instances {} }))

(defn deffeature
  "Creates a new object and stores it in the data map.
  
  This should be called with namespaced keys as the id, and include
  tags and behaviours. All objects should also have an init function to
  set up data which will be stored in the instance created.
  
  i.e.
  (object ::my-object-id
  				:tags [:tag1 :tag2]
  				:behaviours [::render]  				
  				:init (fn [id & more]
  								;;do any inital processing here, fire relavent triggers etc.
  								))"    
  [id & more]
  (swap! data assoc-in [:objects id] (apply hash-map more)))

(defn defbehaviour 
  "Creates a new behaviour and stores it in the data map.
  
  This should be called with namespaced keys as the id, and include
  triggers and a reaction fn which will get called when the trigger is
  fired.
  
  i.e.
  (behaviour ::my-behaviour-id
  					 :triggers [:tag1 :tag2]
  					 :behaviours [:click]  				
  					 :reaction (fn [instance-id & more]
  												;;perform action here - extra arguments can be passed
  												;;when firing a terigger
  												))"    
  [id & more]
  (swap! data assoc-in [:behaviours id] (apply hash-map more)) id)


(defn ^:private tag-behaviours 
  "Retreives behaviours associated with a set of tags, based on
   a supplied list of tag ids"
  [tag-keys]   
  (reduce set/union   
    (map
      (fn [tag-key]
        (if-let [tag (-> @data :tags tag-key)]
          (:behaviours tag)
          []))
      tag-keys)))

(defn ^:private object-behaviours [id]
  "Gets all behaviour ids for a given object id"
  (if-let [obj (-> @data :objects id)]
    (let [tags (:tags obj)
          behaviours (:behaviours obj)]
      (distinct (set/union (tag-behaviours tags) behaviours)))))

(defn instance-behaviours [id]
  "Gets all behaviour ids for a given instance id"
  (if-let [instance ((-> @data :instances) id)]
    (let [object-id (:object instance)
          object-bhvrs (if object-id (object-behaviours object-id) [])
          behaviours (:behaviours instance)]
      (distinct (set/union object-bhvrs behaviours)))))

(defn ^:private triggers [behaviours]
  "Gets all triggers for the list of behaviours"
  (reduce set/union
          (map (fn [b] (if-let [bhvr (-> @data :behaviours b)]
                         (:triggers bhvr)
                         [])) behaviours)))

(defn ^:private instance-trigger? [id trigger]
  (some #{trigger} (triggers (instance-behaviours id))))

(defn ^:private object-trigger? [id trigger]
  (some #{trigger} (triggers (object-behaviours id))))

(defn ^:private trigger? [behaviour-id trigger]
  (if-let [behaviour (-> @data :behaviours behaviour-id)]
    (some #{trigger} (-> behaviour :triggers))
    nil))

(defn trigger 
  "Triggers an event on an object instance, the function gets all behaviours for the instance, 
  parent object and any tags which contain the given trigger, and executes each of the 
  reactions. This uses eager evaluation to ensure that all reactions are called.
  
 	i.e.
  	(trigger ::my-obj-instance :render { :block true })
  "
  [instance-id t & args]  
  (let [behaviours (instance-behaviours instance-id)
        with-trigger (filter #(trigger? % t) behaviours)]                    
    (doseq [b with-trigger]          
      ((-> @data :behaviours b :reaction) instance-id args))))  

(defn create 
  "Creates a new instance of an object with a given id, and set of behaviours. A data map
   should be passed containing an instance specific state, that will be later used for processing
   behaviour reactions.
  
   i.e.
   			(create ::my-object-instance
  							:behaviours [ ::render ]
  							:data { :class \"some css classes\"}"
  [obj-id id & {:keys [behaviours state]}]  
  (let [obj (-> @data :objects id)]
    (if obj (do 
              (swap! data assoc-in [:instances id] {:object obj-id
                   															   :behaviours (if behaviours behaviours [])
                                               	   :data state})
							((:init obj))
              (trigger id :init)id)) nil))
    			 

  (defn property 
    "Getter/Setter function for setting properties on a specific instance."
    [instance-id prop value]   
	(if value 
    (swap! data assoc-in [:instances instance-id :data prop] value)
    ((-> @data :instances instance-id :data) prop)))
 
 
  
  (defn to-html [id contents]
    (.-outerHTML (template/node 
     [:div.feature-container { :cljsid id }
      contents])))

  (defn get-instance [id]
    (if id
    	(-> @data :instances id)
      nil))
  
  (defn get-behaviour [id]
    (-> @data :behaviours id))
  
  (defn available-behaviours [id]
    (keys (-> @data :behaviours)))
   