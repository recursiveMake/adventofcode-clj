(ns advent.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [advent.io :refer [read-file]]
            [advent.problems.bathroom :as bathroom]
            [advent.problems.triangle :as triangle]
            [advent.problems.code :as code]
            [advent.problems.hash :as hash])
  (:gen-class))

(defn problem-1
  []
  nil)

(defn problem-2-a
  []
  (loop [lines (read-file "resources/problem-2.txt")
         start-position 5
         result []]
    (def new-position (bathroom/move-line start-position (first lines) bathroom/standard-keypad))
    (if (= 0 (count lines))
      result
      (recur (rest lines)
             new-position
             (conj result new-position)))))

(defn problem-2-b
  []
  (loop [lines (read-file "resources/problem-2.txt")
         start-position 5
         result []]
    (def new-position (bathroom/move-line start-position (first lines) bathroom/alternate-keypad))
    (if (= 0 (count lines))
      result
      (recur (rest lines)
             new-position
             (conj result new-position)))))

(defn problem-3-a
  []
  (count (filter #(apply triangle/check-valid? %) 
                 (map triangle/line-to-integer (read-file "resources/problem-3.txt")))))

(defn problem-3-b
  []
  (count (filter #(apply triangle/check-valid? %)
                 (partition 3 (flatten (bathroom/to-columns 
                                        (map triangle/line-to-integer (read-file "resources/problem-3.txt"))))))))

(defn problem-4-a
  []
  (reduce + (map :sector (filter code/real? (map code/parse-room (read-file "resources/problem-4.txt"))))))

(defn problem-4-b
  []
  (def valid-rooms (filter code/real? (map code/parse-room (read-file "resources/problem-4.txt"))))
  (filter #(.contains (:translation %) "orth") (map code/room-translate valid-rooms)))

(defn problem-5-a
  []
  (def door-id (first (read-file "resources/problem-5.txt")))
  (apply str (map #(nth % 5) (take 8 (filter hash/is-interesting? (pmap hash/hash-string (map str (repeat door-id) (range))))))))

(defn problem-5-b
 []
  (def door-id (first (read-file "resources/problem-5.txt")))
  (def password (hash/new-password-promise))
  (doall (take-while hash/not-finished?  
                     (map hash/save (repeat password) 
                          (filter #(hash/is-new? password %) 
                                  (filter hash/is-valid? 
                                          (filter hash/is-interesting? 
                                                  (pmap hash/hash-string (map str (repeat door-id) (range)))))))))
  (apply str (map deref password)))

(defn problem-6-a
  []
  (apply str (map #(key (apply max-key val %)) (map code/count-letters (bathroom/to-columns (read-file "resources/problem-6.txt"))))))

(defn problem-6-b
  []
  (apply str (map #(key (apply min-key val %)) (map code/count-letters (bathroom/to-columns (read-file "resources/problem-6.txt"))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Problem 2A: " (problem-2-a))
  (println "Problem 2B: " (problem-2-b))
  (println "Problem 3A: " (problem-3-a))
  (println "Problem 3B: " (problem-3-b))
  (println "Problem 4A: " (problem-4-a))
  (println "Problem 4B: " (problem-4-b))
  (println "Problem 5A: " (time (problem-5-a)))
  (println "Problem 5B: " (time (problem-5-b)))
  (println "Problem 6A: " (problem-6-a))
  (println "Problem 6B: " (problem-6-b)))
