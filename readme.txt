*** NÁZEV PROJEKTU ***
Projekt z předmětu IJA

*** AUTOŘI ***
Jan Holáň, XHOLAN11
Jan Zimola, XZIMOL04

*** POPIS ***
Projekt v dosavadní verzi umožňuje načítat, vytvářet a upravovat diagram tříd (právě jeden).
Úprava diagramu spočívá v možnosti přidání třídy, posunu třídy na specifickou pozici, vytvoření vztahu s jinou třídou a editace atributů a method třídy. To vše v rámci grafického rozhranní.

*** PŘEKLAD A SPUŠTĚNÍ ***
Projekt byl spouštěn a překládán programem maven (viz soubor pom.xml).
Překlad v adresáři xzimol04: mvn clean package javadoc:javadoc
Spuštění jar souboru aplikace: java -jar ./dest/ija-app.jar
Spuštění v adresáři xzimol04: mvn clean javafx:run

*** OVLÁDÁNÍ ***
Program se ovládá pomocí tlačítek na horní liště - zde je ze začátku umožněno načíst či vytvořit nový diagram tříd. Následně se otevře v rámci hlavního okna grafické rozhranní, umožňující pomocí tlačítek vytvářet a upravovat jednotlivé objekty. Úprava objektů (třídy, rozhranní) nebo vztahu je umožněna právě tehdy, je-li daná třída označena (není-li, tlačítko nic nedělá). V závěru je umožněno uzavřít celé rozhranní editace tlačítkem CLOSE na hlavní liště s kontrolními prvky diagramu tříd.

*** UPOZORNĚNÍ ***	
V dosavadní verzi jsou funkční právě a jen ta tlačítka, jež mají za úkol tu činnost, která souvisí s požadavky, jež byly popsány jako splněné v rámci souboru requirements.pdf!
