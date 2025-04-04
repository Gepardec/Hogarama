# Add self-signed certificates to your browser or mac

For chrome, firefox and mac we provide instructions

## Chrome

![chrome_1](images/CodeReady-Workspaces-self-signed-certificates/chrome_1.png)

![chrome_2](images/CodeReady-Workspaces-self-signed-certificates/chrome_2.png)

![chrome_3](images/CodeReady-Workspaces-self-signed-certificates/chrome_3.png)

## Firefox

![firefox_1](images/CodeReady-Workspaces-self-signed-certificates/firefox_1.png)

![firefox_2](images/CodeReady-Workspaces-self-signed-certificates/firefox_2.png)

![firefox_3](images/CodeReady-Workspaces-self-signed-certificates/firefox_3.png)

![firefox_4](images/CodeReady-Workspaces-self-signed-certificates/firefox_4.png)

## MAC

On Mac we have to import the certificate (ingress-operator-...) into the KeyChain Access application. The you have to
trust the certificate with “Informationen->Vertrauen->Immer Vertrauen”. In detail:

In the browser you click on the "Not Save" (“Nicht Sicher”) symbol.

![mac_1](images/CodeReady-Workspaces-self-signed-certificates/mac_1.png)

Choose the link “Zertifikat (Ungültig)” You see the certificate chain:

![mac_2](images/CodeReady-Workspaces-self-signed-certificates/mac_2.png)

Choose the top certificate, the one for the ingress-operator. Drag the picture with the certificate picture onto the
desktop. Open the certificate file with the KeyChain Access application:

![mac_3](images/CodeReady-Workspaces-self-signed-certificates/mac_3.png)

In the KeyChain Access application search for the ingress certificate and select it.

![mac_4](images/CodeReady-Workspaces-self-signed-certificates/mac_4.png)

With a right-click select "Information" to get certificate details:

![mac_5](images/CodeReady-Workspaces-self-signed-certificates/mac_5.png)

Trust the certificate by choosing “Immer Vertrauen”
