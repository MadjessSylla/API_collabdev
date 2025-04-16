<?xml version="1.0" encoding="UTF-8"?>
<Algo>
    <description texte="" courant="AFFICHER monnaie"/>
    <extension extnom="inactif"/>
    <fonction fctcode="" fctetat="inactif"/>
    <F2 F2para="" F2lignes="" F2etat="inactif" F2defaut=""/>
    <repere repcode="-10#10#-10#10#2#2" repetat="inactif"/>
    <param maxBoucle="500000" totalBoucles="5000000" epaisseurLigne="2" nbDecimales="8" arrondiAuto="vrai" epaisseurPoint="2" totalAffichages="1000"/>
    <item code="200#declarationfonctions" algoitem="FONCTIONS_UTILISEES"/>
    <item code="100#declarationsvariables" algoitem="VARIABLES">
        <item code="1#NOMBRE#compteur" algoitem="compteur EST_DU_TYPE NOMBRE"/>
        <item code="1#CHAINE#nom1" algoitem="nom1 EST_DU_TYPE CHAINE"/>
        <item code="1#CHAINE#nom2" algoitem="nom2 EST_DU_TYPE CHAINE"/>
        <item code="1#CHAINE#nom3" algoitem="nom3 EST_DU_TYPE CHAINE"/>
        <item code="1#CHAINE#nom" algoitem="nom EST_DU_TYPE CHAINE"/>
        <item code="1#LISTE#quantites" algoitem="quantites EST_DU_TYPE LISTE"/>
        <item code="1#LISTE#prix_unitaires" algoitem="prix_unitaires EST_DU_TYPE LISTE"/>
        <item code="1#LISTE#sous_totals" algoitem="sous_totals EST_DU_TYPE LISTE"/>
        <item code="1#NOMBRE#total" algoitem="total EST_DU_TYPE NOMBRE"/>
        <item code="1#NOMBRE#montant_verse" algoitem="montant_verse EST_DU_TYPE NOMBRE"/>
        <item code="1#NOMBRE#monnaie" algoitem="monnaie EST_DU_TYPE NOMBRE"/>
    </item>
    <item code="101#debutalgo" algoitem="DEBUT_ALGORITHME">
        <item code="19# Lecture du nom des trois produits" algoitem="// Lecture du nom des trois produits"/>
        <item code="4#Nom du produit 1#0" algoitem="AFFICHER &quot;Nom du produit 1&quot;"/>
        <item code="2#nom1#pasliste" algoitem="LIRE nom1"/>
        <item code="4#Nom du produit 2#0" algoitem="AFFICHER &quot;Nom du produit 2&quot;"/>
        <item code="2#nom2#pasliste" algoitem="LIRE nom2"/>
        <item code="4#Nom du produit 3#0" algoitem="AFFICHER &quot;Nom du produit 3&quot;"/>
        <item code="2#nom3#pasliste" algoitem="LIRE nom3"/>
        <item code="19# Initialisation des variables LISTE : quantites, produit et sous_totals" algoitem="// Initialisation des variables LISTE : quantites, produit et sous_totals"/>
        <item code="5#quantites#0:0:0#1" algoitem="quantites[1] PREND_LA_VALEUR 0:0:0"/>
        <item code="5#prix_unitaires#0:0:0#1" algoitem="prix_unitaires[1] PREND_LA_VALEUR 0:0:0"/>
        <item code="5#sous_totals#0:0:0#1" algoitem="sous_totals[1] PREND_LA_VALEUR 0:0:0"/>
        <item code="19# Lecture de chacun des quantités et du prix unitaire" algoitem="// Lecture de chacun des quantités et du prix unitaire"/>
        <item code="12#compteur#1#3" algoitem="POUR compteur ALLANT_DE 1 A 3">
            <item code="13#debutpour" algoitem="DEBUT_POUR"/>
            <item code="4#Entrez la quantité du produit #0" algoitem="AFFICHER &quot;Entrez la quantité du produit &quot;"/>
            <item code="3#compteur#0#pasliste" algoitem="AFFICHER compteur"/>
            <item code="2#quantites#compteur" algoitem="LIRE quantites[compteur]"/>
            <item code="4#Entrez le prix unitaire du produit #0" algoitem="AFFICHER &quot;Entrez le prix unitaire du produit &quot;"/>
            <item code="3#compteur#0#pasliste" algoitem="AFFICHER compteur"/>
            <item code="2#prix_unitaires#compteur" algoitem="LIRE prix_unitaires[compteur]"/>
            <item code="5#sous_totals#quantites[compteur] * prix_unitaires[compteur]#compteur" algoitem="sous_totals[compteur] PREND_LA_VALEUR quantites[compteur] * prix_unitaires[compteur]"/>
            <item code="14#finpour" algoitem="FIN_POUR"/>
        </item>
        <item code="4#------ TICKET DE CAISSE ------#1" algoitem="AFFICHER &quot;------ TICKET DE CAISSE ------&quot;"/>
        <item code="12#compteur#1#3" algoitem="POUR compteur ALLANT_DE 1 A 3">
            <item code="13#debutpour" algoitem="DEBUT_POUR"/>
            <item code="19# Affichage du nom du produit" algoitem="// Affichage du nom du produit"/>
            <item code="6#compteur == 1" algoitem="SI (compteur == 1) ALORS">
                <item code="7#debutsi" algoitem="DEBUT_SI"/>
                <item code="5#nom#nom1#pasliste" algoitem="nom PREND_LA_VALEUR nom1"/>
                <item code="8#finsi" algoitem="FIN_SI"/>
                <item code="9#sinon" algoitem="SINON">
                    <item code="10#debutsinon" algoitem="DEBUT_SINON"/>
                    <item code="6#compteur == 2" algoitem="SI (compteur == 2) ALORS">
                        <item code="7#debutsi" algoitem="DEBUT_SI"/>
                        <item code="5#nom#nom2#pasliste" algoitem="nom PREND_LA_VALEUR nom2"/>
                        <item code="8#finsi" algoitem="FIN_SI"/>
                        <item code="9#sinon" algoitem="SINON">
                            <item code="10#debutsinon" algoitem="DEBUT_SINON"/>
                            <item code="5#nom#nom3#pasliste" algoitem="nom PREND_LA_VALEUR nom3"/>
                            <item code="11#finsinon" algoitem="FIN_SINON"/>
                        </item>
                    </item>
                    <item code="11#finsinon" algoitem="FIN_SINON"/>
                </item>
            </item>
            <item code="3#nom#0#pasliste" algoitem="AFFICHER nom"/>
            <item code="4# - #0" algoitem="AFFICHER &quot; - &quot;"/>
            <item code="3#quantites#0#compteur" algoitem="AFFICHER quantites[compteur]"/>
            <item code="4# x #0" algoitem="AFFICHER &quot; x &quot;"/>
            <item code="3#prix_unitaires#0#compteur" algoitem="AFFICHER prix_unitaires[compteur]"/>
            <item code="4# = #0" algoitem="AFFICHER &quot; = &quot;"/>
            <item code="3#sous_totals#1#compteur" algoitem="AFFICHER sous_totals[compteur]"/>
            <item code="14#finpour" algoitem="FIN_POUR"/>
        </item>
        <item code="19# Calcul la somme total des produits" algoitem="// Calcul la somme total des produits"/>
        <item code="5#total#ALGOBOX_SOMME(sous_totals,1,3)#pasliste" algoitem="total PREND_LA_VALEUR ALGOBOX_SOMME(sous_totals,1,3)"/>
        <item code="19# Inviter l'utilisateur à entrer le montant à payer" algoitem="// Inviter l'utilisateur à entrer le montant à payer"/>
        <item code="4#Total à payer : #0" algoitem="AFFICHER &quot;Total à payer : &quot;"/>
        <item code="3#total#1#pasliste" algoitem="AFFICHER total"/>
        <item code="4#Entrer le montant payé dans la caisse#0" algoitem="AFFICHER &quot;Entrer le montant payé dans la caisse&quot;"/>
        <item code="2#montant_verse#pasliste" algoitem="LIRE montant_verse"/>
        <item code="15#montant_verse &lt; total" algoitem="TANT_QUE (montant_verse &lt; total) FAIRE">
            <item code="16#debuttantque" algoitem="DEBUT_TANT_QUE"/>
            <item code="4#Le montant est insuffisant !\n Veuillez mettre une somme supérieur ou égale au total à payer !#0" algoitem="AFFICHER &quot;Le montant est insuffisant !\n Veuillez mettre une somme supérieur ou égale au total à payer !&quot;"/>
            <item code="2#montant_verse#pasliste" algoitem="LIRE montant_verse"/>
            <item code="17#fintantque" algoitem="FIN_TANT_QUE"/>
        </item>
        <item code="5#monnaie#montant_verse - total#pasliste" algoitem="monnaie PREND_LA_VALEUR montant_verse - total"/>
        <item code="4#La monnaie s'élève à #0" algoitem="AFFICHER &quot;La monnaie s'élève à &quot;"/>
        <item code="3#monnaie#0#pasliste" algoitem="AFFICHER monnaie"/>
    </item>
    <item code="102#finalgo" algoitem="FIN_ALGORITHME"/>
</Algo>
