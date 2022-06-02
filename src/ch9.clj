(ns ch9)

;; 1. Write a function that takes a string as an argument 
;; and searches for it on Bing and Google using the slurp function. 
;; Your function should return the HTML of the first page returned by the search. 

(defn search-google-or-bing
  [search-term]
  (let [res-promise (promise)]
    (doseq [search-url ["https://google.com/search?q=" "https://bing.com/search?q="]]
      (future (deliver res-promise (slurp (str search-url search-term)))))
    @res-promise))

(print (search-google-or-bing "hi"))
(print (slurp "https://bing.com/search?q=hi"))

;; 2. Update your function so it takes a second argument 
;; consisting of the search engines to use.

(defn search-multiple
  [search-term search-engines]
  (let [res-promise (promise)]
    (doseq [search-url search-engines]
      (future (deliver res-promise (slurp (str search-url search-term)))))
    @res-promise))

(search-multiple "yo" ["https://google.com/search?q=" "https://bing.com/search?q="])
(search-multiple "yo" ["https://google.com/search?q=" "https://bing.com/search?q=", "https://duckduckgo.com/?q="])

;; 3. Create a new function that takes a search term 
;; and search engines as arguments, 
;; and returns a vector of the URLs from the first page of search results 
;; from each search engine.
