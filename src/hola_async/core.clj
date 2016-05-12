(ns hola-async.core
  (:require [clojure.core.async :as a]))

(def my-channel (a/chan))


;; Example of one thread waiting for another thread:

(future (println (a/<!! my-channel)))
;; Everything that happens inside a future, happens in a different JVM thread, a new completedly different process
;; Now we have to threads working
;; the future is blocking his thread until it gets a value

(a/>!! my-channel "My first value")
;; When we evaluate this, it get printed from the other thread, because it was waiting.
;; If we evaluate this again it will block this thread forever, because we have no one to pull a value from the chan.


(future (loop [] (when-let [v (a/<!! my-channel)] (println v) (recur))))
;; It´s important that we recur from within a when-let, because when the thread is closed, when it returns nil,
;; we don´t want it to continue looping forever. This way it will wait until something is available from the channel,
;; print it, go to the top and wait again.

(a/>!! my-channel "a")
(a/>!! my-channel "b")
(a/>!! my-channel "c")

;; a/>!! returns true if it succeed to put something inside of the channel
;; It returns false if the channel is closed
;; Now we can add all values to the channel that we want and they will be printed
