<?xml version="1.0" encoding="UTF-8"?>
<Algo>
    <description courant="SI (Proposition &lt; nbrAlea ) ALORS" texte=""/>
    <extension extnom="inactif"/>
    <fonction fctetat="inactif" fctcode=""/>
    <F2 F2defaut="" F2etat="inactif" F2para="" F2lignes=""/>
    <repere repcode="-10#10#-10#10#2#2" repetat="inactif"/>
    <param epaisseurLigne="2" maxBoucle="500000" totalBoucles="5000000" arrondiAuto="vrai" totalAffichages="1000" epaisseurPoint="2" nbDecimales="8"/>
    <item code="200#declarationfonctions" algoitem="FONCTIONS_UTILISEES"/>
    <item code="100#declarationsvariables" algoitem="VARIABLES">
        <item code="1#NOMBRE#nbrAlea" algoitem="nbrAlea EST_DU_TYPE NOMBRE"/>
        <item code="1#NOMBRE#Proposition" algoitem="Proposition EST_DU_TYPE NOMBRE"/>
    </item>
    <item code="101#debutalgo" algoitem="DEBUT_ALGORITHME">
        <item code="5#nbrAlea#ALGOBOX_ALEA_ENT(1,100)#pasliste" algoitem="nbrAlea PREND_LA_VALEUR ALGOBOX_ALEA_ENT(1,100)"/>
        <item code="15#nbrAlea != Proposition" algoitem="TANT_QUE (nbrAlea != Proposition) FAIRE">
            <item code="16#debuttantque" algoitem="DEBUT_TANT_QUE"/>
            <item code="4#Entrez un chiffre entre 1 et 100#1" algoitem="AFFICHER &quot;Entrez un chiffre entre 1 et 100&quot;"/>
            <item code="2#Proposition#pasliste" algoitem="LIRE Proposition"/>
            <item code="6#Proposition &lt; nbrAlea " algoitem="SI (Proposition &lt; nbrAlea ) ALORS">
                <item code="7#debutsi" algoitem="DEBUT_SI"/>
                <item code="4#Nombre plus grand!#1" algoitem="AFFICHER &quot;Nombre plus grand!&quot;"/>
                <item code="8#finsi" algoitem="FIN_SI"/>
                <item code="9#sinon" algoitem="SINON">
                    <item code="10#debutsinon" algoitem="DEBUT_SINON"/>
                    <item code="6#Proposition > nbrAlea" algoitem="SI (Proposition > nbrAlea) ALORS">
                        <item code="7#debutsi" algoitem="DEBUT_SI"/>
                        <item code="4#Nombre plus petit#1" algoitem="AFFICHER &quot;Nombre plus petit&quot;"/>
                        <item code="8#finsi" algoitem="FIN_SI"/>
                        <item code="9#sinon" algoitem="SINON">
                            <item code="10#debutsinon" algoitem="DEBUT_SINON"/>
                            <item code="4#Bravo vous avez trouvez le nombre!#1" algoitem="AFFICHER &quot;Bravo vous avez trouvez le nombre!&quot;"/>
                            <item code="11#finsinon" algoitem="FIN_SINON"/>
                        </item>
                    </item>
                    <item code="11#finsinon" algoitem="FIN_SINON"/>
                </item>
            </item>
            <item code="17#fintantque" algoitem="FIN_TANT_QUE"/>
        </item>
    </item>
    <item code="102#finalgo" algoitem="FIN_ALGORITHME"/>
</Algo>
