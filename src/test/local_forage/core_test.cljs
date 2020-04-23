(ns local-forage.core-test
  (:require [clojure.test :refer-macros [deftest testing is are use-fixtures]]
            [cljs.test :refer [async]]
            [cljs.core.async :as async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [local-forage.core :as core]))

(def db (atom nil))

(use-fixtures :each
  {:before #(async done
              (go
                (reset! db (core/create-db {:name "local-forage.core-test"}))
                (<p! (core/clear! @db))
                (<p! (core/set! @db "foo" "bar"))
                (done)))
   :after #(async done
             (go
               (<p! (core/drop! @db))
               (reset! db nil)
               (done)))})

(deftest protocol-test
  (testing "LocalForage"
    (is (satisfies? local-forage.core.IStorageBackend @db))))

(deftest get-test
  (testing "Get key."
    (async done
      (go
        (is (= "bar" (<p! (core/get @db "foo" nil))))
        (done))))
  (testing "Not-found."
    (async done
      (go
        (is (= "default" (<p! (core/get @db "not-found" "default"))))
        (done)))))

(deftest set-test
  (testing "Set key."
    (async done
      (go
        (<p! (core/set! @db "key" "value"))
        (is (= "value" (<p! (core/get @db "key" nil))))
        (done))))
  (testing "Set nil."
    (async done
      (go
        (<p! (core/set! @db "key" nil))
        (is (= nil (<p! (core/get @db "key" nil))))
        (done)))))

(deftest del-test
  (testing "Delete key."
    (async done
      (go
        (<p! (core/del! @db "foo"))
        (is (= "default" (<p! (core/get @db "foo" "default"))))
        (done))))
  (testing "Delete non-existing key."
    (async done
      (go
        (<p! (core/del! @db "key"))
        (is (= "default" (<p! (core/get @db "key" "default"))))
        (done)))))

(deftest clear-test
  (testing "Clear keys."
    (async done
      (go
        (<p! (core/clear! @db))
        (is (empty? (<p! (core/keys @db))))
        (done)))))

(deftest keys-test
  (testing "List keys."
    (async done
      (go
        (is (= ["foo"] (into [] (<p! (core/keys @db)))))
        (done)))))
