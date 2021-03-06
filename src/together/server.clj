(ns together.server
  (:require [monger.core :as mc]
            [noir.server :as server]))

(mc/connect!)

(mc/set-db! (mc/get-db "together"))

(server/load-views-ns 'together.views)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode mode
                        :ns 'together})))
