(ns dupefinder.core
  (:gen-class )
  (:require [digest :as d]
            [clojure.core.reducers :as r]))

(defn file? [file]
  (.isFile file))

(defn files [target-dir]
  (filter file? (file-seq target-dir)))

(defn filter-empty [files]
  (filter #(< 0 (.length %)) files))

(defn by-size [file-seq]
  (group-by #(.length %) file-seq))

(defn multiple-values? [entry]
  (let [[key vals] entry]
    (and seq? (> (count vals) 1))))

(defn filter-multiple [map]
  (filter multiple-values? map))

(defn same-size [files-by-size]
  (filter-multiple files-by-size))

(defn files-by-hash [files]
  (group-by #(str (d/md5 %)) files))

(defn hash-files [files-by-size]
  (into []
    (r/map #(let [[size files] %]
              [size (files-by-hash files)]) files-by-size)))

(defn remove-unique-hashes [size-hashes-files]
  (let
    [[size hashes-to-files] size-hashes-files]
    [size (filter multiple-values?
      hashes-to-files)]))

(defn only-duplicates [size-hashes-files]
  (map remove-unique-hashes size-hashes-files))

(defn non-empty [size-hashes-files]
  (filter #(not (zero? (count (second %))))
    size-hashes-files))

(defn -main [dir & args]
  (doseq
    [dupe
     (->
       (clojure.java.io/file dir)
       files
       filter-empty
       by-size
       same-size
       hash-files
       only-duplicates
       non-empty)]

    (let [[size files-by-hash] dupe]
      (do
        (doseq [[hash files] files-by-hash]
          (doseq [file files]
            (println size "\t" hash "\t" (.getAbsolutePath file))))))))