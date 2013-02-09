(ns cljs-spa.internal    
  (:require [clojure.set :as set]
            [dommy.template :as template]))

(def app-data   
  (atom { :tags {}
          :behaviours {} 			
          :features {} 				
          :instances {} }))

(defn return [v]
  (fn [s] [v s]))

(defn bind [a f]
  (fn [s]
    (let [[v newstate] (a s)]
      ((f v) newstate))))

(defn update-application-data [action]
  (bind action (fn [value]
                 (fn [state]
                   (if-let [d (:data state)]
                     (do                       
                       (swap! app-data merge @app-data d)
                       [value state])
                     [nil state])))))

(defn create-type [typekey id args state-map]  
  (if (and typekey id args)
    (assoc-in state-map [:data typekey id] (apply hash-map args))))

(defn create-type-bind [a type-key]
  (bind a (fn [v]
            (fn [s]              
              (if-let [newstate (create-type type-key (:id v) (:args v) s)]
                [v newstate]
                [nil nil])))))
               

 (defn add-feature-to-state [a]
  (bind a (fn [v]
            (fn [s]              
              (let [feature-id (:feature v)]
                (if-let [feature (-> s :data :features feature-id)]
                  [v (assoc-in s [:feature] feature)]
                  [nil nil]))))))

(defn init-instance [a]
  (bind a (fn [v]
            (fn [s]              
              (if-let [init (-> s :feature :init)]
                (init)) [(merge v {:trigger :init}) s]))))   

(defn add-feature-behaviours-to-state [a]
  (bind a (fn [v]
            (fn [s]
              (let [trigger-key (:trigger v)
                    instance-id (:id v)
                    behaviours (:behaviours s)]                
                (if (and trigger-key instance-id)
                  (if-let [feature (-> s :data :instances instance-id :feature)]
                    [v (assoc-in s [:behaviours] (distinct (set/union behaviours (-> s :data :features feature :behaviours))))])
                  [nil nil]))))))
              
(defn add-instance-behaviours-to-state [a]
  (bind a (fn [v]
            (fn [s]
              (let [trigger-key (:trigger v)
                    instance-id (:id v)
                    behaviours (:behaviours s)]                    
                (if (and trigger-key instance-id)
                  (if-let [instance (-> s :data :instances instance-id)]
                    [v (assoc-in s [:behaviours] (distinct (set/union behaviours (:behaviours instance) behaviours)))])
                  [nil nil]))))))

(defn add-to-state [a new-state]
  (bind a (fn [v]
            (fn [s]                                                  
              [v (merge s new-state)]))))

(defn fire-behaviours [a args]
  (bind a (fn [v] 
            (fn [s]              
              (let [behaviours-ids (:behaviours s)]
                (doseq [id behaviours-ids]                  
                  (if-let [behaviour (-> s :data :behaviours id)]
                    (if (and 
                         (:trigger v)
                         (some #{(:trigger v)} (:triggers behaviour)))
                      ((:reaction behaviour) (:id v) (:args v))))))
              [v s])))) 
  
  