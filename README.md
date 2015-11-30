# Sms-Mail
Envoie des sms par mail et réponse par mail pour renvoyer par sms


Utilisation de l'application : 

1 / Installer l'application ANDROID

2 / Créer trois règles de filtre dans votre boite mail qui sont les suivantes : 
   - Correspondances: subject:(return--Message bien envoyé) Action: Ne pas afficher dans la boîte de réception, Appliquer le libellé "MsgRetour"
   - Correspondances: from:(VOTREMAIL@gmail.com) ContenuSpecialTxt -{return--Message bien envoyé} Action: Ne pas afficher dans la boîte de réception, Appliquer le libellé "msgRecep"
   - Correspondances: from:(VOTREMAIL@gmail.com) subject:txtmsg -ContenuSpecialTxt Action: Ne pas afficher dans la boîte de réception, Appliquer le libellé "txtmsg"

3 / Renseigner votre mail et le mot de passe (uniquement GMAIL) dans l'application

4 / Cliquer sur le bouton : "STOP SNIFFING"
