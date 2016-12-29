(ns advent.problems.bathroom)

(defn to-columns
  "Convert rows to colums"
  [keypad]
  (apply map vector keypad))

(defn get-row
  "Return row containing key or nil"
  [keypad key]
  (nth keypad
       (.indexOf 
        (map #(.contains % key) keypad)
        true)
       []))

(defn move
  "Move on a keypad"
  [keypad key direction]
  (def row (get-row keypad key))
  (def new-pos (nth row (+ (.indexOf row key) direction) key))
  (if (= nil new-pos)
    key
    new-pos))

(defn up
  "Move up on a keypad"
  [keypad key]
  (move (to-columns keypad) key -1))

(defn down
  "Move down on a keypad"
  [keypad key]
  (move (to-columns keypad) key 1))

(defn left
  "Move left on a keypad"
  [keypad key]
  (move keypad key -1))

(defn right
  "Move right on a keypad"
  [keypad key]
  (move keypad key 1))

(def standard-keypad
  [
   [1 2 3]
   [4 5 6]
   [7 8 9]])

(def alternate-keypad 
  [
   [nil nil 1 nil nil] 
   [nil 2 3 4 nil] 
   [5 6 7 8 9] 
   [nil "A" "B" "C" nil] 
   [nil nil "D" nil nil]])

(defn move-line
  [start-position moves keypad]
  (def move-funcs (hash-map
                   :U #(up keypad %)
                   :D #(down keypad %)
                   :L #(left keypad %)
                   :R #(right keypad %)))
  (reduce
   #((move-funcs %2) %1)
   start-position
   (map #(keyword (str %)) (seq moves))))
