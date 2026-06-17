# NeoWarp

Ein performantes und leichtgewichtiges Warp-System für Paper 1.21+, entwickelt von Christian für die NeoMine Developer-Bewerbung. Das Plugin bietet ein vollständiges Warp-Management mit GUI, Paginierung, sicheren Teleports und persistenter YAML-Datenspeicherung.

## ✨ Features

- **Interaktives GUI:** Übersichtsmenü (`/warpgui`) mit dynamischer Paginierung (ab 28 Warps) und umgebungsspezifischen Icons (Netherrack, Endstone, Gras).
- **Sicheres Teleportieren:** 3 Sekunden Warmup-Zeit. Der Teleport wird bei Bewegung oder erlittenem Schaden sofort abgebrochen.
- **Combat-Log:** Teleports sind während eines aktiven Kampfes (10 Sekunden Cooldown nach einem Treffer) blockiert.
- **Tab-Completion:** Vollständige Autovervollständigung für alle relevanten Befehle.
- **Speicherung:** Performante asynchrone Teleports und synchrone YAML-Speicherung der Koordinaten inklusive Yaw, Pitch und Ersteller-UUID.

---

## 📦 Setup & Installation

1. Lade die aktuellste `NeoWarp-1.0.jar` herunter oder kompiliere sie selbst.
2. Kopiere die `.jar`-Datei in den `plugins/` Ordner deines Paper/Spigot-Servers (Version 1.21+).
3. Starte den Server neu oder lade die Plugins neu.
4. Richte die untenstehenden Permissions in deinem Rechte-System (z. B. LuckPerms) ein.

---

## 💻 Befehle

| Befehl | Beschreibung |
| :--- | :--- |
| `/setwarp <name>` | Erstellt einen neuen Warp an deiner aktuellen Position. |
| `/warp <name>` | Teleportiert dich zum angegebenen Warp (3s Warmup). |
| `/delwarp <name>` | Löscht den angegebenen Warp. |
| `/warpgui` | Öffnet das grafische Warp-Menü. |
| `/warplist` | Sendet eine Liste aller existierenden Warps in den Chat. |

---

## 🔑 Permissions

| Permission | Erlaubnis |
| :--- | :--- |
| `warptest.setwarp` | Erlaubt das Erstellen von neuen Warps via `/setwarp`. |
| `warptest.warp` | Erlaubt die Nutzung von `/warp`, `/warpgui` und `/warplist`. |
| `warptest.delwarp` | Erlaubt das Löschen der **selbst erstellten** Warps. |
| `warptest.admin` | Erlaubt das Löschen **aller** Warps (Bypass für Fremd-Warps). |
