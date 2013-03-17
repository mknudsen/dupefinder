# dupefinder

A tool for finding duplicate files within a directory.

Dupefinder recurses into subdirectories and compares files with the same size
by computing their MD5-Hashes.

This is mainly a toy project which I wrote to explore clojure.

Similar tools with way more features that are probably more stable are listed on this Wikipedia page: http://en.wikipedia.org/wiki/Fdupes

## Usage

build it with

    lein uberjar

run it with

    java -jar target/dupefinder-VERSION-standalone.jar /some/directory/

Outputs filesize, hash and absolute path to the suspected duplicates

    47 	 c347d69b388abbabaf2f894c4200465c 	 /some/directory/some.file.txt
    47 	 c347d69b388abbabaf2f894c4200465c 	 /some/directory/duplicate.file.txt

## License

Copyright © 2013 Martin Knudsen

Distributed under the Eclipse Public License, the same as Clojure.
