(ns together.models.gatherings
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]
            [monger.core :as mc]
            [monger.collection :as mongo]))

(defn- parse-emails [s]
  (filter
   noir.validation/is-email?
   (clojure.string/split s #"[, \t;]")))

(defn- send-notifications [owner_address emails description]
  (http/post
   "https://mandrillapp.com/api/1.0//messages/send.json"
   {:body
    (json/write-str
     {:key "4ACrJHO7RIZ63wwLN6QRxA"
      :message
       {:text (str "You've been invited to this great event: " description)
        :subject "You're invited!"
        :from_email owner_address
        :to (map #(hash-map :email % :type "to") emails)}})}))

(defn new-gathering [owner description time place attendees capacity]
  (let [emails (parse-emails attendees)]
    (mongo/insert "gatherings"
                  {:owner owner
                   :description description
                   :time time
                   :place place
                   :attendees emails
                   :capacity capacity})
    @(send-notifications owner emails description)))

(defn availability [{:keys [capacity attendees]}]
  (- capacity (count attendees)))

(defn list-gatherings []
  (mongo/find-maps "gatherings"))
