(ns zhaw-weng-shop-api.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[zhaw_weng_api started successfully]=-"))
   :middleware identity})
