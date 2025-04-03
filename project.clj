(defproject com.github.hden/local-forage "0.2.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojurescript "1.11.132" :scope "provided"]
                 [org.clojure/core.async "1.8.735"]
                 [com.cognitect/transit-cljs "0.8.280"]]
  :repl-options {:init-ns local-forage.core})
