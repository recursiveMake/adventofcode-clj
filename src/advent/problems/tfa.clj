(ns advent.problems.tfa
  (:require [clojure.string :as string]))

(defn screen
  "Create new map to store a screen"
  [w h]
  (hash-map :width w :height h :pixels (into [] (repeat (* w h) 0))))

(defn screen-indices
  "Helper function to generate indices"
  [scrn]
  (range 0 (* (:width scrn) (:height scrn))))

(defn update-screen
  [scrn pixels]
  (assoc scrn :pixels pixels))

(defn update-pixels
  [pixels replacement-pairs]
  (apply assoc pixels replacement-pairs))


(defn rect
  [scrn a b]
  (def rect-indices 
    (flatten 
     (take b (partition a (:width scrn) (screen-indices scrn)))))
  (update-screen scrn (update-pixels (:pixels scrn) (interleave rect-indices (repeat 1)))))

(defn rotate-row
  [scrn y n]
  (def start (* (:width scrn) y))
  (def row-indices (range start (+ start (:width scrn))))
  (def row-values (map nth (repeat (scrn :pixels)) row-indices))
  (def shifted-values (concat (take-last n row-values) (take (- (:width scrn) n) row-values)))
  (update-screen scrn (update-pixels (:pixels scrn) (interleave row-indices shifted-values))))

(defn rotate-col
  [scrn x n]
  (def col-indices (range x (* (:width scrn) (:height scrn)) (:width scrn)))
  (def col-values (map nth (repeat (scrn :pixels)) col-indices))
  (def shifted-values (concat (take-last n col-values) (take (- (:height scrn) n) col-values)))
  (update-screen scrn (update-pixels (:pixels scrn) (interleave col-indices shifted-values))))

(defn parse-line
  [line]
  (let [fragments (string/split line #" ")
        params (re-seq #"\d+" line)
        param-1 (Integer. (first params))
        param-2 (Integer. (last params))]
    (case (first fragments)
      "rect" #(rect % param-1 param-2)
      "rotate" (case (second fragments)
                 "row" #(rotate-row % param-1 param-2)
                 "column" #(rotate-col % param-1 param-2)))))

(defn dot
  [c]
  (case c
    0 " "
    1 "*"))

(defn print-dot
  [sequence]
  (println (apply str (map dot sequence))))

(defn draw
  [scrn]
  (count (map print-dot (partition (:width scrn) (:pixels scrn)))))
