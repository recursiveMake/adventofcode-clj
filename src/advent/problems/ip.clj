(ns advent.problems.ip
  (:require [clojure.string :as string]
            [clojure.set :as set]))

(defn repeats?
  [sequence]
  (= (take 2 sequence) (take 2 (reverse sequence))))

(defn different?
  [sequence]
  (not (= (first sequence) (second sequence))))

(defn is-abba?
  [full-sequence]
  (def sequences (partition 4 1 full-sequence))
  (not (= nil (first (filter different? (filter repeats? sequences))))))

(def hypernet-regex #"\[(\w+)\]")

(defn hypernet
  [ip]
  (map second (re-seq hypernet-regex ip)))

(defn not-hypernet
  [ip]
  (string/split (string/replace ip hypernet-regex " ") #" "))

(defn ip
  [address]
  (hash-map :ip (not-hypernet address)
           :hypernet (hypernet address)))

(defn tls?
  [address]
  (def ip-map (ip address))
  (and (not (some is-abba? (ip-map :hypernet)))
       (some is-abba? (ip-map :ip))))

(defn aba?
  [sequence]
  (and (different? sequence)
       (= (first sequence) (last sequence))))

(defn aba
  [supernets]
  (def threes (partial partition 3 1))
  (def sequences (apply concat (map threes supernets)))
  (filter aba? sequences))

(defn bab
  [sequence]
  (seq [(second sequence) (first sequence) (second sequence)]))

(defn ssl?
  [address]
  (def ip-map (ip address))
  (> (count (set/intersection
             (into #{} (map bab (aba (:ip ip-map))))
             (into #{} (aba (:hypernet ip-map)))))
     0))
