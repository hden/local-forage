(ns local-forage.core
  (:refer-clojure :exclude [keys count get -count])
  (:require ["localforage" :as LocalForage]
            [cognitect.transit :as t]))

(defprotocol IStorageBackend
  (-get [db key not-found])
  (-set! [db key val])
  (-del! [db key])
  (-clear! [db])
  (-drop! [db])
  (-count [db])
  (-keys [db]))

(defrecord StorageBackend [db reader writer]
  IStorageBackend
  (-get [_ key not-found]
    (let [key (t/write writer key)]
      (-> (.getItem db key)
          (.then (fn [x]
                   (let [x (t/read reader x)]
                     (if (nil? x)
                       not-found
                       x)))))))

  (-set! [_ key val]
    (let [key (t/write writer key)
          val (t/write writer val)]
      (.setItem db key val)))

  (-del! [_ key]
    (let [key (t/write writer key)]
      (.removeItem db key)))

  (-clear! [_]
    (.clear db))

  (-drop! [_]
    (.dropInstance db))

  (-count [_]
    (.length db))

  (-keys [_]
    (-> (.keys db)
        (.then (fn [array]
                 (map #(t/read reader %)
                      (js->clj array)))))))

(defn get
  "Gets an item from the storage library and supplies the result."
  ([db key]
   (-get db key nil))
  ([db key not-found]
   (-get db key not-found)))

(defn set!
  "Saves data to an offline store."
  [db key val]
  (-set! db key val))

(defn del!
  "Removes the value of a key from the offline store."
  [db key]
  (-del! db key))

(defn clear!
  "Removes every key from the database, returning it to a blank slate."
  [db]
  (-clear! db))

(defn drop!
  [db]
  (-drop! db))

(defn count
  "Gets the number of keys in the offline store."
  [db]
  (-count db))

(defn keys
  "Get the list of all keys in the datastore."
  [db]
  (-keys db))

(def default-reader (t/reader :json))
(def default-writer (t/writer :json))

(defn create-db
  ([options] (create-db options default-reader default-writer))
  ([options reader writer]
   (let [db (LocalForage/createInstance (clj->js options))]
     (->StorageBackend db reader writer))))
