<?xml version="1.0" encoding="UTF-8"?>
<Algo>
    <description texte="" courant="AFFICHER quotient"/>
    <extension extnom="inactif"/>
    <fonction fctcode="" fctetat="inactif"/>
    <F2 F2lignes="" F2para="" F2etat="inactif" F2defaut=""/>
    <repere repetat="inactif" repcode="-10#10#-10#10#2#2"/>
    <param maxBoucle="500000" totalBoucles="5000000" arrondiAuto="vrai" totalAffichages="1000" nbDecimales="8" epaisseurLigne="2" epaisseurPoint="2"/>
    <item algoitem="FONCTIONS_UTILISEES" code="200#declarationfonctions"/>
    <item algoitem="VARIABLES" code="100#declarationsvariables">
        <item algoitem="a EST_DU_TYPE NOMBRE" code="1#NOMBRE#a"/>
        <item algoitem="b EST_DU_TYPE NOMBRE" code="1#NOMBRE#b"/>
        <item algoitem="somme EST_DU_TYPE NOMBRE" code="1#NOMBRE#somme"/>
        <item algoitem="difference EST_DU_TYPE NOMBRE" code="1#NOMBRE#difference"/>
        <item algoitem="produit EST_DU_TYPE NOMBRE" code="1#NOMBRE#produit"/>
        <item algoitem="quotient EST_DU_TYPE NOMBRE" code="1#NOMBRE#quotient"/>
    </item>
    <item algoitem="DEBUT_ALGORITHME" code="101#debutalgo">
        <item algoitem="LIRE a" code="2#a#pasliste"/>
        <item algoitem="LIRE b" code="2#b#pasliste"/>
        <item algoitem="somme PREND_LA_VALEUR a+b" code="5#somme#a+b#pasliste"/>
        <item algoitem="AFFICHER &quot;La somme de a et b est :&quot;" code="4#La somme de a et b est :#1"/>
        <item algoitem="AFFICHER somme" code="3#somme#1#pasliste"/>
        <item algoitem="difference PREND_LA_VALEUR a-b" code="5#difference#a-b#pasliste"/>
        <item algoitem="AFFICHER &quot;La difference de a et b est :&quot;" code="4#La difference de a et b est :#1"/>
        <item algoitem="AFFICHER difference" code="3#difference#1#pasliste"/>
        <item algoitem="produit PREND_LA_VALEUR a*b" code="5#produit#a*b#pasliste"/>
        <item algoitem="AFFICHER &quot;Le produit de a et b est :&quot;" code="4#Le produit de a et b est :#1"/>
        <item algoitem="AFFICHER produit" code="3#produit#1#pasliste"/>
        <item algoitem="a PREND_LA_VALEUR a/b" code="5#a#a/b#pasliste"/>
        <item algoitem="AFFICHER &quot;Le quotient de a et b est :&quot;" code="4#Le quotient de a et b est :#1"/>
        <item algoitem="AFFICHER quotient" code="3#quotient#0#pasliste"/>
    </item>
    <item algoitem="FIN_ALGORITHME" code="102#finalgo"/>
</Algo>
