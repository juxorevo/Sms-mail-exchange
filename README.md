# Sms-Mail
Envoie vos sms par mail.
Envoie vos mail par sms.


Installation de l'application : 

1 / Dans votre boite mail gmail créer les trois libellés suivants :
   - MsgRetour
   - msgRecep
   - txtmsg

2 / Dans votre boite mail, créer trois règles de filtre dans votre boite mail qui sont les suivantes : 
   - Correspondances: subject:(return--Message bien envoyé) Action: Ne pas afficher dans la boîte de réception, Appliquer le libellé "MsgRetour"
   - Correspondances: from:(VOTREMAIL@gmail.com) ContenuSpecialTxt -{return--Message bien envoyé} Action: Ne pas afficher dans la boîte de réception, Appliquer le libellé "msgRecep"
   - Correspondances: from:(VOTREMAIL@gmail.com) subject:txtmsg -ContenuSpecialTxt Action: Ne pas afficher dans la boîte de réception, Appliquer le libellé "txtmsg"

3 / Installer l'application android

4 / Renseigner votre mail et le mot de passe (uniquement GMAIL) dans l'application

5 / Cliquer sur le bouton : "STOP SNIFFING"

Utilisation de l'application :

à partir de maintenant les actions suivantes sont possibles :
   - Tous vos sms vont vous être envoyés par mail à votre adresse gmail.
   - Pour répondre à un sms par sms, simplement répondre au mail que vous avez reçu. (l'application doit être lancée)
   - Si vous souhaitez envoyer un sms via votre boite mail, vous envoyez un mail à vous mêmes avec en objet txtmsg--06xxxxxxxx
   
/!\ Attention j'ai intégré une fonction dans l'application qui permet d'envoyer votre position si vous recevez par sms Cmd--position./!\ Cette fonction n'est pas encore sécurisée /!\
