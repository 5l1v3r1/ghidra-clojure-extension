# Ghidra Clojure Extension

Ghidra Clojure Extension is an extension for scripting Ghidra in [Clojure](https://clojure.org), a lisp dialect.

THe project just just start a while, there might be many bugs. You are welcome to collaborate or report to imporve the tool.


# Build 

Change `ghidraInstallDir` in **build.gradle** to your installation directory. Then type `gradle build` and `gradle` to start
build the tool.

To install the extension. Use **Install Extension** in Ghidra Code Browser. The extension is under `dist` directory.
Create a script with `.clj` suffix to write your clojure script. 

# Example 

**Clojure** does not support Java `extends`. We create varaible `ghidra`, a `GhidraScript` instance to use all the methods and attributes in
`GhidraScript`. Simply use `(.anAtrribute ghidra)` or `(.aMethod ghidra arg1 arg2)` to use them.

For example:
```clojure
(def x 
  (.askInt ghidra "How many?" "num"))

(prn (map 
  (fn [i] (str currentAddress " " i))
(range x)))
```
