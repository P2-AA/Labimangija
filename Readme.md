# Läbimängija

Algoritmide ja andmestruktuuride ainele loodud rakendus läbimängimiseks ning harjutamiseks.

## Nõuded programmi käivitamiseks

- Projekt töötab Java 21 versiooniga,
- kui programm annab käivitamisel vea stiilis `compiled by a more recent version of the Java Runtime`, siis on aktiivne Java versioon liiga vana.

## Programmi käivitamine

Käsuviibal tuleb projekti juurkaustas olles sisestada:
```cmd
.\gradlew run
```

See käsk käivitab läbimängija rakenduse.

## Käivitatava rakenduse jagatava kausta loomine

Projekt on seadistatud jlink-i jaoks. See võimaldab luua käivitatava rakenduse kokkupakitud kujul, koos vajaliku Java jooksutuskeskkonnaga.

Projekti juurkaustas tuleb käsuviibal sisestada:
```cmd
.\gradlew clean jlinkZip
```

Pärast eelmainitud käskluseid tekib kokkupakitud zip-arhiiv, milles sisaldub käivitatav programm:
```text
build/Labimangija-*.zip
```

Rakendust saab käivitada, kui pakkida lahti tekitatud fail ning käivitada järgnev jooksutatav fail:
```text
Labimangija/Labimangija.bat
```

## Sisendfailid ja logid

- Sisendfailid asuvad kaustas `sisendid`,
- Läbimängude logid asuvad kaustas `labimangud`.
