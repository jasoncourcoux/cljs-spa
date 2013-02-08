(defproject cljs-examples-basic "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/clj"]  ;; Set the clojure source path so we can have seperate cljs and clj sources
  :plugins [[lein-cljsbuild "0.2.10"]  ;; Add cljsbuild plugin
             [lein-ring "0.8.2"]]  ;; Add ring plugin

  :hooks [leiningen.cljsbuild]  ;; Add the lein cljsbuild hook

  :ring {:handler cljs-spa-examples.base.server/app} ;; Tell ring where to find your handler (Note this won't 
                                             ;; exist at the minute)
  ;; Configure cljsbuild - 
  ;; Full options example here: https://github.com/emezeske/lein-cljsbuild/blob/master/sample.project.clj
  ;; For now, we set some basic compiler options to produce javascript which is easier to 
  ;; step through and the src path to our clojure script files.
  :cljsbuild {
              :builds {
                       :main {
                              :source-path "src/cljs"
                              :compiler {
                                         :output-to "public/js/main.js"
                                         :optimizations :simple
                                         :pretty-print true
                                         }
                              }
                       }
              }
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.2.0-SNAPSHOT"] ;; Routing library for ring
                 [ring "1.1.8"] ;; Web application library
                 [jayq "2.0.0"] ;;JQuery wrapper
                 [cljs-spa "0.1.0-SNAPSHOT"]
                 [prismatic/dommy "0.0.1"]] ;; Clojurescript templating library
 ) 
;  :profiles { :dev { :dependencies [[cljs-spa-dev "0.1.0-SNAPSHOT"]]}})