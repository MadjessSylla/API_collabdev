<?xml version="1.0" encoding="UTF-8"?>
<Algo>
    <description texte="" courant="AFFICHER sms"/>
    <extension extnom="inactif"/>
    <fonction fctetat="inactif" fctcode=""/>
    <F2 F2etat="inactif" F2defaut="" F2lignes="" F2para=""/>
    <repere repetat="inactif" repcode="-10#10#-10#10#2#2"/>
    <param maxBoucle="500000" epaisseurLigne="2" nbDecimales="8" arrondiAuto="vrai" totalBoucles="5000000" epaisseurPoint="2" totalAffichages="1000"/>
    <item algoitem="FONCTIONS_UTILISEES" code="200#declarationfonctions"/>
    <item algoitem="VARIABLES" code="100#declarationsvariables">
        <item algoitem="prenom EST_DU_TYPE CHAINE" code="1#CHAINE#prenom"/>
        <item algoitem="sms EST_DU_TYPE CHAINE" code="1#CHAINE#sms"/>
    </item>
    <item algoitem="DEBUT_ALGORITHME" code="101#debutalgo">
        <item algoitem="AFFICHER &quot;Bonjour ,monde&quot;" code="4#Bonjour ,monde#0"/>
        <item algoitem="AFFICHER &quot;Quel est votre prénom?&quot;" code="4#Quel est votre prénom?#1"/>
        <item algoitem="LIRE prenom" code="2#prenom#pasliste"/>
        <item algoitem="sms PREND_LA_VALEUR &quot;Bonjour &quot; + prenom" code="5#sms#&quot;Bonjour &quot; + prenom#pasliste"/>
        <item algoitem="AFFICHER sms" code="3#sms#0#pasliste"/>
    </item>
    <item algoitem="FIN_ALGORITHME" code="102#finalgo"/>
</Algo>
