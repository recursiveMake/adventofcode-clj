(ns advent.problems.hash)

(defn hash-string
  [line]
  (apply str
         (map (partial format "%02x")
              (.digest (doto (java.security.MessageDigest/getInstance "MD5")
                         .reset
                         (.update (.getBytes line)))))))

(defn is-interesting?
  [hash]
  (def n 5)
  (= (repeat n \0) (take n hash)))

(defn get-index
  [hash]
  (if (Character/isDigit (nth hash 5))
    (Integer. (str (nth hash 5)))))

(defn is-valid?
  [hash]
  (if-not (= nil (get-index hash))
    (< (get-index hash) 8)
    false))

(defn new-password-promise
  []
  (repeatedly 8 promise)
  ;; (apply merge (map #(hash-map (keyword (str %)) (promise)) (range 0 8)))
  )

(defn is-new?
  [password hash]
  (not (realized? (nth password (get-index hash)))))

(defn not-finished?
  [password]
  (not (= nil (first (remove realized? password)))))

(defn save
  [password hash]
  (do
    (deliver (nth password (get-index hash)) (nth hash 6))
    password))
