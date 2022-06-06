# Zimmerzuteilung

Dependencies:
1. Gurobi Optimizer: https://www.gurobi.com/products/gurobi-optimizer/
2. Java 17

How to run: 
1. Clone repository
2. cd Zimmerzuteilung
3. ./gradlew run

Aktuell werden "zufällige" Schüler "zufälligen" Räumen zugewiesen. Dabei werden die drei Regeln eingehalten: 1 Zimmer pro Person, maximal so viele Personen pro Zimmer wie das Zimmer Platz hat und keine gemischten (m, w, d) Zimmer.
