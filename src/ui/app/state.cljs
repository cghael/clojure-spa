(ns ui.app.state
  (:require [reagent.core :as r]))


(def *config (r/atom {:uri nil
                      :limit nil}))

(def *activ-patient (r/atom nil))

(def *page (r/atom 1))

(def *patients (r/atom {:current-page []
                        :next-page []
                        :previous-page []}))

(def *search-filer (r/atom nil))

(def *alert (r/atom nil))
