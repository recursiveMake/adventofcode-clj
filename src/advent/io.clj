(ns advent.io
  (:require [clojure.string :as string]
            [clojure.java.io :as io]))

(defn old-read-file
  [file-name line-func]
  (with-open [rdr (io/reader file-name)]
    (doseq [line (line-seq rdr)]
      (line-func line))))

(defn read-file
  [file-name]
  (string/split-lines (slurp file-name)))
