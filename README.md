# Intelligente Systeme

Vorlesung Intelligente Systeme bei Professor Stefan Krause.
Projekt erstellt von Malte Grebe und Sheraz Azad.

Aufgabenstellung


Ausgangssituation und Zielsetzung

In einem Fitness-Studio gibt es im Umkleideraum Schränke (Schließfächer), die in zwei Reihen
übereinander angeordnet sind. Ein Kunde des Fitness-Studios wundert sich darüber, dass es
häufig vorkommt, dass sich während seiner Umkleidezeit eine andere Person an einem unmittelbar
benachbarten Schrank umkleidet. Dieses Ereignis wird im Folgenden "Begegnung" genannt. Wir
wollen untersuchen, welche Begegnungshäufigkeiten als "zufällig" betrachtet werden können,
und eine Strategie entwickeln, mit der die Begegnungshäufigkeiten gesenkt werden können.

Als unmittelbar benachbart gelten (sofern vorhanden) die beiden unmittelbar links und rechts
befindlichen Schränke, der darüber bzw. darunter befindliche Schrank sowie die beiden diagonal
angrenzenden Schränke. Die 4 am Rand befindlichen Schränke haben also jeweils 3 Nachbarn,
alle anderen haben 5 Nachbarn.


Weitere Angaben

Die Zahl der Schränke beträgt 150, also 75 pro Reihe.
Zur Vereinfachung nehmen wir an, dass alle 10 Sekunden mit einer Wahrscheinlichkeit von
1/10 eine Person das Fitness-Studio betritt.
Die Aufenthaltsdauern (inkl. Umkleiedezeit) wurden über längere Zeit gemessen. In der
unten angehängten Datei befindet sich eine Tabelle, welche deren Häufigkeitsverteilung
enthält. Die Zeit für das Umkleiden beträgt 5 Minuten jeweils am Anfang und am Ende
eines Besuchs.


Konkrete Aufgabenstellung


Wir gehen davon aus, dass die von uns betrachtete Person, im Folgenden "Fokusperson"
genannt, 10 mal pro Monat das Fitness-Studio besucht. Jeder Besuch erfolgt um ca. 15 Uhr.
Das Fitness-Studio öffnet stets um 10 Uhr und schließt um 22 Uhr.

Entwickeln Sie eine Software zur Beantwortung der folgenden Fragen:

1)
Wieviele Begegnungen hat unsere Fokusperson pro Monat, wenn die Schränke zufällig zugeteilt
werden? In welchem Rahmen bewegen sich diese Zahlen? Ermitteln Sie deren Häufigkeitsverteilung.

2)
Entwickeln Sie eine Strategie, welche die Zahl der Begegnungen pro Monat senkt und demonstrieren
ihre Wirksamkeit.

3)
Angenommen, das Fitness-Studio möchte die Zahl der Begegnungen senken und steht vor der Wahl,
entweder eine Software zu erwerben, welche die unter 2) entwickelte Strategie implementiert
oder 20 neue Schränke anzuschaffen. Was würden Sie dem Fitness-Studio empfehlen, wenn Sie
berücksichtigen, dass das Fitness-Studio die Zahl seiner Mitglieder gerne vergrößern möchte?