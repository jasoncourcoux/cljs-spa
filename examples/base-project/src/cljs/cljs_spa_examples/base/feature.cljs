(ns cljs-spa-examples.base.feature
  (:require [cljs-spa.core :as framework]))

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