# local-forage

ClojureScript binding for [localForage](https://github.com/localForage/localForage)


## Install

[![Clojars Project](https://img.shields.io/clojars/v/com.github.hden/local-forage.svg)]

## Usage

```clojure
(ns my-ns
  (:require [cljs.core.async :as async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [local-forage.core :as storage]))

(def db (storage/create-db {:name "local-forage.core-test"}))
(go
  (<p! (core/set! db "foo" "bar"))
  (<p! (core/get db "foo")))
```

## License

Copyright © 2020 Haokang Den

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
