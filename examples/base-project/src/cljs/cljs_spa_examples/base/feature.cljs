(ns cljs-spa-examples.base.feature
  (:require [cljs-spa.core :as framework]
            [cljs-spa.features.logging :as log]
            [cljs-spa.features.keyboard :as kb]))

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

(framework/trigger :cljs-spa.features.keyboard/keyboard-handler :register-shortcut :target ::my-first-feature :keycode 48 :trigger (fn [] (js/alert "Done")))

