(ns user
  (:require [mount.core :as mount]
            zhaw-weng-shop-api.core))

(defn start []
  (mount/start-without #'zhaw-weng-shop-api.core/repl-server))

(defn stop []
  (mount/stop-except #'zhaw-weng-shop-api.core/repl-server))

(defn restart []
  (stop)
  (start))


