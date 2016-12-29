(ns advent.problems.triangle
   (:require [clojure.java.io :as io]
             [clojure.string :as string]))

(defn not-valid?
  "Tests if side c fails triangle inequality"
  [a b c]
  (not (> (+ a b) c)))

(defn permute
  "Rearranges triangle sides for all possible c values"
  [a b c]
  (list (list a b c) (list a c b) (list b c a)))

(defn check-valid?
  "Tests if triangle is valid"
  [a b c]
  (= nil (first (filter #(apply not-valid? %) (permute a b c)))))

(defn line-to-integer
  "Given a string line, parses and casts line as a list of integers"
  [line]
  (map #(Integer. %) (filter #(not (= "" %)) (string/split line #" "))))
