(ns together.views.welcome
  (:require [together.views.common :as common]
            [noir.content.getting-started]
            [noir.response :as response])
  (:use [noir.core :only [defpage]]
        [together.models.gatherings]))

(defpage "/welcome" []
         (common/layout
           [:p "Welcome to together"]))

(defn- gathering->html [gathering]
  [:div
   [:h2 (:time gathering)]
   [:h2 (:owner gathering)]
   [:h3 (:location gathering)]
   [:p (:description gathering)]
   [:ul (map #(vector :li %1) (:attendees gathering))]
   [:hr]])

(defpage "/gatherings" []
  (common/layout
   [:div
    [:form {:action "/newevent"}
     "Name:" [:input {:type "text" :name "owner"}]
     "When:" [:input {:type "date" :name "time"}]
     "Where:"[:input {:type "text" :name "place"}]
     "Description:"[:textarea {:placeholder "Description" :name "description"}]
     "Who:"[:input {:type "text" :name "attendees"}]
     "Capacity:"[:input {:type "number" :name "capacity" :min "1"}]
     [:input {:type "submit"}]]
    [:hr]
    (map gathering->html (list-gatherings))]))

(defpage "/newevent" {:keys [owner time place description attendees capacity]}
  (new-gathering owner description time place attendees capacity)
  (response/redirect "/gatherings"))
