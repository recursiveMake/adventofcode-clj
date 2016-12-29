(ns advent.problems.code
  (:require [clojure.string :as string]))

(defn word-split
  [line]
  (filter #(not (= "" %)) (string/split line #"[-\[\]]")))

(defn letter-frequency
  [memo letter]
  (assoc memo letter (inc (memo letter 0))))

(defn count-letters
  [data]
  (reduce letter-frequency {} data))

(defn parse-room
  [room-code]
  (def room-sequence (word-split room-code))
  {
   :name (butlast (butlast room-sequence))
   :sector (Integer. (last (butlast room-sequence)))
   :checksum (last room-sequence)})

(defn real?
  [room]
  (def letter-count (count-letters (apply str (room :name))))
  (def sorted-letters (reverse (sort-by second (reverse (sort-by first (seq letter-count))))))
  (def checksum (apply str (map first (take 5 sorted-letters))))
  (= checksum (room :checksum)))

(defn shift-translate
  [letter shift-param]
  (if (= letter \-)
    \space
    (char (+ (int \a) (mod (+ shift-param (- (int letter) (int \a))) 26)))))

(defn room-translate
  [room]
  (assoc room :translation
         (apply str (map shift-translate (string/join "-" (:name room)) (repeat (:sector room))))))
