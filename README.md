# TestKinomap

Application Android en Kotlin avec Jetpack Compose pour afficher et consulter des badges depuis une API.

---

##  Fonctionnalités

- Affichage de badges par **catégorie**
- Filtrage instantané par **recherche (insensible à la casse et aux accents)**
- Vue **détail du badge** : image (verrouillée/déverrouillée), titre, catégorie, progression, date de déblocage
- Thèmes **Dark / Light** avec support des couleurs dynamiques (Android 12+)
- États de l’interface : **Loading / Vide**
- Architecture propre : **Clean Architecture** (data/domain/presentation)
- Navigation Compose & **Hilt** pour l’injection de dépendances
- **Tests unitaires** du ViewModel et Use Cases

---

##  Stack technique

| Layer         | Technologies / Bibliothèques                             |
|---------------|----------------------------------------------------------|
| Android SDK   | Kotlin ≥ 1.8, minSdk 21, compileSdk 36                   |
| UI            | Jetpack Compose + Material3, Coil pour les images        |
| Architecture  | Hilt, Retrofit, Clean Architecture                       |
| Réactivité    | Coroutines + Flow                                        |
| Testing       | JUnit, Coroutines Test                                   |

---

##  Architecture & Organisation

app/

├– data/ (API, Retrofit, mappers) 

├– domain/ (Entities + UseCases + Repository interfaces)

├– presentation/

│ ├– badgesList/

│ ├– badgeDetail/

│ ├– navigation/

│ └– theme/

└– di/ (Hilt modules)

---

##  Clean Architecture :
- `data` implémente les interfaces du `domain`
- `presentation` gère la logique UI et les ViewModels

---

##  Comment l’installer et le lancer

> [!IMPORTANT]  
> Vous devez ajouter la ligne suivante dans le fichier **local.properties** à la racine du projet :  
>
> ```properties
> # Token
> KINOMAP_APP_TOKEN=Y7pNWqI4nlYuGBILm46tqw57aKInntGTpzQau30To8WDSt6ZOU60GHWG8QSyWIs1TsFrnheftxBmmFWxR4eKhUWruEndo0aXaZVC6tn9fWhdBDb0ThVvmY6E
> ```  
>
> Ceci est nécessaire pour que l'application puisse communiquer avec l'API Kinomap.

1. Récupère le dépôt :
bash
git clone https://github.com/Xaice01/TestKinomap.git
cd TestKinomap
Ouvre avec Android Studio Dolphin (ou plus récent)

Ajoute un token API (à cacher via local.properties : KINOMAP_TOKEN=…)

Sync Gradle, puis Run sur un émulateur ou appareil

2. Via l’APK (installation directe)

Télécharge le fichier APK depuis la section Releases (https://github.com/Xaice01/TestKinomap/tree/development/app/release) du dépôt GitHub

Copie-le sur ton téléphone Android

Active l’installation depuis des sources inconnues (⚙️ Paramètres → Sécurité)

Installe l’APK et lance l’application 🎉

## Captures d’écran
<img width="394" height="852" alt="Capture d'écran 2025-08-28 111944" src="https://github.com/user-attachments/assets/ba1ff74f-7d8b-44fb-af26-558ed8cd5619" />
<img width="398" height="844" alt="Capture d'écran 2025-08-28 112015" src="https://github.com/user-attachments/assets/66c470ee-ed02-40db-9c5f-c324bfce304a" />



Développé par Xavier Carpentier.
