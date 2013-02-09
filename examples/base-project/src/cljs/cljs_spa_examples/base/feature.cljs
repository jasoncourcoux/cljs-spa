(ns cljs-spa-examples.base.feature
  (:use [jayq.core :only [$ append delegate data bind inner]])
  (:require [cljs-spa.core :as framework]
            [cljs-spa.features.logging :as log]
            [cljs-spa.features.keyboard :as kb]
            [cljs-spa-examples.base.header :as header]))

(framework/def-behaviour ::render
                        :triggers [:render]
                        :reaction (fn [] (.log js/console "Rendering...")))

(framework/def-feature ::my-first-feature
                      :behaviours [::render]
                      :init (fn [] (.log js/console "Initing...")))

(framework/create ::my-first-feature
                  ::my-first-feature
                  :behaviours []
                  :state {})

(framework/trigger ::my-first-feature :render)

(framework/trigger :cljs-spa-examples.base.header/header :render :parent ($ :#content))  

(framework/trigger :cljs-spa.features.keyboard/keyboard-handler :register-shortcut :target ::my-first-feature :keycode 48 :trigger (fn [] (js/alert "Done")))