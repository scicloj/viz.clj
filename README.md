# viz.clj

A Data Visualization Clojure library for beginners.

[![Clojars Project](https://img.shields.io/clojars/v/org.scicloj/viz.clj.svg)](https://clojars.org/org.scicloj/viz.clj)

## Goal

Create a beginner-friendly library for data visualization in Clojure.

## Status

Viz.clj is already useful but is in alpha stage.

As it has been doing since the middle of 2021, it will keep evolving for a while, following user comments and experiments in the Scicloj weekend sessions.

This means the API may still change.

## [Usage](https://scicloj.github.io/viz.clj/#/notebooks/intro.clj)

## The landscape

Data visualization through [Vega](https://vega.github.io/vega/) and [Vega-Lite](https://vega.github.io/vega-lite/) is quite nice from Clojure. These libraries embrace the declarative approach, representing everything in plain JSON structures, and Clojure offers flexible and expressive ways to handle such situations.

One quite powerful Clojure library for data visualization is [Hanami](https://github.com/jsa-aerial/hanami). Hanami offers, among other things, a recursive subsitution mechanism that can generate Vega/Vega-Lite specs using pre-defined defaults and templates. It is very general, extenible and user-customizable. While powerful, Hanami requires some time to learn. Also, user mistakes and data problems may result in errors which are not always easy to diagnose.

Another great Clojure library that can generate Vega specs is [tech.viz](https://github.com/techascent/tech.viz). It has a quite nice collection of data visualization functions. Some of them are capable of backend-side preprocessing (e.g.), and thus allow for a faster pathway compared to client side transformations (e.g., counting data in bins for a histogram).

## Viz.clj in the landscape

Viz.clj wishes to add the following capabilities to the existing stack:
* generate Vega-Lite specs expressed as pipelines of functional transformations
* actually generate Hanami substitutions as the last step before Vega-Lite
* thus allow the user to gradually learn & enjoy Hanami
* catch and explain common errors
* integrate well with [tech.ml.dataset](https://github.com/techascent/tech.ml.dataset) / [Tablecloth](https://scicloj.github.io/tablecloth/index.html) as a data source
* gradually grow a coherent grammar of plot transformations based on user feedback
* make the grammar play nicely with datasets transformations, following the [metamorph](https://github.com/scicloj/metamorph) approach

## The work process

Viz.clj has been using a few sources of inspiration:

* feedback of many user sessions at the Scicloj study groups

* Vega/Vega-Lite wrappers of other ecosystems, such as [Altair](https://altair-viz.github.io/), [qplot](https://ggplot2.tidyverse.org/reference/qplot.html), [ggvis](https://ggvis.rstudio.com/), and [Vega-Lite API](https://vega.github.io/vega-lite-api/)

* the Clojure habit of passing context maps through pipelines of functional transformations, demonstrated in Web infrastructure such as [Ring](https://github.com/ring-clojure/ring), event handling in UI libraries such as [cljfx](https://github.com/cljfx/cljfx), data science libraries such as [metamorph.ml](https://github.com/scicloj/metamorph.ml), and more


## License

Copyright (c) 2021 Scicloj

This program and the accompanying materials are made available under the terms of the Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary Licenses when the conditions for such availability set forth in the Eclipse Public License, v. 2.0 are satisfied: GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version, with the GNU Classpath Exception which is available at https://www.gnu.org/software/classpath/license.html.
