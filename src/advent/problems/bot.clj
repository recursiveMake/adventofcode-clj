(ns advent.problems.bot
  (:require [clojure.string :as string]
            [clojure.set :as set]))
; node
(def example-node #{0 1})

; state
(def example-state {"bot 0" #{0 1} "bot 1" #{3}})

; rules
(def example-rules {"bot 0" {:hi "output 0" :lo "bot 1"}})

(defn add-element-node
  [node value]
  (conj node value))

(defn remove-element-node
  [node value]
  (disj node value))

(defn add-element
  "Update state with new value"
  [node-label value state]
  (let [node (get state node-label #{})
        updated-node (add-element-node node value)]
    (assoc state node-label updated-node)))

(defn remove-element
  "Update state with new value"
  [node-label value state]
  (let [node (get state node-label)
        updated-node (remove-element-node node value)]
    (assoc state node-label updated-node)))

(defn move-element
  "Move a value from one bot to another"
  [from-label to-label value state]
  (add-element to-label value (remove-element from-label value state)))

(defn ready?
  "If a node has two elements, make a move"
  [node]
  (= 2 (count node)))

(defn move-node
  [node-label rules state]
  (let [[lo-val hi-val] (sort (get state node-label))
        rule (get rules node-label)]
    (move-element node-label (:lo rule) lo-val (move-element node-label (:hi rule) hi-val state))))

(defn parse-state
  "Update state with values found in text"
  [text state]
  (let [[value-text node-label] (string/split text #" goes to ")
        value (Integer. (second (string/split value-text #" ")))]
    (add-element node-label value state)))

(defn parse-rule
  "Update rules with values found in text"
  [text rules]
  (let [[node-label inst] (string/split text #" gives low to ")
        [lo-inst hi-inst] (string/split inst #" and high to ")]
    (assoc rules node-label {:hi hi-inst :lo lo-inst})))

(defn parse-text
  [text rules state]
  (let [words (string/split text #" ")]
    (if (= 6 (count words))
      ; value x goes to bot y
      [rules (parse-state text state)]
      ; bot x gives low to bot/output y and high to bot/output 2
      [(parse-rule text rules) state])))

(defn start-position
  "Take"
  [rules-state text]
  (let [rules (first rules-state)
        state (second rules-state)]
    (parse-text text rules state)))

(defn next-ready
  "Key of the next available node-label"
  [state]
  (first 
   (filter #(not (string/starts-with? % "output"))
           (keys (filter #(ready? (second %)) state)))))

