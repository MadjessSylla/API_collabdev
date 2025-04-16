<?xml version="1.0" encoding="UTF-8"?>
<Algo>
    <description courant="SI (mot[i]==&quot;a&quot; OU mot[i]==&quot;e&quot;  OU mot[i]==&quot;i&quot;  OU mot[i]==&quot;o&quot;  OU mot[i]==&quot;u&quot;  OU mot[i]==&quot;y&quot; ) ALORS" texte=""/>
    <extension extnom="inactif"/>
    <fonction fctetat="inactif" fctcode=""/>
    <F2 F2defaut="" F2etat="inactif" F2para="" F2lignes=""/>
    <repere repcode="-10#10#-10#10#2#2" repetat="inactif"/>
    <param epaisseurLigne="2" maxBoucle="500000" totalBoucles="5000000" arrondiAuto="vrai" totalAffichages="1000" epaisseurPoint="2" nbDecimales="8"/>
    <item code="200#declarationfonctions" algoitem="FONCTIONS_UTILISEES"/>
    <item code="100#declarationsvariables" algoitem="VARIABLES">
        <item code="1#CHAINE#mot" algoitem="mot EST_DU_TYPE CHAINE"/>
        <item code="1#NOMBRE#i" algoitem="i EST_DU_TYPE NOMBRE"/>
        <item code="1#NOMBRE#nbVoyelles" algoitem="nbVoyelles EST_DU_TYPE NOMBRE"/>
    </item>
    <item code="101#debutalgo" algoitem="DEBUT_ALGORITHME">
        <item code="4#Veuillez saisir un mot : #0" algoitem="AFFICHER &quot;Veuillez saisir un mot : &quot;"/>
        <item code="2#mot#pasliste" algoitem="LIRE mot"/>
        <item code="5#nbVoyelles#0#pasliste" algoitem="nbVoyelles PREND_LA_VALEUR 0"/>
        <item code="12#i#0#mot.length" algoitem="POUR i ALLANT_DE 0 A mot.length">
            <item code="13#debutpour" algoitem="DEBUT_POUR"/>
            <item code="6#mot[i]==&quot;a&quot; OU mot[i]==&quot;e&quot;  OU mot[i]==&quot;i&quot;  OU mot[i]==&quot;o&quot;  OU mot[i]==&quot;u&quot;  OU mot[i]==&quot;y&quot; " algoitem="SI (mot[i]==&quot;a&quot; OU mot[i]==&quot;e&quot;  OU mot[i]==&quot;i&quot;  OU mot[i]==&quot;o&quot;  OU mot[i]==&quot;u&quot;  OU mot[i]==&quot;y&quot; ) ALORS">
                <item code="7#debutsi" algoitem="DEBUT_SI"/>
                <item code="5#nbVoyelles#nbVoyelles+1#pasliste" algoitem="nbVoyelles PREND_LA_VALEUR nbVoyelles+1"/>
                <item code="8#finsi" algoitem="FIN_SI"/>
            </item>
            <item code="14#finpour" algoitem="FIN_POUR"/>
        </item>
        <item code="3#nbVoyelles#1#pasliste" algoitem="AFFICHER nbVoyelles"/>
    </item>
    <item code="102#finalgo" algoitem="FIN_ALGORITHME"/>
</Algo>
