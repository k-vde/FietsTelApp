# FietsTelApp
## Inleiding
applicatie om fietsen te tellen op basis van timelapse video's. Ik heb deze applicatie ontwikkeld om te kunnen deelnemen aan de fietstelweek (www.fietstelweek.be) zonder op de specifieke data en uren tijd te moeten vrijmaken. 

##Installatie
Er is geen installatie nodig om dit programma te gebruiken. Gewoon de Executable uitvoeren is voldoende.

Dit programma maakt gebruik van VLC om video af te spelen. Uw PC moet dus tenminste versie 2.0 van VLC geinstalleerd hebben. https://www.videolan.org/vlc/

##Gebruik
Bij de start van het programma krijgt u een aantal pop up schermen waarin u gevraagd wordt om een video te selecteren, het adres, de coordinaten en het tijdstip in te geven. Deze data zijn belangrijk om het exportbestand in het juiste formaat te zetten voor http://www.dataplatformfiets.be/ 

###invoer gegevens
Bij de start van het programma worden volgende gegevens gevraagd:
1.adres van het telpunt
1.Coordinaten van de locatie.
  1.Open Google Maps op uw computer. 
  1. Klik met de rechtermuisknop op de locatie of het gebied op de kaart.
  1. Selecteer Wat is hier?.
  1. Onderaan ziet u een kaart met de coördinaten.
1.tijdstip waarop de video start.
1.rate: is deze video versnelt opgenomen? (zie hoofdstuk Video met Lapseit)

###export data
Alle data wordt geexporteerd naar een CSV bestand dat rechtstreeks geupload kan worden naar http://www.dataplatformfiets.be/.
De CSV bestanden worden geexporteerd naar de folder waarin het programma zich bevindt.

###creëren van beelden
Videobeelden kunnen aangemaakt worden met eender welk middel. Am 
####Video met Lapseit


andere videobronnen zijn ook mogelijk. (gopro, dashcams, ...) Hievoor zal de rate normaalgesproken 1 zijn.

##toekomstige functionaliteiten
voorlopig staan volgende extra features op het verlanglijstje.
*rechtstreekse upload naar dataplatformfiets.be
*onthouden van gebruikte adressen
*uitbreiden met functionaliteit voor het tellen van voetgangers, personenwagens en vrachtwagens
*verbetering interface

##Contributie
Alle hulp is welkom. De huidige code base kan waarschijnlijk veel beter/duidelijker. 
Aanvragen voor functionaliteit of bug reports zijn ook zeer welkom.

##licentie
Deze code is gelicentieerd onder GNU Affero General Public License v3.0.
