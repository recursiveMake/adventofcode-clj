(ns advent.problems.compress
  (:require [clojure.string :as string]))

(defn compress-parser
  [sequence-data]
  (let [data (apply str sequence-data)
        fragments (string/split data #"[x\)]" 3)
        characters (Integer. (first fragments))
        repeats (Integer. (second fragments))
        line (last fragments)]
    [(drop characters line) (flatten (concat (repeat repeats (take characters line))))]))

(defn parser
  [data]
  (loop [text data
         result []]
    (if (> (count text) 0)
      (if (= \( (first text))
        (let [parsed (compress-parser (rest text))]
          (recur (first parsed) (apply conj result (second parsed))))
        (recur (rest text) (conj result (first text))))
      (apply str result))))

(defn as-integer
  [text]
  (if (= nil text)
    0
    (Integer. text)))

(defn get-token
  "Parse a line of (nxr)text"
  [text]
  (let [[_ n r t] (string/split text #"[\(\)x]" 4)
        nchars (as-integer n)
        repeats (as-integer r)]
    [nchars repeats t]))

(defn take-from-string
  "Takes n chars from string"
  [n text]
  (apply str (take n text)))

(defn drop-from-string
  "Drops n chars from string"
  [n text]
  (apply str (drop n text)))

(defn tokenize
  "Lazy sequence of (repeats text) tuples"
  [text]
  (lazy-seq
   (let [idx (string/index-of text "(")]
     (if (= nil idx)
       ; no brackets in text (no compression)
       (if (= 0 (count text))
         ; no text
         (seq nil)
         ; uncompressed text
         (cons 
          {:repeats 1 :text text} 
          (seq nil)))
       (if (> idx 0)
         ; uncompressed text preceeds compressed
         (cons 
          {:repeats 1 :text (take-from-string idx text)}
          (tokenize (drop-from-string idx text)))
         ; starts with compressed text
         (let [[n r t] (get-token text)]
           (cons 
            {:repeats r :text (take-from-string n t)}
            (tokenize (drop-from-string n t)))))))))

(defn expand-token
  "Tokenize a token"
  [token]
  (tokenize (:text token)))

(defn fully-expanded?
  "Can this token be further expanded (not)"
  [token]
  (= nil (string/index-of (:text token) "(")))

(defn token-length
  ""
  [token]
  (if (fully-expanded? token)
    (* (:repeats token) (count (:text token)))
    (* (:repeats token) (reduce + (map token-length (expand-token token))))))

(defn line-length
  [line]
  (reduce + (map token-length (tokenize line))))
