# ToDos for HELM-ifying Hogarama

1. Keycloak-Commons & Keycloak chart erstellen
2. Skripting der Helm Installation (gesamt, einzelne resources, flags durchpipen) | helm installation -f für upgrade bei bestehender installation
3. Option für deinstallation hinzufügen bei installationsskript
    1. wichtig: grafana-dashboard bei hogarama deinstallation vorher manuell deinstallieren da der Operator immer einen grafana.cleanup finalizer hinzufügt
    2. ebenso: subscriptions bei den commons als erstes entfernen
3. Vererbung d. Values in Subcharts ansehen (evtl. Skript für Installation von Subcharts only erstellen)
4. Readme schreiben
5. Mehr Konfiguration in Values hinausziehen (evtl. Untersützung Clemens, da sonst sehr mühsam)
